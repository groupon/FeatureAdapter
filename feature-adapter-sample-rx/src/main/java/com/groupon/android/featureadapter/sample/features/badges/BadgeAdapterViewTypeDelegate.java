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
package com.groupon.android.featureadapter.sample.features.badges;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.DiffUtilComparator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class BadgeAdapterViewTypeDelegate extends AdapterViewTypeDelegate<BadgeAdapterViewTypeDelegate.ViewHolder, BadgeModel> {

  private static final int LAYOUT = R.layout.sample_badge;

  @Override
  public ViewHolder createViewHolder(ViewGroup parent) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false));
  }

  @Override
  public void bindViewHolder(ViewHolder holder, BadgeModel model) {
    holder.badgeText.setText(model.badgeText);
    holder.badgeText.setAllCaps(model.isHighlighted);
    holder.badgeText.setOnClickListener(v -> fireEvent(new OnBadgeTap(model.badgeText)));
  }

  @Override
  public void bindViewHolder(ViewHolder holder, BadgeModel model, List<Object> payloads) {
    if (payloads == null || payloads.isEmpty()) {
      bindViewHolder(holder, model);
      return;
    }
    // only highlighted has been modified
    holder.badgeText.setAllCaps(model.isHighlighted);
  }

  @Override
  public void unbindViewHolder(ViewHolder holder) {
    // no op
  }

  @Override
  public DiffUtilComparator createDiffUtilComparator() {
    return new DiffUtilComparator<BadgeModel>() {

      @Override
      public boolean areItemsTheSame(BadgeModel oldModel, BadgeModel newModel) {
        return oldModel.badgeText.equals(newModel.badgeText);
      }

      @Override
      public boolean areContentsTheSame(BadgeModel oldModel, BadgeModel newModel) {
        return oldModel.isHighlighted == newModel.isHighlighted;
      }

      @Override
      public Object getChangePayload(BadgeModel oldModel, BadgeModel newModel) {
        return "isHighlighted";
      }
    };
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.badge_text) TextView badgeText;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
