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
package com.groupon.android.featureadapter.sample.features.header;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.AdapterViewTypeDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

class ImageAdapterViewTypeDelegate extends AdapterViewTypeDelegate<ImageAdapterViewTypeDelegate.ImageViewHolder, String> {

  private static final int LAYOUT = R.layout.sample_header_image;

  @Override
  public ImageViewHolder createViewHolder(ViewGroup viewGroup) {
    return new ImageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(LAYOUT, viewGroup, false));
  }

  @Override
  public void bindViewHolder(ImageViewHolder holder, String s) {
    Glide.with(holder.headerImage.getContext())
      .load(s)
      .into(holder.headerImage);
  }

  @Override
  public void unbindViewHolder(ImageViewHolder holder) {
    Glide.with(holder.headerImage.getContext())
      .clear(holder.headerImage);
  }

  static class ImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.header_image) ImageView headerImage;

    ImageViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
