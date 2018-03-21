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
package com.groupon.android.featureadapter.sample.state;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.google.auto.value.AutoValue;
import com.groupon.android.featureadapter.sample.features.collapsible.CollapsibleFeatureState;
import com.groupon.android.featureadapter.sample.model.Deal;
import com.groupon.android.featureadapter.sample.model.Option;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@AutoValue
public abstract class SampleModel {

  public static final String STATE_READY = "STATE_READY";
  public static final String STATE_LOADING = "STATE_LOADING";
  public static final String STATE_ERROR = "STATE_ERROR";

  @Retention(SOURCE)
  @StringDef({
    STATE_LOADING,
    STATE_READY,
    STATE_ERROR
  })
  public @interface State {}

  @Nullable
  public abstract Deal deal();

  @Nullable
  public abstract Option selectedOption();

  @State
  public abstract String state();

  @Nullable
  public abstract String exceptionText();

  public abstract CollapsibleFeatureState collapsibleFeatureState();

  @Nullable
  public abstract String highlightedBadge();

  public abstract Builder toBuilder();

  public static Builder builder() {
    return new AutoValue_SampleModel.Builder()
      .setState(STATE_READY)
      .setCollapsibleFeatureState(CollapsibleFeatureState.DEFAULT);
  }

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder setDeal(Deal deal);

    public abstract Builder setSelectedOption(Option option);

    public abstract Builder setState(@State String state);

    public abstract Builder setExceptionText(String exceptionText);

    public abstract Builder setCollapsibleFeatureState(CollapsibleFeatureState collapsibleFeatureState);

    public abstract Builder setHighlightedBadge(@Nullable String highlightedBadge);

    public abstract SampleModel build();
  }
}
