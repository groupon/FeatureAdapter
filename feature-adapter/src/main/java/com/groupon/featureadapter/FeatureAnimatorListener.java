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

import android.animation.Animator;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A Feature's implementation of a custom animator that only runs for its own view type.
 *
 * @param <VH> The {@link AdapterViewTypeDelegate} ViewHolder.
 * @param <M>  A {@link RecyclerView.ItemAnimator.ItemHolderInfo} model to hold animation metadata.
 */
public interface FeatureAnimatorListener<VH extends RecyclerView.ViewHolder, M extends RecyclerView.ItemAnimator.ItemHolderInfo> {
  M getPreLayoutInformation(VH viewHolder);

  M getPostLayoutInformation(VH viewHolder);

  Animator setupChangeAnimation(RecyclerView.ItemAnimator itemAnimator, VH oldHolder, VH newHolder, M preInfo, M postInfo);
}
