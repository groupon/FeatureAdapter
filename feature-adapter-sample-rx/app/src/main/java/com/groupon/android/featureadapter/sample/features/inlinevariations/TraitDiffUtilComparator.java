package com.groupon.android.featureadapter.sample.features.inlinevariations;

import com.groupon.android.featureadapter.sample.model.Trait;
import com.groupon.featureadapter.DiffUtilComparator;

class TraitDiffUtilComparator implements DiffUtilComparator<Trait> {

  @Override
  public boolean areItemsTheSame(Trait oldModel, Trait newModel) {
    return oldModel.getIndex() == newModel.getIndex();
  }

  @Override
  public boolean areContentsTheSame(Trait oldModel, Trait newModel) {
    return oldModel.equals(newModel);
  }
}
