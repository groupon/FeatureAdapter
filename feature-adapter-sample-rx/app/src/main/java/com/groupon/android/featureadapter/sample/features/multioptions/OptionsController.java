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
package com.groupon.android.featureadapter.sample.features.multioptions;

import com.groupon.android.featureadapter.sample.DealDetailsModel;
import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionsController extends FeatureController<DealDetailsModel> {

  private final OptionAdapterViewTypeDelegate optionAdapterViewTypeDelegate =
      new OptionAdapterViewTypeDelegate();
  private List<Option> oldOptions;

  @Override
  public List<ViewItem> buildItems(DealDetailsModel model) {
    //check if the list of items needs to be updated
    if (model.getDeal() == null || model.getDeal().getOptions().equals(oldOptions)) {
      return null;
    }

    oldOptions = model.getDeal().getOptions();
    List<ViewItem> items = new ArrayList<>(2);
    for (Option option : model.getDeal().getOptions()) {
      items.add(new ViewItem<>(option, optionAdapterViewTypeDelegate));
    }
    return items;
  }

  @Override
  public List<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return Arrays.asList(optionAdapterViewTypeDelegate);
  }
}
