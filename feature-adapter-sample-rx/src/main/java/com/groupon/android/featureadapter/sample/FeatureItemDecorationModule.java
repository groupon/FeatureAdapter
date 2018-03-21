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
package com.groupon.android.featureadapter.sample;

import com.groupon.featureadapter.FeatureAdapterItemDecoration;

import toothpick.config.Module;

/**
 * Provides a shared instance of {@link FeatureAdapterItemDecoration} to pass to the RecyclerView
 * and to inject into the Features themselves.
 */
class FeatureItemDecorationModule extends Module {
  FeatureItemDecorationModule() {
    bind(FeatureAdapterItemDecoration.class).toInstance(new FeatureAdapterItemDecoration());
  }
}
