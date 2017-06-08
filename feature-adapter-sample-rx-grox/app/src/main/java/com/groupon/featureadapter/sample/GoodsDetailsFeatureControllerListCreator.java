package com.groupon.featureadapter.sample;

import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.sample.features.header.HeaderController;
import com.groupon.featureadapter.sample.features.inlinevariations.IVController;
import com.groupon.featureadapter.sample.features.multioptions.OptionsController;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class GoodsDetailsFeatureControllerListCreator {

  @Inject IVController ivController;
  private List<FeatureController<DealDetailsModel>> group;

  @Inject
  public GoodsDetailsFeatureControllerListCreator() {}

  @Inject
  @SuppressWarnings("unused")
  void init() {
    group = Arrays.asList(new HeaderController(), new OptionsController(), ivController);
  }

  public List<FeatureController<DealDetailsModel>> getFeatureControllerList() {
    return group;
  }
}
