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
 * Implement this interface and set it to the {@link FeaturesAdapter} to catch and handle any error that happens during the binding process
 * see {@link FeaturesAdapter#setFeaturesAdapterErrorHandler(FeaturesAdapterErrorHandler)}
 */
@FunctionalInterface
public interface FeaturesAdapterErrorHandler {

  /**
  * Implement this method to catch and handle any error that happens during the binding process
  *
  * @param throwable the error
  * @param position the position of the feature that caused the error
  */
  void onBindViewHolderError(Throwable throwable, int position);
}
