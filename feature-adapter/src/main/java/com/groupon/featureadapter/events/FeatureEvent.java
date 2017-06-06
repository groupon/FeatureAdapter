package com.groupon.featureadapter.events;

/**
 * Marker interface for all events that can be fired from a feature.
 *
 * <p>Typically, when used in conjunction with grox-events, a class that implements this interface
 * will also implement the {@link com.groupon.events.Event} interface so that it can be turned into
 * {@link com.groupon.Action}s.
 *
 * <p>Note to maintainers: this class seems a bit strange as it looks both empty and redundant with
 * the {@link com.groupon.events.Event} class from grox-events. But this is done on purpose as we
 * don't want the feature control library to be coupled with grox directly, while letting developers
 * elegantly merge their own events with grox if they want to.
 */
public interface FeatureEvent {}
