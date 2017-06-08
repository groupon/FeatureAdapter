package com.groupon.featureadapter.sample.features.multioptions.state;

import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.featureadapter.sample.model.Option;
import com.groupon.grox.commands.rxjava1.SingleActionCommand;

public class OptionClickedAction extends SingleActionCommand<DealDetailsModel>
    implements FeatureEvent {

  private Option optionClicked;

  public OptionClickedAction(Option optionClicked) {
    this.optionClicked = optionClicked;
  }

  @Override
  public DealDetailsModel reduce(DealDetailsModel oldState) {
    return oldState
        .toBuilder()
        .setOption(optionClicked)
        .setSummary(optionClicked.getTitle())
        .build();
  }
}
