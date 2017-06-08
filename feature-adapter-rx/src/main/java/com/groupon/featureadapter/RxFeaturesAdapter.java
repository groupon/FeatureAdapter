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
package com.groupon.featureadapter;

import static rx.Observable.from;
import static rx.Observable.range;
import static rx.Observable.zip;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.computation;

import android.util.Pair;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class RxFeaturesAdapter<MODEL> extends FeaturesAdapter<MODEL> {

  public RxFeaturesAdapter(List<FeatureController<MODEL>> featureControllers) {
    super(featureControllers);
  }

  /**
   * Updates asynchronously the adapter.
   *
   * @param modelObservable the stream of models. Ideally it is subscribed on the computation
   *     thread.
   * @return an observable of {@link FeatureUpdate} if one wants to track the adapter changes.
   */
  @SuppressWarnings("WeakerAccess")
  public Observable<FeatureUpdate> updateFeatureItems(Observable<MODEL> modelObservable) {
    return modelObservable.compose(updateAdapter(this));
  }

  /**
   * Transforms an {@link Observable} of {@code MODEL} into an {@link Observable} of {@link
   * FeatureUpdate}. It updates concurrently {@code featuresAdapter} by computing the result of all
   * operations to update the list of items in the adapter every time a new {@code MODEL} is emitted
   * on the source observable.
   *
   * @param featureAdapter the adapter in which the update takes place.
   * @param <MODEL> the class of the input model of the {@link FeatureController}s.
   * @return an {@link Observable} of {@link FeatureUpdate}.
   */
  private static <MODEL> Observable.Transformer<MODEL, FeatureUpdate> updateAdapter(
      FeaturesAdapter<MODEL> featureAdapter) {
    return modelObservable -> modelObservable.compose(toFeatureUpdates(featureAdapter));
  }

  private static <MODEL> Observable.Transformer<MODEL, FeatureUpdate> toFeatureUpdates(
      FeaturesAdapter<MODEL> featureAdapter) {
    //here we want to do multiple things:
    //1 - compute concurrently all list of items via feature controllers
    //2 - update the UI as soon as possible
    //3 - make sure that given a model, we compute only one list of items from each feature controller.
    //We want to avoid the case where a feature controller is too slow compared to others and the UI
    //would be updated with inconsistent list of items (pats of list of items from the first model,
    // and parts from a subsequent model).

    //the ticker observable is gonna emit an item every time all the
    //list of items from all the feature controllers have been computed
    //so we just process the model instances one at a time
    //this is meant to be a very fine grained back pressure mechanism
    BehaviorSubject<Object> tickObservable = BehaviorSubject.create();
    tickObservable.onNext(null);
    return modelObservable ->
        modelObservable
            .observeOn(computation())
            .zipWith(tickObservable, (model, tick) -> model)
            .publish(
                shared -> {
                  //each feature controller receives a fork of the model observable
                  //and compute its items in parallel, and then updates the UI ASAP
                  //but we still aggregate all the list to be sure to pace the model observable
                  //correctly using the tick observable

                  //iterate with an index on the feature controllers
                  return from(featureAdapter.getFeatureControllers())
                      .zipWith(range(0, Integer.MAX_VALUE), Pair::new)
                      //for each of them, compose the shared model observable
                      //to get an observable of feature updates.
                      //as soon as we got the feature update, we update the UI
                      .map(
                          pair ->
                              shared
                                  .compose(
                                      toFeatureUpdates(featureAdapter, pair.second, pair.first))
                                  .observeOn(mainThread())
                                  .doOnNext(featureAdapter::updateFeatureItems)
                                  //TODO: we should probably let users decide where to execute this
                                  .observeOn(computation()))
                      //collect all observable of feature updates in a list
                      .collect(ArrayList<Observable<FeatureUpdate>>::new, ArrayList::add)
                      //zip together all observables of this list so that we get an observable
                      //of feature updates [] for the processing of a single model emitted by shared
                      //once we got all these feature updates element, we indicate
                      // to the original model observable that we can proceed next model
                      // via a tick to the ticker
                      .flatMap(
                          list ->
                              zip(
                                  list,
                                  args -> {
                                    tickObservable.onNext(null);
                                    return args;
                                  }))
                      //we now create an observable of feature updates from the []
                      .flatMap(Observable::from)
                      .cast(FeatureUpdate.class)
                      .filter(diff -> diff != null);
                });
  }

  private static <MODEL> Observable.Transformer<MODEL, FeatureUpdate> toFeatureUpdates(
      FeaturesAdapter<MODEL> featuresAdapter,
      int featureIndex,
      FeatureController<MODEL> featureController) {
    return modelObservable ->
        modelObservable.map(
            model -> featuresAdapter.toFeatureUpdate(model, featureIndex, featureController));
  }
}
