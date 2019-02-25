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
package com.groupon.android.featureadapter.sample.features.collapsible;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.groupon.featureadapter.FeatureAnimatorListener;

import static android.view.View.ROTATION;
import static com.groupon.android.featureadapter.sample.features.collapsible.CollapsibleParentAdapterViewTypeDelegate.CARET_ROTATION_COLLAPSED;
import static com.groupon.android.featureadapter.sample.features.collapsible.CollapsibleParentAdapterViewTypeDelegate.CARET_ROTATION_EXPANDED;

class CollapsibleParentAnimatorListener implements FeatureAnimatorListener<CollapsibleParentViewHolder, CollapsibleParentItemInfo> {

  @Override
  public CollapsibleParentItemInfo getPreLayoutInformation(CollapsibleParentViewHolder viewHolder) {
    return new CollapsibleParentItemInfo(viewHolder);
  }

  @Override
  public CollapsibleParentItemInfo getPostLayoutInformation(CollapsibleParentViewHolder viewHolder) {
    return new CollapsibleParentItemInfo(viewHolder);
  }

  @Override
  public Animator setupChangeAnimation(RecyclerView.ItemAnimator itemAnimator, CollapsibleParentViewHolder oldHolder, CollapsibleParentViewHolder newHolder, CollapsibleParentItemInfo preInfo, CollapsibleParentItemInfo postInfo) {
    if (preInfo.isCollapsed == postInfo.isCollapsed) {
      return null;
    }
    final float rotationFrom = preInfo.isCollapsed ? CARET_ROTATION_COLLAPSED : CARET_ROTATION_EXPANDED;
    final float rotationTo = !preInfo.isCollapsed ? CARET_ROTATION_COLLAPSED : CARET_ROTATION_EXPANDED;
    final ObjectAnimator animation = ObjectAnimator.ofFloat(newHolder.caretImage, ROTATION.getName(), rotationFrom, rotationTo);
    animation.addListener(new OnAnimationFinishListener(newHolder.caretImage, rotationTo));
    return animation;
  }

  private static class OnAnimationFinishListener extends AnimatorListenerAdapter {

    private boolean isCancelled;

    private final ImageView caretImage;
    private final float rotationTo;

    OnAnimationFinishListener(ImageView caretImage, float rotationTo) {
      this.caretImage = caretImage;
      this.rotationTo = rotationTo;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
      isCancelled = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
      if (!isCancelled) {
        caretImage.setRotation(rotationTo);
      }
    }
  }
}
