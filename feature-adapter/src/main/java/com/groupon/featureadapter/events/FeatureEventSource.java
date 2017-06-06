package com.groupon.featureadapter.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Internal helper to addFeatureEventListener/removeFeatureEventListener {@link
 * FeatureEventListener} to a list of listeners and fire events to all of them.
 */
public class FeatureEventSource {
  /** Concurrency robust list of listener. */
  private List<FeatureEventListener> featureEventListeners = new CopyOnWriteArrayList<>();

  /**
   * Adds a listener to the list of listeners.
   *
   * @param featureEventListener the listener to be added.
   */
  public void addFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventListeners.add(featureEventListener);
  }

  /**
   * Removes a listener to the list of listeners.
   *
   * @param featureEventListener the listener to be removed.
   */
  public void removeFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventListeners.remove(featureEventListener);
  }

  /**
   * Emits an {@link FeatureEvent} to all listeners that were previously added.
   *
   * @param featureEvent the event to emit to all listeners.
   */
  public void fireEvent(FeatureEvent featureEvent) {
    for (FeatureEventListener featureEventListener : featureEventListeners) {
      featureEventListener.onFeatureEvent(featureEvent);
    }
  }
}
