package com.groupon.featureadapter.sample.features.inlinevariations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.DiffUtilComparator;
import com.groupon.featureadapter.sample.features.inlinevariations.state.VariationClickAction;
import com.groupon.featureadapter.sample.model.Variation;
import com.groupon.featureadapter.samplerxgrox.R;

public class VariationAdapterViewTypeDelegate
    extends AdapterViewTypeDelegate<
        VariationAdapterViewTypeDelegate.VariationViewHolder, Variation> {

  private static final int LAYOUT = R.layout.iv_variation;

  public VariationAdapterViewTypeDelegate() {}

  @Override
  public VariationViewHolder createViewHolder(ViewGroup parent) {
    View view = LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false);
    return new VariationViewHolder(view);
  }

  @Override
  public void bindViewHolder(VariationViewHolder holder, Variation model) {
    holder.valueText.setText(model.getValue());
    holder.valueText.setSelected(model.isSelected());
    holder.itemView.setOnClickListener(
        v -> fireEvent(new VariationClickAction(model.getTraitIndex(), model.getIndex())));
  }

  @Override
  public void unbindViewHolder(VariationViewHolder holder) {
    // no op
  }

  @Override
  public DiffUtilComparator createDiffUtilComparator() {
    return new VariationDiffUtilComparator();
  }

  static final class VariationViewHolder extends RecyclerView.ViewHolder {

    final TextView valueText;

    VariationViewHolder(View itemView) {
      super(itemView);
      valueText = (TextView) itemView.findViewById(R.id.variation_value_text);
    }
  }
}
