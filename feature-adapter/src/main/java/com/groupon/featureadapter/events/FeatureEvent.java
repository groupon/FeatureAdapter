/*
 * Copyright (c) 2017, Groupon, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
