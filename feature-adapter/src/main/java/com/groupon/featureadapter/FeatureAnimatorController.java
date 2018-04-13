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
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ItemAnimator.ItemHolderInfo;
import android.support.v7.widget.RecyclerView.ViewHolder;
import java.util.Hashtable;

import java.util.Map;

/**
 * Composable class to be attached to a {@link RecyclerView.ItemAnimator} that allows Features
 * to register change animation implementations against the {@link AdapterViewTypeDelegate} types.
 *
 * Refer to {@link FeatureAdapterDefaultAnimator} for an example on how to use this class with an
 * existing {@link RecyclerView.ItemAnimator}
 */
public class FeatureAnimatorController {

  private final Hashtable<Integer, FeatureAnimatorListener> viewTypeFeatureAnimatorMap = new Hashtable<>();
  private final Map<ViewHolder, Animator> viewHolderAnimatorMap = new ArrayMap<>();

  public void registerFeatureAnimatorListener(FeatureAnimatorListener featureAnimator, AdapterViewTypeDelegate viewTypeDelegate) {
    viewTypeFeatureAnimatorMap.put(viewTypeDelegate.getViewType(), featureAnimator);
  }

  @Nullable
  public ItemHolderInfo recordPreLayoutInformation(@NonNull ViewHolder viewHolder) {
    final FeatureAnimatorListener listener = getFeatureAnimatorListener(viewHolder);
    return listener != null ? listener.getPreLayoutInformation(viewHolder) : null;
  }

  @Nullable
  public ItemHolderInfo recordPostLayoutInformation(@NonNull ViewHolder viewHolder) {
    final FeatureAnimatorListener listener = getFeatureAnimatorListener(viewHolder);
    return listener != null ? listener.getPostLayoutInformation(viewHolder) : null;
  }

  public FeatureAnimatorListener getFeatureAnimatorListener(@NonNull ViewHolder viewHolder) {
    return viewTypeFeatureAnimatorMap.get(viewHolder.getItemViewType());
  }

  public boolean animateChange(@NonNull ItemAnimator itemAnimator, @NonNull ViewHolder oldHolder, @NonNull ViewHolder newHolder, @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {
    final FeatureAnimatorListener listener = viewTypeFeatureAnimatorMap.get(oldHolder.getItemViewType());
    if (listener != null) {
      final Animator animator = listener.setupChangeAnimation(itemAnimator, oldHolder, newHolder, preInfo, postInfo);
      if (animator != null) {
        animator.addListener(new CustomAnimationEndListener(itemAnimator, newHolder));
        viewHolderAnimatorMap.put(newHolder, animator);
        return true;
      }
    }
    return false;
  }

  public void runPendingAnimations() {
    for (Animator animator : viewHolderAnimatorMap.values()) {
      animator.start();
    }
  }

  public boolean isRunning() {
    return !viewHolderAnimatorMap.isEmpty();
  }

  public void endAnimation(ViewHolder item) {
    final Animator animator = viewHolderAnimatorMap.get(item);
    if (animator != null) {
      animator.cancel();
    }
  }

  public void endAnimations() {
    for (Animator animator : viewHolderAnimatorMap.values()) {
      if (animator != null) {
        animator.cancel();
      }
    }
  }

  private class CustomAnimationEndListener extends AnimatorListenerAdapter {

    private final ViewHolder holder;
    private final ItemAnimator itemAnimator;

    CustomAnimationEndListener(ItemAnimator itemAnimator, ViewHolder holder) {
      this.holder = holder;
      this.itemAnimator = itemAnimator;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
      viewHolderAnimatorMap.remove(holder);
      itemAnimator.dispatchAnimationFinished(holder);
    }
  }
}
