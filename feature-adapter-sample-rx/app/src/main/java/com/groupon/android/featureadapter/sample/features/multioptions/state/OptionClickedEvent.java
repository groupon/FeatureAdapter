package com.groupon.android.featureadapter.sample.features.multioptions.state;

import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.featureadapter.events.FeatureEvent;

public class OptionClickedEvent implements FeatureEvent {

  public final Option optionClicked;

  public OptionClickedEvent(Option optionClicked) {
    this.optionClicked = optionClicked;
  }
}
