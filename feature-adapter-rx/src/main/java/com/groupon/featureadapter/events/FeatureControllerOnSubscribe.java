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
