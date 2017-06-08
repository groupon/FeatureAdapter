package com.groupon.featureadapter.sample.features.inlinevariations.state;

import static solid.collectors.ToSolidList.toSolidList;
import static solid.stream.Stream.stream;

import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.featureadapter.sample.model.Deal;
import com.groupon.featureadapter.sample.model.Trait;
import com.groupon.featureadapter.sample.model.Variation;
import com.groupon.grox.commands.rxjava1.SingleActionCommand;
import solid.collections.SolidList;

public class VariationClickAction extends SingleActionCommand<DealDetailsModel>
    implements FeatureEvent {
  private final int traitIndex;
  private final int variationIndex;

  public VariationClickAction(int traitIndex, int variationIndex) {
    this.traitIndex = traitIndex;
    this.variationIndex = variationIndex;
  }

  @Override
  public DealDetailsModel reduce(DealDetailsModel oldState) {
    Deal deal = oldState.getDeal();
    Trait trait = deal.getTraits().get(traitIndex);
    SolidList<Variation> variations = trait.getVariations();
    Variation targetVariation = variations.get(variationIndex);

    // toggle selection for variation
    Variation selectedVariation = targetVariation.isSelected() ? null : targetVariation;

    // update variation selection states
    variations =
        stream(variations)
            .map(v -> v.withSelected(v.equals(selectedVariation)))
            .collect(toSolidList());

    // update trait view state
    trait = trait.withSelectedVariation(selectedVariation).withVariations(variations);

    //update summary
    final String summary =
        oldState.getOption().getTitle() + " " + targetVariation.getValue() + " " + trait.getName();

    return oldState
        .toBuilder()
        .setDeal(deal.withTraitAt(traitIndex, trait))
        .setSummary(summary)
        .build();
  }
}
