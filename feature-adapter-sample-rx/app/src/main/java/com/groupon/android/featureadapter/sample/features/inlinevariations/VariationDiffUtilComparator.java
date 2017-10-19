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
package com.groupon.android.featureadapter.sample.features.inlinevariations;

import com.groupon.android.featureadapter.sample.model.Variation;
import com.groupon.featureadapter.DefaultDiffUtilComparator;

class VariationDiffUtilComparator extends DefaultDiffUtilComparator<Variation> {

  @Override
  public boolean areItemsTheSame(Variation oldModel, Variation newModel) {
    return oldModel.getTraitIndex() == newModel.getTraitIndex()
        && oldModel.getIndex() == newModel.getIndex();
  }
}
