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


import com.groupon.featureadapter.FeatureController;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;

import static io.reactivex.Flowable.merge;

/**
 * A helper class to make it easier to use {@link FeatureEvent}s from {@link FeatureController}s
 * with Rx 1.
 */
public class RxFeatureEvent {

  /**
   * Creates an observable of {@link FeatureEvent}s out of a {@link FeatureControllerGroup}. It is
   * possible to call this method multiple times on the controller.
   *
   * <p><em>Warning:</em> The created observable keeps a strong reference to {@code
   * featureControllers}. Unsubscribe to free this reference.
   *
   * @param featureControllers a list of feature controllers.
   * @return an observable of the {@link FeatureEvent} that this group emits.
   */
  public static <MODEL> Flowable<FeatureEvent> featureEvents(
      List<FeatureController<MODEL>> featureControllers) {
    List<Flowable<FeatureEvent>> observables = new ArrayList<>();
    for (FeatureController controller : featureControllers) {
      observables.add(featureEvents(controller));
    }
    return merge(observables);
  }

  /**
   * Creates an observable of {@link FeatureEvent}s out of a {@link FeatureController}. It is
   * possible to call this method multiple times on the controller.
   *
   * <p><em>Warning:</em> The created observable keeps a strong reference to {@code controller}.
   * Unsubscribe to free this reference.
   *
   * @param controller a {@link FeatureController}.
   * @return an observable of the {@link FeatureEvent} that this controller emits.
   */
  public static <MODEL> Flowable<FeatureEvent> featureEvents(
      FeatureController<MODEL> controller) {
    return Flowable.create(new FeatureControllerOnSubscribe(controller), BackpressureStrategy.BUFFER);
  }
}
