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

import com.groupon.android.featureadapter.sample.model.Deal;
import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class HeaderController extends FeatureController<SampleModel> {

  private final ImageAdapterViewTypeDelegate imageDelegate = new ImageAdapterViewTypeDelegate();
  private final TitleAdapterViewTypeDelegate titleDelegate = new TitleAdapterViewTypeDelegate();

  @Override
  public Collection<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return asList(imageDelegate, titleDelegate);
  }

  @Override
  public List<ViewItem> buildItems(SampleModel sampleModel) {
    Deal deal = sampleModel.deal();
    if (deal == null) {
      return emptyList();
    }
    return asList(
      new ViewItem<>(resolveImageUrl(sampleModel.selectedOption(), deal), imageDelegate),
      new ViewItem<>(resolveTitle(sampleModel.selectedOption(), deal), titleDelegate)
    );
  }

  private String resolveImageUrl(Option selectedOption, Deal deal) {
    return selectedOption != null ? selectedOption.imageUrl : deal.imageUrl;
  }

  private String resolveTitle(Option selectedOption, Deal deal) {
    return selectedOption != null ? selectedOption.title : deal.title;
  }
}
