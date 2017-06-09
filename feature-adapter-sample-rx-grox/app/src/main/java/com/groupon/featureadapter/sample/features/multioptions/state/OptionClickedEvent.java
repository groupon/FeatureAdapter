package com.groupon.featureadapter.sample.features.multioptions.state;

import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.sample.model.Option;

public class OptionClickedEvent implements FeatureEvent {

  public final Option optionClicked;

  public OptionClickedEvent(Option optionClicked) {
    this.optionClicked = optionClicked;
  }
}
