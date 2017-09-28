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
 * Default implementation of {@link DiffUtilComparator}. It considers that items at the same
 * position are the same (basically items are place holders), but can have changed between the new
 * list and the old list.
 *
 * @param <MODEL>
 */
public class DefaultDiffUtilComparator<MODEL> implements DiffUtilComparator<MODEL> {

  /**
   * @param oldModel the item in the old list.
   * @param newModel the item in the new list.
   * @return true. Basically items are place holders.
   */
  @Override
  public boolean areItemsTheSame(MODEL oldModel, MODEL newModel) {
    return true;
  }

  /**
   * @param oldModel the item in the old list.
   * @param newModel the item in the new list.
   * @return true if {@code oldModel.equals(newModel)} and false otherwise.
   */
  @Override
  public boolean areContentsTheSame(MODEL oldModel, MODEL newModel) {
    return oldModel.equals(newModel);
  }

  /**
   *
   * @param oldModel the item in the old list.
   * @param newModel the item in the new list.
   * @return default to null
   */
  @Override
  public Object getChangePayload(MODEL oldModel, MODEL newModel) {
    return null;
  }
}
