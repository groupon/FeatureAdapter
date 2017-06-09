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

public class HeaderAdapterViewTypeDelegate
    extends AdapterViewTypeDelegate<HeaderAdapterViewTypeDelegate.SummaryViewHolder, HeaderModel> {

  private static final int LAYOUT = R.layout.dd_header;

  public HeaderAdapterViewTypeDelegate() {}

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
  public void unbindViewHolder(SummaryViewHolder holder) {
    // no op
  }

  public class SummaryViewHolder extends RecyclerView.ViewHolder {

    private final TextView valueText;
    private final ImageView headerImage;

    private SummaryViewHolder(View itemView) {
      super(itemView);
      valueText = (TextView) itemView.findViewById(R.id.title_text);
      headerImage = (ImageView) itemView.findViewById(R.id.header_image);
    }
  }
}
