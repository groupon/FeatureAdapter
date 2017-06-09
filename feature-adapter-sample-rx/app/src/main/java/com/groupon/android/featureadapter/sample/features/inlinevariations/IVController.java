package com.groupon.android.featureadapter.sample.features.inlinevariations;

import com.groupon.android.featureadapter.sample.DealDetailsModel;
import com.groupon.android.featureadapter.sample.model.Trait;
import com.groupon.android.featureadapter.sample.model.Variation;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IVController extends FeatureController<DealDetailsModel> {

  private TraitAdapterViewTypeDelegate traitAdapterViewTypeDelegate =
      new TraitAdapterViewTypeDelegate();
  private VariationAdapterViewTypeDelegate variationAdapterViewTypeDelegate =
      new VariationAdapterViewTypeDelegate();

  public List<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return Arrays.asList(traitAdapterViewTypeDelegate, variationAdapterViewTypeDelegate);
  }

  @Override
  public List<ViewItem> buildItems(DealDetailsModel dealDetailsModel) {
    if (dealDetailsModel.getDeal() == null) {
      return null;
    }
    List<ViewItem> items = new ArrayList<>();
    for (Trait trait : dealDetailsModel.getDeal().getTraits()) {
      items.add(new ViewItem<>(trait, traitAdapterViewTypeDelegate));

      if (trait.isExpanded()) {
        for (Variation variation : trait.getVariations()) {
          items.add(new ViewItem<>(variation, variationAdapterViewTypeDelegate));
        }
      }
    }
    return items;
  }
}
