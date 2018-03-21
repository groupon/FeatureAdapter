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
package com.groupon.featureadapter;

import static android.support.v7.widget.RecyclerView.Adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.events.FeatureEventListener;
import com.groupon.featureadapter.events.FeatureEventSource;
import java.util.List;

/**
 * A delegate of the @link {@link FeaturesAdapter} for a given view type. This entity is responsible
 * for:
 *
 * <ul>
 *   <li>Creating the views of a given view type.
 *   <li>Recycling the views and re-bind them to a new model.
 *   <li>Providing a few hooks related to the life cycle of the views it is in charge of.
 * </ul>
 *
 * @param <HOLDER>
 * @param <MODEL> the view model of this {@link AdapterViewTypeDelegate}. It is the same class as
 *     the {@link ViewItem}'s model class. It is an output model class of a {@link
 *     FeatureController}.
 */
public abstract class AdapterViewTypeDelegate<HOLDER extends RecyclerView.ViewHolder, MODEL> {

  private int viewType = RecyclerView.INVALID_TYPE;
  private final FeatureEventSource featureEventSource = new FeatureEventSource();

  void setViewType(int viewType) {
    this.viewType = viewType;
  }

  int getViewType() {
    return viewType;
  }

  /**
   * @return a {@link DiffUtilComparator} that will be used internally to animate the updates to the
   *     list of items associated to this feature controller. By default, it returns a {@link
   *     DefaultDiffUtilComparator}.
   * @see DiffUtil
   */
  public DiffUtilComparator createDiffUtilComparator() {
    return new DefaultDiffUtilComparator<>();
  }

  /**
   * Called by the Adapter from {@link Adapter#onCreateViewHolder(ViewGroup, int)}
   *
   * @param parent The ViewGroup into which the new View will be added after it is bound to an
   *     adapter position.
   * @return A new ViewHolder that holds a View of the given view type.
   */
  public abstract HOLDER createViewHolder(ViewGroup parent);

  /**
   * Called by the Adapter from {@link Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)} Maps
   * the data back to the view.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the item at
   *     the given position in the data set.
   * @param model The Model containing all the data for the ViewHolder to bind to
   */
  public abstract void bindViewHolder(HOLDER holder, MODEL model);

  /**
   * Called by the Adapter from {@link Adapter#onBindViewHolder(RecyclerView.ViewHolder, int,
   * List)}. Maps the data back to the view, using a non empty PayLoad.
   *
   * <p>The default implementation discards the {@code payloads} information and just calls {@link
   * #bindViewHolder(RecyclerView.ViewHolder, MODEL)}. Override, if you want to implement more
   * fine-grained, payload based updates.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the item at
   *     the given position in the data set.
   * @param model The Model containing all the data for the ViewHolder to bind to.
   * @param payloads the payload of changes. An empty list means the view holder needs full re-bind.
   */
  public void bindViewHolder(HOLDER holder, MODEL model, List<Object> payloads) {
    bindViewHolder(holder, model);
  }

  /**
   * Called by the Adapter from {@link Adapter#onViewRecycled(RecyclerView.ViewHolder)}
   *
   * @param holder The ViewHolder for the view being recycled
   */
  public abstract void unbindViewHolder(HOLDER holder);

  /**
   * Called by the Adapter from {@link Adapter#onViewAttachedToWindow(RecyclerView.ViewHolder)} A
   * good place to register NST impressions
   *
   * @param holder Holder of the view being attached
   */
  public void onAttachToWindow(HOLDER holder) {}

  /**
   * Called by the Adapter from {@link Adapter#onViewDetachedFromWindow(RecyclerView.ViewHolder)}
   *
   * @param holder Holder of the view being detached
   */
  public void onDetachToWindow(HOLDER holder) {}

  /**
   * Adds a listener to the list of listeners.
   *
   * @param featureEventListener the listener to be added.
   */
  protected void addFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventSource.addFeatureEventListener(featureEventListener);
  }

  /**
   * Removes a listener to the list of listeners.
   *
   * @param featureEventListener the listener to be removed.
   */
  protected void removeFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventSource.removeFeatureEventListener(featureEventListener);
  }

  /**
   * Fires an {@link FeatureEvent} to all listeners.
   *
   * @param featureEvent the event to be passed to all listeners.
   */
  protected void fireEvent(FeatureEvent featureEvent) {
    featureEventSource.fireEvent(featureEvent);
  }
}
