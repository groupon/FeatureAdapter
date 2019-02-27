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

import com.groupon.android.featureadapter.sample.events.SingleActionBaseCommand;
import com.groupon.android.featureadapter.sample.model.Deal;
import com.groupon.android.featureadapter.sample.model.Option;
import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.grox.commands.rxjava2.SingleActionCommand;

class OnOptionClickEvent extends SingleActionBaseCommand {

  private final String uuid;

  OnOptionClickEvent(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public SampleModel newState(SampleModel model) {
    Option newOption = findOption(model.deal(), uuid);
    return model.toBuilder()
      .setSelectedOption(newOption != model.selectedOption() ? newOption : null)
      .build();
  }

  private static Option findOption(Deal deal, String uuid) {
    for (Option option : deal.options) {
      if (uuid.equals(option.uuid)) return option;
    }
    throw new IllegalArgumentException("Option does not exist");
  }
}
