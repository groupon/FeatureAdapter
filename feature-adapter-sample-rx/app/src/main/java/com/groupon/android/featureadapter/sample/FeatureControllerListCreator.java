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

import com.groupon.android.featureadapter.sample.features.header.HeaderController;
import com.groupon.android.featureadapter.sample.features.inlinevariations.IVController;
import com.groupon.android.featureadapter.sample.features.multioptions.OptionsController;
import com.groupon.featureadapter.FeatureController;
import java.util.Arrays;
import java.util.List;

public class FeatureControllerListCreator {

  private List<FeatureController<DealDetailsModel>> group;

  public FeatureControllerListCreator() {
    group = Arrays.asList(new HeaderController(), new OptionsController(), new IVController());
  }

  public List<FeatureController<DealDetailsModel>> getFeatureControllerList() {
    return group;
  }
}
