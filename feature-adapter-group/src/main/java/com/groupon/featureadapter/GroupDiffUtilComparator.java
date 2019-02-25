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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

class GroupDiffUtilComparator implements DiffUtilComparator<List<ViewItem>> {

  private final List<DiffUtilComparator> childDiffUtilComparators;

  GroupDiffUtilComparator(List<DiffUtilComparator> childDiffUtilComparators) {
    this.childDiffUtilComparators = childDiffUtilComparators;
  }

  @Override
  public boolean areItemsTheSame(List<ViewItem> oldModel, List<ViewItem> newModel) {
    return true; // assume position doesn't move
  }

  @Override
  public boolean areContentsTheSame(List<ViewItem> oldItems, List<ViewItem> newItems) {
    if (oldItems.size() != newItems.size()) {
      return false;
    }
    for (int i = 0; i < oldItems.size(); i++) {
      final ViewItem oldItem = oldItems.get(i);
      final ViewItem newItem = newItems.get(i);
      if (oldItem.viewType != newItem.viewType) {
        return false;
      }
      final DiffUtilComparator comparator = childDiffUtilComparators.get(oldItem.viewType);
      if (!comparator.areItemsTheSame(oldItem.model, newItem.model)) {
        return false;
      }
      if (!comparator.areContentsTheSame(oldItem.model, newItem.model)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Object getChangePayload(List<ViewItem> oldItems, List<ViewItem> newItems) {
    final DiffUtilCallback callback = new DiffUtilCallback(oldItems, newItems);
    return DiffUtil.calculateDiff(callback, false);
  }

  private class DiffUtilCallback extends DiffUtil.Callback {

    private final List<ViewItem> oldItems;
    private final List<ViewItem> newItems;

    DiffUtilCallback(List<ViewItem> oldItems, List<ViewItem> newItems) {
      this.oldItems = oldItems;
      this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
      return oldItems.size();
    }

    @Override
    public int getNewListSize() {
      return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
      final ViewItem oldItem = oldItems.get(oldItemPosition);
      final ViewItem newItem = newItems.get(newItemPosition);
      return oldItem.viewType == newItem.viewType &&
        childDiffUtilComparators.get(oldItem.viewType).areItemsTheSame(oldItem.model, newItem.model);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
      final ViewItem oldItem = oldItems.get(oldItemPosition);
      final ViewItem newItem = newItems.get(newItemPosition);
      return childDiffUtilComparators.get(oldItem.viewType).areContentsTheSame(oldItem.model, newItem.model);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
      final ViewItem oldItem = oldItems.get(oldItemPosition);
      final ViewItem newItem = newItems.get(newItemPosition);
      return childDiffUtilComparators.get(oldItem.viewType).getChangePayload(oldItem.model, newItem.model);
    }
  }
}
