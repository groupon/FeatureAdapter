package com.groupon.featureadapter.sample;

import com.google.auto.value.AutoValue;
import com.groupon.featureadapter.sample.model.Deal;
import com.groupon.featureadapter.sample.model.Option;
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
    return new AutoValue_DealDetailsModel.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setDeal(Deal deal);

    public abstract Builder setRefreshDealError(Throwable error);

    public abstract Builder setOption(Option option);

    public abstract Builder setSummary(String summary);

    public abstract DealDetailsModel build();
  }
}
