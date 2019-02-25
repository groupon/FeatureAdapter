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

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Simple ItemDecoration that allows Features to register implementations that only run on their
 * own view types.
 */
public final class FeatureAdapterItemDecoration extends RecyclerView.ItemDecoration {

  private final SparseArray<FeatureItemDecoration> viewTypeDecorationMap = new SparseArray<>();

  public void registerFeatureDecoration(FeatureItemDecoration decoration, AdapterViewTypeDelegate viewTypeDelegate) {
    viewTypeDecorationMap.put(viewTypeDelegate.getViewType(), decoration);
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    final RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
    final int viewType = holder.getItemViewType();
    final FeatureItemDecoration decoration = viewTypeDecorationMap.get(viewType);
    if (decoration != null) {
      decoration.getItemOffsetsImpl(outRect, view, holder, parent, state);
    }
  }

  @Override
  public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    if (parent.getLayoutManager() == null) {
      return;
    }
    for (int i = 0; i < parent.getChildCount(); i++) {
      final View view = parent.getChildAt(i);
      final RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
      final int viewType = holder.getItemViewType();
      final FeatureItemDecoration decoration = viewTypeDecorationMap.get(viewType);
      if (decoration != null) {
        decoration.onDrawViewImpl(canvas, view, holder, parent, state);
      }
    }
  }
}
