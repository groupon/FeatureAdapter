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
package com.groupon.android.featureadapter.sample.features.inlinevariations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.groupon.android.featureadapter.sample.features.inlinevariations.state.TraitClickAction;
import com.groupon.android.featureadapter.sample.model.Trait;
import com.groupon.android.featureadapter.sample.model.Variation;
import com.groupon.android.featureadapter.sample.rx.R;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.DiffUtilComparator;

public class TraitAdapterViewTypeDelegate
    extends AdapterViewTypeDelegate<TraitAdapterViewTypeDelegate.TraitViewHolder, Trait> {

  private static final int LAYOUT = R.layout.iv_trait;

  public TraitAdapterViewTypeDelegate() {}

  @Override
  public TraitViewHolder createViewHolder(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false);
    return new TraitViewHolder(view);
  }

  @Override
  public void bindViewHolder(TraitViewHolder holder, Trait model) {
    holder.nameText.setText(
        String.format(
            "%1$s: %2$s (%3$s)",
            model.getName(),
            getSelectedText(model.getSelectedVariation()),
            model.isExpanded() ? "Expanded" : "Collapsed"));
    holder.itemView.setOnClickListener(v -> fireEvent(new TraitClickAction(model.getIndex())));
  }

  @Override
  public void unbindViewHolder(TraitViewHolder holder) {
    // no op
  }

  @Override
  public DiffUtilComparator createDiffUtilComparator() {
    return new TraitDiffUtilComparator();
  }

  private String getSelectedText(Variation selectedVariation) {
    return selectedVariation != null ? selectedVariation.getValue() : "Please Select";
  }

  static class TraitViewHolder extends RecyclerView.ViewHolder {

    final TextView nameText;

    TraitViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.trait_name_text);
    }
  }
}
