package com.groupon.featureadapter.sample.features.refresh.state;

import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.grox.Action;

public class RefreshDealFailureAction implements Action<DealDetailsModel> {
  private Throwable error;

  public RefreshDealFailureAction(Throwable error) {
    this.error = error;
  }

  @Override
  public DealDetailsModel reduce(DealDetailsModel oldState) {
    return oldState.toBuilder().setDeal(null).setRefreshDealError(error).build();
  }
}
