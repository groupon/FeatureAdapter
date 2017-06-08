package com.groupon.featureadapter.sample.features.inlinevariations;

import com.groupon.featureadapter.DiffUtilComparator;
import com.groupon.featureadapter.sample.model.Trait;

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
