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

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.DiffUtilComparator;

import java.util.List;

class CollapsibleParentAdapterViewTypeDelegate extends AdapterViewTypeDelegate<CollapsibleParentViewHolder, CollapsibleParentModel> {

  static final float CARET_ROTATION_EXPANDED = 0f;
  static final float CARET_ROTATION_COLLAPSED = 180f;

  private static final int LAYOUT = R.layout.sample_collapsible_parent;

  @Override
  public CollapsibleParentViewHolder createViewHolder(ViewGroup parent) {
    return new CollapsibleParentViewHolder(LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false));
  }

  @Override
  public void bindViewHolder(CollapsibleParentViewHolder holder, CollapsibleParentModel model) {
    holder.titleText.setText(model.title);
    holder.caretImage.setRotation(model.isCollapsed ? CARET_ROTATION_COLLAPSED : CARET_ROTATION_EXPANDED);
    holder.itemView.setOnClickListener(v -> fireEvent(new OnCollapsibleParentTap()));
    holder.model = model;
  }

  @Override
  public void bindViewHolder(CollapsibleParentViewHolder holder, CollapsibleParentModel model, List<Object> payloads) {
    if (payloads == null || payloads.isEmpty()) {
      bindViewHolder(holder, model);
    }
    // This point will be reached if the DiffUtilComparator returns a payload for the collapsed
    // value updating. Caret rotation will be handled by the animator
    holder.model = model;
  }

  @Override
  public void unbindViewHolder(CollapsibleParentViewHolder holder) {
    // no op
  }

  @Override
  public DiffUtilComparator createDiffUtilComparator() {
    return new CollapsibleParentDiffUtilComparator();
  }
}
