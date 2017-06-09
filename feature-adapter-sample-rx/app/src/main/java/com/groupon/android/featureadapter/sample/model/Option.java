package com.groupon.android.featureadapter.sample.model;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class Option {
  public abstract String getUuid();

  public abstract String getTitle();

  public abstract String getPrice();

  public abstract String getHtml();

  @Nullable
  public abstract String getImageUrl();

  public static Builder builder() {
    return new AutoValue_Option.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setUuid(String uuid);

    public abstract Builder setTitle(String title);

    public abstract Builder setPrice(String price);

    public abstract Builder setHtml(String html);

    public abstract Builder setImageUrl(String imageUrl);

    public abstract Option build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Option option = (Option) o;

    return getUuid().equals(option.getUuid());
  }

  @Override
  public int hashCode() {
    return getUuid().hashCode();
  }
}
