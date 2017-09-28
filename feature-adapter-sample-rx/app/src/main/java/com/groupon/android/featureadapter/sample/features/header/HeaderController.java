package com.groupon.android.featureadapter.sample.features.header;

import com.groupon.android.featureadapter.sample.DealDetailsModel;
import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;

public class HeaderController extends FeatureController<DealDetailsModel> {

  private final HeaderAdapterViewTypeDelegate headerAdapterViewTypeDelegate =
      new HeaderAdapterViewTypeDelegate();

  private Option oldOption = null;
  private String oldTitle = null;

  @Override
  public List<ViewItem> buildItems(DealDetailsModel model) {
    if (model.getSummary() == null) {
      return null;
    }

    if (hasChangedSinceLastUpdate(model)) {
      return null;
    } else {
      memorizeUpdate(model);
    }

    HeaderModel summaryModel = new HeaderModel(model.getSummary(), model.getOption().getImageUrl());
    return singletonList(new ViewItem<>(summaryModel, headerAdapterViewTypeDelegate));
  }

  private void memorizeUpdate(DealDetailsModel model) {
    oldOption = model.getOption();
    oldTitle = model.getSummary();
  }

  private boolean hasChangedSinceLastUpdate(DealDetailsModel model) {
    return model.getOption() == oldOption && Objects.equals(model.getSummary(), oldTitle);
  }

  @Override
  public List<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return singletonList(headerAdapterViewTypeDelegate);
  }
}
