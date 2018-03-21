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

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;
import java.util.Map;

/**
 * Callback of DiffUtil to compare items. It uses the {@link FeatureController}s' {@link
 * DiffUtilComparator} to do so.
 */
class DiffUtilCallbackImpl extends DiffUtil.Callback {

  private final Map<Integer, DiffUtilComparator> mapViewTypeToItemComparator;
  private final List<? extends ViewItem> oldList;
  private final List<? extends ViewItem> newList;

  DiffUtilCallbackImpl(
      Map<Integer, DiffUtilComparator> mapViewTypeToItemComparator,
      List<? extends ViewItem> oldList,
      List<? extends ViewItem> newList) {
    this.mapViewTypeToItemComparator = mapViewTypeToItemComparator;
    this.oldList = oldList;
    this.newList = newList;
  }

  @Override
  public int getOldListSize() {
    return oldList.size();
  }

  @Override
  public int getNewListSize() {
    return newList.size();
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    final ViewItem oldItem = oldList.get(oldItemPosition);
    final ViewItem newItem = newList.get(newItemPosition);
    // noinspection unchecked
    return oldItem.viewType == newItem.viewType
        && mapViewTypeToItemComparator
            .get(oldItem.viewType)
            .areItemsTheSame(oldItem.model, newItem.model);
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    final ViewItem oldItem = oldList.get(oldItemPosition);
    final ViewItem newItem = newList.get(newItemPosition);
    // noinspection unchecked
    return mapViewTypeToItemComparator
        .get(oldItem.viewType)
        .areContentsTheSame(oldItem.model, newItem.model);
  }

  @Nullable
  @Override
  public Object getChangePayload(int oldItemPosition, int newItemPosition) {
    final ViewItem oldItem = oldList.get(oldItemPosition);
    final ViewItem newItem = newList.get(newItemPosition);
    // noinspection unchecked
    return mapViewTypeToItemComparator
        .get(oldItem.viewType)
        .getChangePayload(oldItem.model, newItem.model);
  }
}
