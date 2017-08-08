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

/**
 * Compares item of a given model class. Classes implementing this interface will be used internally
 * with {@link android.support.v7.util.DiffUtil.Callback} to compare items in the recycler view.
 * When an adapter updates the list of items for a given feature controller, items of the old list
 * and of the new list will be compared. Depending on the result of this comparison, items will be
 * animated in the underlying recycler view in different ways (additions, deletions, modifications).
 *
 * @param <MODEL> the class of the items to compare.
 * @see android.support.v7.util.DiffUtil.Callback
 * @see FeaturesAdapter#updateFeatureItems(FeatureUpdate)
 */
public interface DiffUtilComparator<MODEL> {

  /** @see android.support.v7.util.DiffUtil.Callback#areItemsTheSame(int, int) */
  boolean areItemsTheSame(MODEL oldModel, MODEL newModel);

  /** @see android.support.v7.util.DiffUtil.Callback#areContentsTheSame(int, int) */
  boolean areContentsTheSame(MODEL oldModel, MODEL newModel);
}
