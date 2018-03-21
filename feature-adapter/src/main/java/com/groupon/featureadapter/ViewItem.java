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

/**
 * Wraps the model & view type into a single object.
 *
 * @param <MODEL> the view model of this item. It is an ouput class of a {@link FeatureController}.
 */
public final class ViewItem<MODEL> {

  /** The model of this item. */
  public final MODEL model;
  /** Must match a {@link AdapterViewTypeDelegate#getViewType()}. */
  public final int viewType;

  /**
   * Creates a new item.
   *
   * @param model the instance of the {@code MODEL} class.
   * @param adapterViewTypeDelegate the {@link AdapterViewTypeDelegate} associated with this item.
   */
  public ViewItem(MODEL model, AdapterViewTypeDelegate adapterViewTypeDelegate) {
    this.model = model;
    this.viewType = adapterViewTypeDelegate.getViewType();
  }
}
