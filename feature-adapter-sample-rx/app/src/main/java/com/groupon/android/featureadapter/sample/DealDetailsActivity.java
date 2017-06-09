package com.groupon.android.featureadapter.sample;

import static rx.Observable.just;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;
import static solid.collectors.ToSolidList.toSolidList;
import static solid.stream.Stream.stream;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.groupon.android.featureadapter.sample.features.inlinevariations.state.TraitClickAction;
import com.groupon.android.featureadapter.sample.features.inlinevariations.state.VariationClickAction;
import com.groupon.android.featureadapter.sample.features.multioptions.state.OptionClickedEvent;
import com.groupon.android.featureadapter.sample.model.Deal;
import com.groupon.android.featureadapter.sample.model.DealApiClient;
import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.android.featureadapter.sample.model.Trait;
import com.groupon.android.featureadapter.sample.model.Variation;
import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.RxFeaturesAdapter;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.events.FeatureEventListener;
import rx.subscriptions.CompositeSubscription;
import solid.collections.SolidList;

public class DealDetailsActivity extends AppCompatActivity {

  public static final String SCOPE = "SCOPE";
  public static final int MAX_THROTTLE = 1000;
  FeatureControllerListCreator recyclerViewController = new FeatureControllerListCreator();
  RxFeaturesAdapter adapter;

  private DealDetailsModel dealDetailsModel;
  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private RecyclerView recycler;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dealDetailsModel = DealDetailsModel.builder().build();

    setContentView(R.layout.activity_with_recycler);

    adapter = new RxFeaturesAdapter(recyclerViewController.getFeatureControllerList());

    recycler = (RecyclerView) findViewById(R.id.recycler_view);
    recycler.setHasFixedSize(true);
    recycler.setLayoutManager(new FlexboxLayoutManager());
    recycler.setAdapter(adapter);
    progressBar = (ProgressBar) findViewById(R.id.progress);

    Button buttonRefresh = (Button) findViewById(R.id.button_refresh);
    buttonRefresh.setOnClickListener(view -> refreshDeal());

    final FeatureEventListener featureEventListener = new GeneralFeatureEventListener();
    for (FeatureController<DealDetailsModel> dealDetailsModelFeatureController :
        recyclerViewController.getFeatureControllerList()) {
      dealDetailsModelFeatureController.addFeatureEventListener(featureEventListener);
    }

    updateAdapter();

    refreshDeal();
  }

  private long random() {
    return (long) (Math.random() * MAX_THROTTLE);
  }

  private void updateAdapter() {
    if (dealDetailsModel.isRefreshing()) {
      progressBar.setVisibility(View.VISIBLE);
      recycler.setVisibility(View.GONE);
    } else {
      progressBar.setVisibility(View.GONE);
      recycler.setVisibility(View.VISIBLE);
      subscriptions.add(adapter.updateFeatureItems(just(dealDetailsModel)).subscribe());
    }
  }

  private void refreshDeal() {
    subscriptions.add(
        DealApiClient.getDeal(random())
            .subscribeOn(io())
            .toObservable()
            .observeOn(mainThread())
            .doOnSubscribe(this::updateProgress)
            .subscribe(this::updateDeal, this::updateErrorMessage));
  }

  private void updateOption(Option option) {
    dealDetailsModel =
        dealDetailsModel.toBuilder().setOption(option).setSummary(option.getTitle()).build();
    updateAdapter();
  }

  private void updateTraitClicked(int traitIndex) {
    Deal deal = dealDetailsModel.getDeal();
    Trait trait = deal.getTraits().get(traitIndex);
    Trait newTrait = trait.withExpanded(!trait.isExpanded());
    dealDetailsModel =
        dealDetailsModel.toBuilder().setDeal(deal.withTraitAt(traitIndex, newTrait)).build();
    updateAdapter();
  }

  private void updateVariationClicked(int traitIndex, int variationIndex) {
    Deal deal = dealDetailsModel.getDeal();
    Trait trait = deal.getTraits().get(traitIndex);
    SolidList<Variation> variations = trait.getVariations();
    Variation targetVariation = variations.get(variationIndex);

    // toggle selection for variation
    Variation selectedVariation = targetVariation.isSelected() ? null : targetVariation;

    // update variation selection states
    variations =
        stream(variations)
            .map(v -> v.withSelected(v.equals(selectedVariation)))
            .collect(toSolidList());

    // update trait view state
    trait = trait.withSelectedVariation(selectedVariation).withVariations(variations);

    //update summary
    final String summary =
        dealDetailsModel.getOption().getTitle()
            + " "
            + targetVariation.getValue()
            + " "
            + trait.getName();

    dealDetailsModel =
        dealDetailsModel
            .toBuilder()
            .setDeal(deal.withTraitAt(traitIndex, trait))
            .setSummary(summary)
            .build();
    updateAdapter();
  }

  private void updateDeal(Deal deal) {
    dealDetailsModel =
        dealDetailsModel
            .toBuilder()
            .setDeal(deal)
            .setRefreshDealError(null)
            .setRefreshing(false)
            .build();
    updateAdapter();
  }

  private void updateErrorMessage(Throwable throwable) {
    dealDetailsModel =
        dealDetailsModel
            .toBuilder()
            .setDeal(null)
            .setRefreshDealError(throwable)
            .setRefreshing(false)
            .build();
    updateAdapter();
  }

  private void updateProgress() {
    dealDetailsModel =
        dealDetailsModel
            .toBuilder()
            .setDeal(null)
            .setRefreshDealError(null)
            .setRefreshing(true)
            .build();
    updateAdapter();
  }

  @Override
  protected void onDestroy() {
    subscriptions.clear();
    super.onDestroy();
  }

  private class GeneralFeatureEventListener implements FeatureEventListener {
    @Override
    public void onFeatureEvent(FeatureEvent featureEvent) {
      if (featureEvent instanceof OptionClickedEvent) {
        final Option optionClicked = ((OptionClickedEvent) featureEvent).optionClicked;
        updateOption(optionClicked);
      } else if (featureEvent instanceof TraitClickAction) {
        final int traitIndex = ((TraitClickAction) featureEvent).traitIndex;
        updateTraitClicked(traitIndex);
      } else if (featureEvent instanceof VariationClickAction) {
        final VariationClickAction variationClickAction = (VariationClickAction) featureEvent;
        final int traitIndex = variationClickAction.traitIndex;
        final int variationIndex = variationClickAction.variationIndex;
        updateVariationClicked(traitIndex, variationIndex);
      }
    }
  }
}
