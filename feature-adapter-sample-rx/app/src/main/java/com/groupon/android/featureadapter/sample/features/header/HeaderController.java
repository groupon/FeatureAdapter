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
package com.groupon.android.featureadapter.sample.features.header;

import static java.util.Collections.singletonList;

import com.groupon.android.featureadapter.sample.DealDetailsModel;
import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;
import java.util.List;
import java.util.Objects;

public class HeaderController extends FeatureController<DealDetailsModel> {

  private final HeaderAdapterViewTypeDelegate headerAdapterViewTypeDelegate =
      new HeaderAdapterViewTypeDelegate();

  private Option oldOption = null;
  private String oldTitle = null;

  @Override
  public List<ViewItem> buildItems(DealDetailsModel model) {
    if (model.getSummary() == null) {
      return null;
    }

    if (hasChangedSinceLastUpdate(model)) {
      return null;
    } else {
      memorizeUpdate(model);
    }

    HeaderModel summaryModel = new HeaderModel(model.getSummary(), model.getOption().getImageUrl());
    return singletonList(new ViewItem<>(summaryModel, headerAdapterViewTypeDelegate));
  }

  private void memorizeUpdate(DealDetailsModel model) {
    oldOption = model.getOption();
    oldTitle = model.getSummary();
  }

  private boolean hasChangedSinceLastUpdate(DealDetailsModel model) {
    return model.getOption() == oldOption && Objects.equals(model.getSummary(), oldTitle);
  }

  @Override
  public List<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return singletonList(headerAdapterViewTypeDelegate);
  }
}
