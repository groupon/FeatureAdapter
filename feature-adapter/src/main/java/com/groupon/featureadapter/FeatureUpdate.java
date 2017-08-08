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

import android.support.v7.util.DiffUtil.DiffResult;
import com.groupon.featureadapter.FeaturesAdapter.FeatureDataSegment;
import java.util.List;
import javax.annotation.Nullable;

public class FeatureUpdate {
  public final int featureIndex;
  @Nullable public final List<ViewItem> newItems;
  @Nullable public final DiffResult diffResult;
  @Nullable public final FeatureDataSegment dataSegment;

  public FeatureUpdate(
      int featureIndex,
      List<ViewItem> newItems,
      DiffResult diffResult,
      FeatureDataSegment dataSegment) {
    this.featureIndex = featureIndex;
    this.newItems = newItems;
    this.diffResult = diffResult;
    this.dataSegment = dataSegment;
  }
}
