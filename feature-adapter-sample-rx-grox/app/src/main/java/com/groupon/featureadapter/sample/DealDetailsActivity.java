package com.groupon.featureadapter.sample;

import static com.groupon.featureadapter.events.RxFeatureEvent.featureEvents;
import static com.groupon.grox.RxStores.states;
import static com.jakewharton.rxbinding.view.RxView.clicks;
import static rx.Observable.just;
import static rx.schedulers.Schedulers.computation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.groupon.featureadapter.RxFeaturesAdapter;
import com.groupon.featureadapter.sample.features.refresh.commands.RefreshDealCommand;
import com.groupon.featureadapter.sample.state.DealStore;
import com.groupon.featureadapter.samplerxgrox.R;
import com.groupon.grox.commands.rxjava1.Command;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class DealDetailsActivity extends AppCompatActivity {

  public static final String SCOPE = "SCOPE";
  public static final int MAX_THROTTLE = 1000;
  @Inject GoodsDetailsFeatureControllerListCreator recyclerViewController;
  RxFeaturesAdapter adapter;

  private DealStore store;
  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private Scope scope;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final DealDetailsModel initialState = DealDetailsModel.builder().build();
    store = new DealStore(initialState);

    scope = Toothpick.openScope(this);
    scope.installModules(
        new Module() {
          {
            bind(DealStore.class).toInstance(store);
          }
        });
    Toothpick.inject(this, scope);
    setContentView(R.layout.activity_with_recycler);

    adapter = new RxFeaturesAdapter(recyclerViewController.getFeatureControllerList());

    RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_view);
    recycler.setHasFixedSize(true);
    recycler.setLayoutManager(new FlexboxLayoutManager());
    recycler.setAdapter(adapter);

    Button buttonRefresh = (Button) findViewById(R.id.button_refresh);
    // we could have called this below
    //manageCommands(clicks(buttonRefresh)
    //                     .map(ignored -> new RefreshDealCommand()));
    //but this cancels the request as the UI dies
    //we can do this to let the request live independently of the UI
    subscriptions.add(
        clicks(buttonRefresh)
            .map((Func1<Void, Command>) aVoid -> new RefreshDealCommand(random()))
            .subscribe(command -> command.actions().subscribe(store::dispatch)));

    //same as
    //final Observable<Action> publish = new RefreshDealCommand().actions().share();
    //subscriptions.addFeatureCommandListener(clicks(buttonRefresh)
    //        .flatMap(ignored -> publish)
    //        .subscribe(store::dispatch));
    //publish.subscribe(store::dispatch);

    manageCommands(
        featureEvents(recyclerViewController.getFeatureControllerList()).cast(Command.class));

    updateAdapterOnStateChange();

    refreshDeal();
  }

  private long random() {
    return (long) (Math.random() * MAX_THROTTLE);
  }

  private void updateAdapterOnStateChange() {
    subscriptions.add(
        states(store).subscribeOn(computation()).compose(adapter::updateFeatureItems).subscribe());
  }

  @Override
  public Object getSystemService(String name) {
    if (name.equals(SCOPE)) {
      return scope;
    }
    return super.getSystemService(name);
  }

  private void manageCommands(Observable<Command> commandObservable) {
    subscriptions.add(
        commandObservable
            .observeOn(computation())
            .flatMap(Command::actions)
            .subscribe(store::dispatch));
  }

  private void refreshDeal() {
    manageCommands(just(new RefreshDealCommand(0)));
  }

  @Override
  protected void onDestroy() {
    subscriptions.clear();
    Toothpick.closeScope(this);
    super.onDestroy();
  }
}
