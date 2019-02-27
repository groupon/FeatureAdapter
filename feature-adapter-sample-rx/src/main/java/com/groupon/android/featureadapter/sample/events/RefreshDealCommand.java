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
package com.groupon.android.featureadapter.sample.events;

import com.groupon.android.featureadapter.sample.model.Deal;
import com.groupon.android.featureadapter.sample.model.DealApiClient;
import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.grox.Action;

import com.groupon.grox.commands.rxjava2.Command;
import io.reactivex.Observable;
import javax.inject.Inject;

import toothpick.Scope;

import static com.groupon.android.featureadapter.sample.state.SampleModel.STATE_ERROR;
import static com.groupon.android.featureadapter.sample.state.SampleModel.STATE_LOADING;
import static com.groupon.android.featureadapter.sample.state.SampleModel.STATE_READY;
import static toothpick.Toothpick.inject;

/**
 * Encapsulate the state update logic to refresh a deal from the Api.
 * This class implements {@link FeatureEvent} so that it can be launched from features.
 * It also implements {@link Command} so that it can be processed in a Grox chain.
 */
public class RefreshDealCommand implements BaseCommand {

  @Inject DealApiClient dealApiClient;

  public RefreshDealCommand(Scope scope) {
    inject(this, scope);
  }

  @Override
  public Observable<? extends Action<SampleModel>> actions() {
    return dealApiClient.getDeal()
      .map(SuccessAction::new)
      .map(action -> (Action<SampleModel>) action)
      .onErrorReturn(FailedAction::new)
      .toObservable()
      .startWith(new StateLoadingAction());
  }

  /**
   * The deal is fetched successfully.
   */
  private static class SuccessAction implements Action<SampleModel> {

    private final Deal deal;

    SuccessAction(Deal deal) {
      this.deal = deal;
    }

    @Override
    public SampleModel newState(SampleModel oldState) {
      return oldState.toBuilder()
        .setDeal(deal)
        .setSelectedOption(updateOption(deal, oldState.selectedOption()))
        .setState(STATE_READY)
        .setExceptionText(null)
        .build();
    }

    private static Option updateOption(Deal deal, Option previousOption) {
      if (previousOption == null) return null;
      for (Option option : deal.options) {
        if (previousOption.uuid.equals(option.uuid)) return option;
      }
      throw new IllegalArgumentException("Option does not exist");
    }
  }

  /**
   * There is an exception while fetching the deal.
   */
  private static class FailedAction implements Action<SampleModel> {

    private final Throwable throwable;

    FailedAction(Throwable throwable) {
      this.throwable = throwable;
    }

    @Override
    public SampleModel newState(SampleModel oldState) {
      return oldState.toBuilder()
        .setState(STATE_ERROR)
        .setExceptionText(throwable.getLocalizedMessage())
        .build();
    }
  }

  /**
   * Update the progress state when fetching the deal.
   */
  private static class StateLoadingAction implements Action<SampleModel> {

    @Override
    public SampleModel newState(SampleModel oldState) {
      return oldState.toBuilder()
        .setState(STATE_LOADING)
        .build();
    }
  }
}
