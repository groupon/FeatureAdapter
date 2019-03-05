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
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class FeatureControllerOnSubscribe implements ObservableOnSubscribe<FeatureEvent> {
  final FeatureController featureController;

  FeatureControllerOnSubscribe(FeatureController featureController) {
    this.featureController = featureController;
  }

  @Override
  public void subscribe(final ObservableEmitter<FeatureEvent> subscriber) {
    verifyMainThread();

    FeatureEventListener listener =
        event -> {
          if (!subscriber.isDisposed()) {
            subscriber.onNext(event);
          }
        };

    subscriber.setCancellable(() -> featureController.removeFeatureEventListener(listener));

    featureController.addFeatureEventListener(listener);
  }
}
