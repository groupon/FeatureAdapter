package com.groupon.android.featureadapter.sample.features.inlinevariations;

import com.groupon.android.featureadapter.sample.model.Trait;
import com.groupon.featureadapter.DefaultDiffUtilComparator;

class TraitDiffUtilComparator extends DefaultDiffUtilComparator<Trait> {

  @Override
  public boolean areItemsTheSame(Trait oldModel, Trait newModel) {
    return oldModel.getIndex() == newModel.getIndex();
  }
}
