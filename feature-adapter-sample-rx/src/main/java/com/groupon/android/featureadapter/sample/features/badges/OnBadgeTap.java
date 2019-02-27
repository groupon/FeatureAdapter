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
package com.groupon.android.featureadapter.sample.features.badges;

import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.grox.commands.rxjava2.Command;
import com.groupon.grox.commands.rxjava2.SingleActionCommand;

class OnBadgeTap extends SingleActionCommand<SampleModel> implements FeatureEvent {

  private final String badge;

  OnBadgeTap(String badge) {
    this.badge = badge;
  }

  @Override
  public SampleModel newState(SampleModel oldState) {
    String newHighlightedBadge = badge.equals(oldState.highlightedBadge()) ? null : badge;
    return oldState
      .toBuilder()
      .setHighlightedBadge(newHighlightedBadge)
      .build();
  }
}
