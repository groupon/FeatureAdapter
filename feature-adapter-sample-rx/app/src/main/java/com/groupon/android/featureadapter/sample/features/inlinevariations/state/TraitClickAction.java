package com.groupon.android.featureadapter.sample.features.inlinevariations.state;

import com.groupon.featureadapter.events.FeatureEvent;

public class TraitClickAction implements FeatureEvent {
  public final int traitIndex;

  public TraitClickAction(int traitIndex) {
    this.traitIndex = traitIndex;
  }
}
