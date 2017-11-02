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
package com.groupon.android.featureadapter.sample.features.options;

import com.google.auto.value.AutoValue;

/**
 * It is not mandatory to use AutoValue to create the feature (small) models, we recommend it
 * as it generates valid equals and hashcode methods, and enforces immutable models.
 * (which are required)
 */
@AutoValue
abstract class OptionsModel {
  abstract String uuid();
  abstract String title();
  abstract String price();
  abstract boolean selected();

  abstract Builder toBuilder();

  static Builder builder() {
    return new AutoValue_OptionsModel.Builder();
  }

  @AutoValue.Builder
  static abstract class Builder {
    abstract Builder setUuid(String uuid);
    abstract Builder setTitle(String title);
    abstract Builder setPrice(String price);
    abstract Builder setSelected(boolean selected);
    abstract OptionsModel build();
  }
}
