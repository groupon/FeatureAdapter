package com.groupon.featureadapter.sample.features.header;

import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;
import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.featureadapter.sample.model.Option;
import java.util.Arrays;
import java.util.List;

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

    HeaderModel summaryModel = new HeaderModel();
    summaryModel.title = model.getSummary();
    summaryModel.imageUrl = model.getOption().getImageUrl();
    return Arrays.asList(new ViewItem<>(summaryModel, headerAdapterViewTypeDelegate));
  }

  private void memorizeUpdate(DealDetailsModel model) {
    oldOption = model.getOption();
    oldTitle = model.getSummary();
  }

  private boolean hasChangedSinceLastUpdate(DealDetailsModel model) {
    return model.getOption() == oldOption && model.getSummary() == oldTitle;
  }

  @Override
  public List<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return Arrays.asList(headerAdapterViewTypeDelegate);
  }
}
