package com.groupon.android.featureadapter.sample;

import com.google.auto.value.AutoValue;
import com.groupon.android.featureadapter.sample.model.Deal;
import com.groupon.android.featureadapter.sample.model.Option;
import javax.annotation.Nullable;

@AutoValue
public abstract class DealDetailsModel {
  @Nullable
  public abstract Deal getDeal();

  @Nullable
  public abstract Throwable getRefreshDealError();

  @Nullable
  public abstract Option getOption();

  @Nullable
  public abstract String getSummary();

  public static Builder builder() {
    return new AutoValue_DealDetailsModel.Builder().setRefreshing(true);
  }

  public abstract Builder toBuilder();

  public abstract boolean isRefreshing();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setDeal(Deal deal);

    public abstract Builder setRefreshDealError(Throwable error);

    public abstract Builder setOption(Option option);

    public abstract Builder setSummary(String summary);

    public abstract Builder setRefreshing(boolean isRefreshing);

    public abstract DealDetailsModel build();
  }
}
