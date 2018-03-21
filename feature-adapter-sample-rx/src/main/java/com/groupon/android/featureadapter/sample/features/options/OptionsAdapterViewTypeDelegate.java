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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.AdapterViewTypeDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

class OptionsAdapterViewTypeDelegate extends AdapterViewTypeDelegate<OptionsAdapterViewTypeDelegate.OptionsViewHolder, OptionsModel> {

  private static final int LAYOUT = R.layout.sample_option;

  @Override
  public OptionsViewHolder createViewHolder(ViewGroup viewGroup) {
    return new OptionsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(LAYOUT, viewGroup, false));
  }

  @Override
  public void bindViewHolder(OptionsViewHolder holder, OptionsModel optionsModel) {
    holder.titleText.setText(optionsModel.title());
    holder.titleText.setAllCaps(optionsModel.selected());
    holder.priceText.setText(optionsModel.price());
    holder.itemView.setOnClickListener(ignored -> fireEvent(new OnOptionClickEvent(optionsModel.uuid())));
  }

  @Override
  public void unbindViewHolder(OptionsViewHolder holder) {
    // no op
  }

  static class OptionsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.option_title_text) TextView titleText;
    @BindView(R.id.option_price_text) TextView priceText;

    OptionsViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
