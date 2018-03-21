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
package com.groupon.android.featureadapter.sample.features.options;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.FeatureItemDecoration;

class OptionsItemDecoration implements FeatureItemDecoration {

  private final Drawable divider;
  private final Rect tempBounds = new Rect();

  OptionsItemDecoration(Context context) {
    this.divider = ContextCompat.getDrawable(context, R.drawable.divider);
  }

  @Override
  public void getItemOffsetsImpl(Rect outRect, View view, RecyclerView.ViewHolder holder, RecyclerView parent, RecyclerView.State state) {
    outRect.bottom = divider.getIntrinsicHeight();
  }

  @Override
  public void onDrawViewImpl(Canvas canvas, View view, RecyclerView.ViewHolder holder, RecyclerView parent, RecyclerView.State state) {
    canvas.save();
    parent.getDecoratedBoundsWithMargins(view, tempBounds);
    divider.setBounds(tempBounds.left, tempBounds.bottom - divider.getIntrinsicHeight(), tempBounds.right, tempBounds.bottom);
    divider.draw(canvas);
    canvas.restore();
  }
}
