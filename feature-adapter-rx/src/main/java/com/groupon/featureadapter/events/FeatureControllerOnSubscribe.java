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

import static rx.android.MainThreadSubscription.verifyMainThread;

import com.groupon.featureadapter.FeatureController;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

final class FeatureControllerOnSubscribe implements Observable.OnSubscribe<FeatureEvent> {
  final FeatureController featureController;

  FeatureControllerOnSubscribe(FeatureController featureController) {
    this.featureController = featureController;
  }

  @Override
  public void call(final Subscriber<? super FeatureEvent> subscriber) {
    verifyMainThread();

    FeatureEventListener listener =
        new FeatureEventListener() {
          @Override
          public void onFeatureEvent(FeatureEvent event) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(event);
            }
          }
        };

    subscriber.add(
        new MainThreadSubscription() {
          @Override
          protected void onUnsubscribe() {
            featureController.removeFeatureEventListener(listener);
          }
        });

    featureController.addFeatureEventListener(listener);
  }
}
