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
package com.groupon.android.featureadapter.sample.features.collapsible;

import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.grox.commands.rxjava1.SingleActionCommand;

class OnCollapsibleParentTap extends SingleActionCommand<SampleModel> implements FeatureEvent {

  @Override
  public SampleModel newState(SampleModel oldState) {
    final boolean newIsCollapsed = !oldState.collapsibleFeatureState().isCollapsed;
    final CollapsibleFeatureState newCollapsibleFeatureState = new CollapsibleFeatureState(newIsCollapsed);
    return oldState
      .toBuilder()
      .setCollapsibleFeatureState(newCollapsibleFeatureState)
      .build();
  }
}
