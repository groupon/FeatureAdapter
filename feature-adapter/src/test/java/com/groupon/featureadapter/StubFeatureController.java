package com.groupon.featureadapter;

import java.util.List;

class StubFeatureController<T> extends FeatureController<T> {

  private List<AdapterViewTypeDelegate> binders;
  private List<ViewItem> items;

  public StubFeatureController(List<AdapterViewTypeDelegate> binders) {
    this.binders = binders;
  }

  public StubFeatureController(List<AdapterViewTypeDelegate> binders, List<ViewItem> items) {
    this(binders);
    this.items = items;
  }

  @Override
  public List<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return binders;
  }

  @Override
  public List<ViewItem> buildItems(T s) {
    return items;
  }
}
