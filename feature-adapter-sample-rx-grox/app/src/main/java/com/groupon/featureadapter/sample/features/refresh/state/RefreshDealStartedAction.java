package com.groupon.featureadapter.sample.features.refresh.state;

import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.grox.Action;

public class RefreshDealStartedAction implements Action<DealDetailsModel> {

  @Override
  public DealDetailsModel reduce(DealDetailsModel oldState) {
    return oldState;
  }
}
