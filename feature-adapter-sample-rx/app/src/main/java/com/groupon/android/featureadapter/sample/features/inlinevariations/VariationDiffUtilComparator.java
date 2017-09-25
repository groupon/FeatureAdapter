package com.groupon.android.featureadapter.sample.features.inlinevariations;

import com.groupon.android.featureadapter.sample.model.Variation;
import com.groupon.featureadapter.DefaultDiffUtilComparator;

class VariationDiffUtilComparator extends DefaultDiffUtilComparator<Variation> {

  @Override
  public boolean areItemsTheSame(Variation oldModel, Variation newModel) {
    return oldModel.getTraitIndex() == newModel.getTraitIndex()
      && oldModel.getIndex() == newModel.getIndex();
  }
}
