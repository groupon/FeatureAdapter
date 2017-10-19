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
package com.groupon.android.featureadapter.sample.model;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class Option {
  public abstract String getUuid();

  public abstract String getTitle();

  public abstract String getPrice();

  public abstract String getHtml();

  @Nullable
  public abstract String getImageUrl();

  public static Builder builder() {
    return new AutoValue_Option.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setUuid(String uuid);

    public abstract Builder setTitle(String title);

    public abstract Builder setPrice(String price);

    public abstract Builder setHtml(String html);

    public abstract Builder setImageUrl(String imageUrl);

    public abstract Option build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Option option = (Option) o;

    return getUuid().equals(option.getUuid());
  }

  @Override
  public int hashCode() {
    return getUuid().hashCode();
  }
}
