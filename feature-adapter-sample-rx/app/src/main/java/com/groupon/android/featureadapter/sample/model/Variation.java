package com.groupon.android.featureadapter.sample.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Variation {
  public abstract String getValue();

  public abstract int getTraitIndex();

  public abstract int getIndex();

  public abstract boolean isSelected();

  public Variation withSelected(boolean selected) {
    return toBuilder().setSelected(selected).build();
  }

  abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_Variation.Builder().setSelected(false);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setValue(String value);

    public abstract Builder setTraitIndex(int traitIndex);

    public abstract Builder setIndex(int index);

    public abstract Builder setSelected(boolean selected);

    public abstract Variation build();
  }
}
