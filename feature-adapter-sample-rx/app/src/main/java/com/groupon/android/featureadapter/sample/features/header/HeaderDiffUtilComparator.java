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
package com.groupon.android.featureadapter.sample.features.header;

import com.groupon.featureadapter.DefaultDiffUtilComparator;
import java.util.Objects;

public class HeaderDiffUtilComparator extends DefaultDiffUtilComparator<HeaderModel> {
  @Override
  public Object getChangePayload(HeaderModel oldModel, HeaderModel newModel) {
    if (!hasValueChanged(oldModel.imageUrl, newModel.imageUrl)
        && hasValueChanged(oldModel.title, newModel.title)) {
      // capture when only the title has changed
      return newModel.title;
    }
    // request a full update
    return null;
  }

  private boolean hasValueChanged(String oldValue, String newValue) {
    return Objects.equals(oldValue, newValue);
  }
}
