package com.groupon.featureadapter.sample.model;

import static solid.collectors.ToSolidList.toSolidList;
import static solid.stream.Stream.stream;

import com.google.auto.value.AutoValue;
import java.util.Collections;
import javax.annotation.Nullable;
import solid.collections.SolidList;

@AutoValue
public abstract class Deal {
  public abstract String getId();

  public abstract String getTitle();

  public abstract SolidList<Option> getOptions();

  public abstract SolidList<Trait> getTraits();

  public abstract String getPrice();

  @Nullable
  public abstract String getImageUrl();

  public Deal withTraitAt(int indexTrait, Trait trait) {
    SolidList<Trait> newTraits =
        stream(getTraits())
            .index()
            .map(it -> it.index == indexTrait ? trait : it.value)
            .collect(toSolidList());
    return toBuilder().setTraits(newTraits).build();
  }

  abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_Deal.Builder()
        .setTraits(new SolidList<>(Collections.emptyList()))
        .setOptions(new SolidList<>(Collections.emptyList()));
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setId(String id);

    public abstract Builder setTitle(String title);

    public abstract Builder setOptions(SolidList<Option> options);

    public abstract Builder setTraits(SolidList<Trait> traits);

    public abstract Builder setPrice(String price);

    public abstract Builder setImageUrl(String imageUrl);

    public abstract SolidList<Option> getOptions();

    public Builder addOption(Option option) {
      setOptions(stream(getOptions()).merge(option).collect(toSolidList()));
      return this;
    }

    public Builder addOption(Option.Builder optionBuilder) {
      addOption(optionBuilder.build());
      return this;
    }

    public abstract SolidList<Trait> getTraits();

    public Builder addTrait(Trait trait) {
      setTraits(stream(getTraits()).merge(trait).collect(toSolidList()));
      return this;
    }

    public Builder addTrait(Trait.Builder traitBuilder) {
      addTrait(traitBuilder.build());
      return this;
    }

    public abstract Deal build();
  }
}
