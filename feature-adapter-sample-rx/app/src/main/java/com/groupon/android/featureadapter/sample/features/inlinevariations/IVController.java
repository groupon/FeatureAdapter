/*
 * Copyright (c) 2017, Groupon, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
