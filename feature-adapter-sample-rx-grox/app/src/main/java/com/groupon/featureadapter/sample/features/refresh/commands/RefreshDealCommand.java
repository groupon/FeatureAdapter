package com.groupon.featureadapter.sample.features.refresh.commands;

import static rx.Observable.fromCallable;
import static rx.schedulers.Schedulers.io;

import com.groupon.featureadapter.sample.features.refresh.state.RefreshDealFailureAction;
import com.groupon.featureadapter.sample.features.refresh.state.RefreshDealStartedAction;
import com.groupon.featureadapter.sample.features.refresh.state.RefreshDealSuccessAction;
import com.groupon.featureadapter.sample.model.DealApiClient;
import com.groupon.grox.Action;
import com.groupon.grox.commands.rxjava1.Command;
import rx.Observable;

public class RefreshDealCommand implements Command {

  private final long throttleInMs;

  public RefreshDealCommand(long throttleInMs) {
    this.throttleInMs = throttleInMs;
  }

  @Override
  public Observable<Action> actions() {
    return DealApiClient.getDeal(throttleInMs)
        .subscribeOn(io())
        .toObservable()
        .map(RefreshDealSuccessAction::new)
        .cast(Action.class)
        .onErrorReturn(RefreshDealFailureAction::new)
        .startWith(fromCallable(RefreshDealStartedAction::new));
  }
}
