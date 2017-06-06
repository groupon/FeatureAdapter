package com.groupon.featureadapter.events;

/** A listener of {@link FeatureEvent}s. */
public interface FeatureEventListener {
  /**
   * Reacts to a {@link FeatureEvent} being emitted.
   *
   * @param featureEvent the event that was emitted.
   */
  void onFeatureEvent(FeatureEvent featureEvent);
}
