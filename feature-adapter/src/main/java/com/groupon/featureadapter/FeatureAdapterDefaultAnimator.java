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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Default implementation that takes and utilizes a {@link FeatureAnimatorController} to allow
 * Features to register and run their own Animations with a similar pattern to
 * {@link RecyclerView.ItemAnimator}.
 */
public final class FeatureAdapterDefaultAnimator extends DefaultItemAnimator {

  private final FeatureAnimatorController featureAnimatorController;

  public FeatureAdapterDefaultAnimator(FeatureAnimatorController featureAnimatorController) {
    this.featureAnimatorController = featureAnimatorController;
  }

  @Override
  public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
    return true;
  }

  @NonNull
  @Override
  public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder, int changeFlags, @NonNull List<Object> payloads) {
    final ItemHolderInfo info = featureAnimatorController.recordPreLayoutInformation(viewHolder);
    return info != null
      ? info
      : super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
  }

  @NonNull
  @Override
  public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder) {
    final ItemHolderInfo info = featureAnimatorController.recordPostLayoutInformation(viewHolder);
    return info != null
      ? info
      : super.recordPostLayoutInformation(state, viewHolder);
  }

  @Override
  public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {
    return featureAnimatorController.animateChange(this, oldHolder, newHolder, preInfo, postInfo) ||
      super.animateChange(oldHolder, newHolder, preInfo, postInfo);
  }

  @Override
  public void runPendingAnimations() {
    super.runPendingAnimations();
    featureAnimatorController.runPendingAnimations();
  }

  @Override
  public boolean isRunning() {
    return super.isRunning() || featureAnimatorController.isRunning();
  }

  @Override
  public void endAnimation(RecyclerView.ViewHolder item) {
    super.endAnimation(item);
    featureAnimatorController.endAnimation(item);
  }

  @Override
  public void endAnimations() {
    super.endAnimations();
    featureAnimatorController.endAnimations();
  }
}
