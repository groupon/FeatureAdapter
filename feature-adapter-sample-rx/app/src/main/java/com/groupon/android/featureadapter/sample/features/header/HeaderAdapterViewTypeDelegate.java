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
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.DiffUtilComparator;
import java.util.List;

class HeaderAdapterViewTypeDelegate
    extends AdapterViewTypeDelegate<HeaderAdapterViewTypeDelegate.SummaryViewHolder, HeaderModel> {

  private static final int LAYOUT = R.layout.dd_header;

  HeaderAdapterViewTypeDelegate() {}

  @Override
  public SummaryViewHolder createViewHolder(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false);
    return new SummaryViewHolder(view);
  }

  @Override
  public void bindViewHolder(SummaryViewHolder holder, HeaderModel model) {
    holder.valueText.setText(model.title);
    Glide.with(holder.headerImage.getContext()).load(model.imageUrl).into(holder.headerImage);
  }

  @Override
  public void bindViewHolder(
      SummaryViewHolder holder, HeaderModel headerModel, List<Object> payloads) {
    if (payloads.isEmpty()) {
      bindViewHolder(holder, headerModel);
    } else {
      holder.valueText.setText(payloads.get(0).toString());
    }
  }

  @Override
  public void unbindViewHolder(SummaryViewHolder holder) {
    // no op
  }

  @Override
  public DiffUtilComparator createDiffUtilComparator() {
    return new HeaderDiffUtilComparator();
  }

  static class SummaryViewHolder extends RecyclerView.ViewHolder {

    private final TextView valueText;
    private final ImageView headerImage;

    private SummaryViewHolder(View itemView) {
      super(itemView);
      valueText = (TextView) itemView.findViewById(R.id.title_text);
      headerImage = (ImageView) itemView.findViewById(R.id.header_image);
    }
  }
}
