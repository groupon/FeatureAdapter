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
