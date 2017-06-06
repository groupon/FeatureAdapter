package com.groupon.featureadapter;

import android.support.v7.util.DiffUtil.DiffResult;
import com.groupon.featureadapter.FeaturesAdapter.FeatureDataSegment;
import java.util.List;
import javax.annotation.Nullable;

public class FeatureUpdate {
  public final int featureIndex;
  @Nullable public final List<ViewItem> newItems;
  @Nullable public final DiffResult diffResult;
  @Nullable public final FeatureDataSegment dataSegment;

  public FeatureUpdate(
      int featureIndex,
      List<ViewItem> newItems,
      DiffResult diffResult,
      FeatureDataSegment dataSegment) {
    this.featureIndex = featureIndex;
    this.newItems = newItems;
    this.diffResult = diffResult;
    this.dataSegment = dataSegment;
  }
}
