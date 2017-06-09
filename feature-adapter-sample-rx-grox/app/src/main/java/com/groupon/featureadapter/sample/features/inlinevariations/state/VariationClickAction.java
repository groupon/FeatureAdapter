package com.groupon.featureadapter.sample.features.inlinevariations.state;

import com.groupon.featureadapter.events.FeatureEvent;

public class VariationClickAction implements FeatureEvent {
  public final int traitIndex;
  public final int variationIndex;

  public VariationClickAction(int traitIndex, int variationIndex) {
    this.traitIndex = traitIndex;
    this.variationIndex = variationIndex;
  }
}
