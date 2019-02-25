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
import android.widget.LinearLayout;

import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.GroupAdapterViewTypeDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class GroupBadgeAdapterViewTypeDelegate extends GroupAdapterViewTypeDelegate<GroupBadgeAdapterViewTypeDelegate.GroupBadgeViewHolder> {

  private static final int LAYOUT = R.layout.sample_badge_group;

  GroupBadgeAdapterViewTypeDelegate(List<AdapterViewTypeDelegate> delegates) {
    super(delegates);
  }

  @Override
  public GroupBadgeViewHolder createViewHolder(ViewGroup parent) {
    return new GroupBadgeViewHolder(LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false));
  }

  @Override
  protected ViewGroup getRootViewGroup(GroupBadgeViewHolder holder) {
    return holder.linearLayout;
  }

  static class GroupBadgeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.content_layout) LinearLayout linearLayout;

    GroupBadgeViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
