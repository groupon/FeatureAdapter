package com.groupon.android.featureadapter.sample.features.inlinevariations;

import com.groupon.android.featureadapter.sample.model.Variation;
import com.groupon.featureadapter.DiffUtilComparator;

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
