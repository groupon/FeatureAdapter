package com.groupon.featureadapter.sample.features.inlinevariations.state;

import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.featureadapter.sample.model.Deal;
import com.groupon.featureadapter.sample.model.Trait;
import com.groupon.grox.commands.rxjava1.SingleActionCommand;

public class TraitClickAction extends SingleActionCommand<DealDetailsModel>
    implements FeatureEvent {
  private final int traitIndex;

  public TraitClickAction(int traitIndex) {
    this.traitIndex = traitIndex;
  }

  @Override
  public DealDetailsModel reduce(DealDetailsModel oldState) {
    Deal deal = oldState.getDeal();
    Trait trait = deal.getTraits().get(traitIndex);
    Trait newTrait = trait.withExpanded(!trait.isExpanded());
    return oldState.toBuilder().setDeal(deal.withTraitAt(traitIndex, newTrait)).build();
  }
}
