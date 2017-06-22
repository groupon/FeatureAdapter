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

import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.events.FeatureEventListener;
import com.groupon.featureadapter.events.FeatureEventSource;

import java.util.Collection;
import java.util.List;

/**
 * The main class to represent a feature in the feature-control library. A feature receives an input
 * model (typically from a data source such as a network call), and converts it into a list of items
 * that will be displayed inside the recycler view. Items can be of different types and rendered
 * using different views. Each item of the list will use its own output model, a partial
 * representation of the input model that is of interest to its associated view.
 *
 * <p>Example: Let's consider the input model class
 *
 * <pre>
 *  class Foo {
 *    String title;
 *    String currency;
 *    String price;
 *  }
 * </pre>
 *
 * To represent this input model class in feature-control, we could create as many different feature
 * controllers as we want. Each of them could be in charge of representing one or more field, and/or
 * one or more computed values from this input model (like the price in various different
 * currencies, or the amount of taxes, etc..). We could have for instance a feature controller that
 * represents the title, with items of type String. And a feature controller that represents the
 * price and the currency together, using a new output model, a simple pojo with 2 strings or it
 * could represent the price and the currency as separate items and use different views to represent
 * them.
 *
 * @param <MODEL> the class of the input model that this controller will represent partially.
 */
public abstract class FeatureController<MODEL> {

  private final FeatureEventSource featureEventSource = new FeatureEventSource();

  /**
   * @return the list of the {@link AdapterViewTypeDelegate} that will be used to represent each
   *     item on screen.
   */
  public abstract Collection<AdapterViewTypeDelegate> getAdapterViewTypeDelegates();

  /**
   * Builds the list of items to represent the {@code model}. When building the {@link ViewItem}s,
   * we must provide them with a view type. The view type must be one of the view type of the {@link
   * AdapterViewTypeDelegate}s provided by the method {@link #getAdapterViewTypeDelegates()}.
   *
   * @param model the input model instance.
   * @return a list of {@link ViewItem} that represent an aspect of {@code model}.
   * @see #getAdapterViewTypeDelegates()
   * @see AdapterViewTypeDelegate#getViewType()
   * @see ViewItem#viewType
   */
  public abstract List<ViewItem> buildItems(MODEL model);

  //TODO: not used. Do we really need this.
  public void onDestroy() {
    // override to handle
  }

  /**
   * Adds a {@link FeatureEventListener} to all the {@link AdapterViewTypeDelegate} returned by
   * {@link #getAdapterViewTypeDelegates()}.
   *
   * @param featureEventListener the listener to be added.
   */
  public void addFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventSource.addFeatureEventListener(featureEventListener);
    for (AdapterViewTypeDelegate adapterViewTypeDelegate : getAdapterViewTypeDelegates()) {
      adapterViewTypeDelegate.addFeatureEventListener(featureEventListener);
    }
  }

  /**
   * Removes a {@link FeatureEventListener} from all the {@link AdapterViewTypeDelegate} returned by
   * {@link #getAdapterViewTypeDelegates()}.
   *
   * @param featureEventListener the listener to be removed.
   */
  public void removeFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventSource.removeFeatureEventListener(featureEventListener);
    for (AdapterViewTypeDelegate adapterViewTypeDelegate : getAdapterViewTypeDelegates()) {
      adapterViewTypeDelegate.removeFeatureEventListener(featureEventListener);
    }
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
