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
package com.groupon.featureadapter;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Maintains adapter feature order, item position / ownership
 * WARNING: this class is not thread safe. RxFeatureAdapter guarantees the current state with its
 * behavior.
 *
 * @param <MODEL> the input model of the {@link FeatureController}s and {@link FeaturesAdapter}
 */
class FeatureItems<MODEL> {

  private final List<FeatureController<MODEL>> featureControllers = new ArrayList<>();
  private final Map<FeatureController<MODEL>, List<ViewItem>> mapFeatureControllerToItems = new IdentityHashMap<>();
  private final List<ViewItem> items = new ArrayList<>();

  FeatureItems(List<FeatureController<MODEL>> featureControllers) {
    this.featureControllers.addAll(featureControllers);
    for (FeatureController<MODEL> featureController : this.featureControllers) {
      mapFeatureControllerToItems.put(featureController, emptyList());
    }
  }

  List<FeatureController<MODEL>> getFeatureControllers() {
    return featureControllers;
  }

  ViewItem get(int position) {
    return items.get(position);
  }

  List<ViewItem> getItems(FeatureController<MODEL> featureController) {
    return mapFeatureControllerToItems.get(featureController);
  }

  int size() {
    return items.size();
  }

  Iterator<ViewItem> iterator() {
    return items.iterator();
  }

  int setItemsAndGetOffset(List<ViewItem> newItems, FeatureController<MODEL> featureController) {
    mapFeatureControllerToItems.put(featureController, unmodifiableList(newItems));
    items.clear();
    int offset = 0;
    for (FeatureController<MODEL> controller : featureControllers) {
      if (controller == featureController) {
        offset = items.size();
      }
      items.addAll(mapFeatureControllerToItems.get(controller));
    }
    return offset;
  }
}
