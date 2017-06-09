package com.groupon.featureadapter.sample;

import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.sample.features.header.HeaderController;
import com.groupon.featureadapter.sample.features.inlinevariations.IVController;
import com.groupon.featureadapter.sample.features.multioptions.OptionsController;
import java.util.Arrays;
import java.util.List;

public class FeatureControllerListCreator {

  private List<FeatureController<DealDetailsModel>> group;

  public FeatureControllerListCreator() {
    group = Arrays.asList(new HeaderController(), new OptionsController(), new IVController());
  }

  public List<FeatureController<DealDetailsModel>> getFeatureControllerList() {
    return group;
  }
}
