/*
 * Copyright (c) 2017, Groupon, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of GROUPON nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.groupon.featureadapter.events;

import static rx.Observable.merge;

import com.groupon.featureadapter.FeatureController;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

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
  public static <MODEL> Observable<FeatureEvent> featureEvents(
      List<FeatureController<MODEL>> featureControllers) {
    List<Observable<FeatureEvent>> observables = new ArrayList<>();
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
  public static <MODEL> Observable<FeatureEvent> featureEvents(
      FeatureController<MODEL> controller) {
    return Observable.create(new FeatureControllerOnSubscribe(controller));
  }
}
