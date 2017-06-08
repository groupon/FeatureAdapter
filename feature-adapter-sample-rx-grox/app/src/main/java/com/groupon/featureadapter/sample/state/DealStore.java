package com.groupon.featureadapter.sample.state;

import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.grox.Store;

public class DealStore extends Store<DealDetailsModel> {
  public DealStore(DealDetailsModel initialState) {
    super(initialState, new LoggerMiddleWare());
  }
}
