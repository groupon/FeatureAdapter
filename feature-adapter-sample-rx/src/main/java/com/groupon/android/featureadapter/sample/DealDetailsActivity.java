/*
 * Copyright (c) 2017, Groupon, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.groupon.android.featureadapter.sample;

import static com.groupon.android.featureadapter.sample.state.SampleModel.STATE_ERROR;
import static com.groupon.android.featureadapter.sample.state.SampleModel.STATE_LOADING;
import static com.groupon.android.featureadapter.sample.state.SampleModel.STATE_READY;
import static com.groupon.featureadapter.events.RxFeatureEvent.featureEvents;
import static com.groupon.grox.rxjava2.RxStores.states;
import static com.jakewharton.rxbinding3.view.RxView.clicks;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.computation;
import static io.reactivex.schedulers.Schedulers.io;
import static toothpick.Toothpick.closeScope;
import static toothpick.Toothpick.inject;
import static toothpick.Toothpick.openScopes;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import com.groupon.android.featureadapter.sample.events.BaseCommand;
import com.groupon.android.featureadapter.sample.events.RefreshDealCommand;
import com.groupon.android.featureadapter.sample.features.FeatureControllerListCreator;
import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.android.featureadapter.sample.state.DealDetailsScopeSingleton;
import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.android.featureadapter.sample.state.SampleStore;
import com.groupon.featureadapter.FeatureAdapterDefaultAnimator;
import com.groupon.featureadapter.FeatureAdapterItemDecoration;
import com.groupon.featureadapter.FeatureAnimatorController;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.FeatureUpdate;
import com.groupon.featureadapter.RxFeaturesAdapter;
import com.groupon.grox.commands.rxjava2.Command;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;
import toothpick.Scope;
import toothpick.smoothie.module.SmoothieActivityModule;
import toothpick.smoothie.module.SmoothieAndroidXActivityModule;

public class DealDetailsActivity extends AppCompatActivity {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @BindView(R.id.button_refresh)
  Button refreshButton;

  @BindView(R.id.progress)
  ProgressBar progressBar;

  @Inject SampleStore store;
  @Inject FeatureAnimatorController featureAnimatorController;
  @Inject FeatureAdapterItemDecoration featureAdapterItemDecoration;
  @Inject FeatureControllerListCreator featureControllerListCreator;

  private Scope scope;

  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    scope = openScopes(getApplication(), DealDetailsScopeSingleton.class, this);
    scope.installModules(
        new SmoothieActivityModule(this),
        new SmoothieAndroidXActivityModule(this),
        new FeatureAnimatorModule(),
        new FeatureItemDecorationModule());
    inject(this, scope);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_with_recycler);
    ButterKnife.bind(this);

    List<FeatureController<SampleModel>> features =
        featureControllerListCreator.getFeatureControllerList();
    RxFeaturesAdapter<SampleModel> adapter = new RxFeaturesAdapter<>(features);

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new FlexboxLayoutManager(this));
    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(new FeatureAdapterDefaultAnimator(featureAnimatorController));
    recyclerView.addItemDecoration(featureAdapterItemDecoration);

    compositeDisposable.add(clicks(refreshButton).subscribe(v -> refreshDeal(), this::logError));

    refreshButton.setOnClickListener(ignored -> refreshDeal());

    // listen for feature events
    compositeDisposable.add(
        featureEvents(features)
            .observeOn(computation())
            .cast(BaseCommand.class)
            .flatMap(Command::actions)
            .subscribe(store::dispatch, this::logError));

    // propagate states to features
    compositeDisposable.add(
        states(store)
            .subscribeOn(computation())
            .compose(adapter::updateFeatureItems)
            .subscribe(this::logFeatureUpdate, this::logError));

    // listen for new states
    compositeDisposable.add(
        states(store).observeOn(mainThread()).subscribe(this::reactToNewState, this::logError));

    if (store.getState().deal() == null) {
      refreshDeal();
    }
  }

  @Override
  protected void onDestroy() {
    compositeDisposable.dispose();
    super.onDestroy();
    if (isFinishing()) {
      closeScope(DealDetailsScopeSingleton.class);
    }
    closeScope(this);
  }

  private void refreshDeal() {
    compositeDisposable.add(
        new RefreshDealCommand(scope)
            .actions()
            .subscribeOn(io())
            .subscribe(store::dispatch, this::logError));
  }

  private void reactToNewState(SampleModel sampleModel) {
    // update activity state
    switch (sampleModel.state()) {
      case STATE_READY:
        progressBar.setVisibility(View.GONE);
        break;
      case STATE_LOADING:
        progressBar.setVisibility(View.VISIBLE);
        break;
      case STATE_ERROR:
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, sampleModel.exceptionText(), Snackbar.LENGTH_LONG).show();
        break;
    }
  }

  private void logFeatureUpdate(List<FeatureUpdate> featureUpdate) {
    Log.d(getClass().getSimpleName(), featureUpdate.toString());
  }

  private void logError(Throwable t) {
    Log.e(getClass().getSimpleName(), t.getLocalizedMessage(), t);
  }
}
