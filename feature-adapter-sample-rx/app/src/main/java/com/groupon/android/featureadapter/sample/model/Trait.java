package com.groupon.android.featureadapter.sample.model;

import static solid.collectors.ToSolidList.toSolidList;
import static solid.stream.Stream.stream;

import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import java.util.Collections;
import javax.annotation.Nullable;
import solid.collections.SolidList;

@AutoValue
public abstract class Trait {
  public abstract String getName();

  public abstract SolidList<Variation> getVariations();

  public abstract boolean isExpanded();

  @Nullable
  public abstract Variation getSelectedVariation();

  public abstract int getIndex();

  public Trait withExpanded(boolean isExpanded) {
    return toBuilder().setExpanded(isExpanded).build();
  }

  public Trait withSelectedVariation(Variation variation) {
    return toBuilder().setSelectedVariation(variation).build();
  }

  public Trait withVariations(SolidList<Variation> newVariations) {
    return toBuilder().setVariations(newVariations).build();
  }

  abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_Trait.Builder()
        .setExpanded(true)
        .setVariations(new SolidList<>(Collections.emptyList()));
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String name);

    public abstract Builder setVariations(SolidList<Variation> variations);

    public abstract Builder setExpanded(boolean isExpanded);

    @NonNull
    public abstract Builder setSelectedVariation(Variation variation);

    public abstract Builder setIndex(int index);

    public abstract SolidList<Variation> getVariations();

    public Builder addVariation(Variation variation) {
      setVariations(stream(getVariations()).merge(variation).collect(toSolidList()));
      return this;
    }

    public abstract Trait build();
  }
}
