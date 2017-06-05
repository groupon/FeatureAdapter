package com.groupon.featureadapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

class StubAdapterViewTypeDelegate extends AdapterViewTypeDelegate {
  @Override
  public ViewHolder createViewHolder(ViewGroup parent) {
    return null;
  }

  @Override
  public void bindViewHolder(ViewHolder holder, Object o) {}

  @Override
  public void unbindViewHolder(ViewHolder holder) {}
}
