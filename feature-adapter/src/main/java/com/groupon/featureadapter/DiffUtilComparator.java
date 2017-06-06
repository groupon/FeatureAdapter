/**
 * Copyright (c) 2017, Groupon, Inc. All rights reserved.
 *
 * <p>Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * <p>Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * <p>Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * <p>Neither the name of GROUPON nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
