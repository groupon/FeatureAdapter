package com.groupon.featureadapter.sample.features.refresh.state;

import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.featureadapter.sample.model.Deal;
import com.groupon.grox.Action;

public class RefreshDealSuccessAction implements Action<DealDetailsModel> {
  private Deal deal;

  public RefreshDealSuccessAction(Deal deal) {
    this.deal = deal;
  }

  @Override
  public DealDetailsModel reduce(DealDetailsModel oldState) {
    return oldState
        .toBuilder()
        .setDeal(deal)
        .setOption(deal.getOptions().get(0))
        .setSummary(deal.getOptions().get(0).getTitle())
        .setRefreshDealError(null)
        .build();
  }
}
