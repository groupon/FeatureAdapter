package com.groupon.android.featureadapter.sample.features.header;

import com.groupon.featureadapter.DefaultDiffUtilComparator;

import java.util.Objects;

public class HeaderDiffUtilComparator extends DefaultDiffUtilComparator<HeaderModel> {
  @Override
  public Object getChangePayload(HeaderModel oldModel, HeaderModel newModel) {
    if (!hasValueChanged(oldModel.imageUrl, newModel.imageUrl)
      && hasValueChanged(oldModel.title, newModel.title)) {
      // capture when only the title has changed
      return newModel.title;
    }
    // request a full update
    return null;
  }

  private boolean hasValueChanged(String oldValue, String newValue) {
    return Objects.equals(oldValue, newValue);
  }

}
