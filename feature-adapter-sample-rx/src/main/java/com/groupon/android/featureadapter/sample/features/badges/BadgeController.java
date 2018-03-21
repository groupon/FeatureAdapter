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
package com.groupon.android.featureadapter.sample.features.badges;

import com.groupon.android.featureadapter.sample.model.Deal;
import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class BadgeController extends FeatureController<SampleModel> {

  private final BadgeAdapterViewTypeDelegate badgeDelegate = new BadgeAdapterViewTypeDelegate();
  private final BadgeAdapterViewTypeDelegate childBadgeDelegate = new BadgeAdapterViewTypeDelegate();
  private final GroupBadgeAdapterViewTypeDelegate groupBadgeDelegate = new GroupBadgeAdapterViewTypeDelegate(singletonList(childBadgeDelegate));

  @Override
  public Collection<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return asList(badgeDelegate, groupBadgeDelegate);
  }

  @Override
  public List<ViewItem> buildItems(SampleModel sampleModel) {
    Deal deal = sampleModel.deal();
    if (deal == null) {
      return emptyList();
    }
    List<ViewItem> items = new ArrayList<>();

    /*
    The first group of badges utilizes the {@link com.google.android.flexbox.FlexboxLayoutManager}
    to layout a wrapping list of badges in line with the rest of the Activity.
    */
    for (String badge : deal.badges) {
      items.add(new ViewItem<>(new BadgeModel(badge, badge.equals(sampleModel.highlightedBadge())), badgeDelegate));
    }

    /*
    The second group of badges utilizes the {@link GroupAdapterViewTypeDelegate} to nest a group
    of badges inside a custom layout. Note that it uses the same {@link BadgeAdapterViewTypeDelegate}
    class/ Note the same DiffUtilComparator and fireEvent still work for the child view items.
    */
    List<ViewItem> childItems = new ArrayList<>();
    for (String badge : deal.badges) {
      childItems.add(new ViewItem<>(new BadgeModel(badge, badge.equals(sampleModel.highlightedBadge())), childBadgeDelegate));
    }
    items.add(new ViewItem<>(childItems, groupBadgeDelegate));

    return items;
  }
}
