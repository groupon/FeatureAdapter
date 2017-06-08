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

import static android.support.v7.widget.RecyclerView.Adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.events.FeatureEventListener;
import com.groupon.featureadapter.events.FeatureEventSource;

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
  private FeatureEventSource featureEventSource = new FeatureEventSource();

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
