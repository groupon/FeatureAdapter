package com.groupon.featureadapter.sample.features.inlinevariations;

import com.groupon.featureadapter.DiffUtilComparator;
import com.groupon.featureadapter.sample.model.Variation;

class VariationDiffUtilComparator implements DiffUtilComparator<Variation> {

  @Override
  public boolean areItemsTheSame(Variation oldModel, Variation newModel) {
    return oldModel.getTraitIndex() == newModel.getTraitIndex()
        && oldModel.getIndex() == newModel.getIndex();
  }

  @Override
  public boolean areContentsTheSame(Variation oldModel, Variation newModel) {
    return oldModel.equals(newModel);
  }
}
