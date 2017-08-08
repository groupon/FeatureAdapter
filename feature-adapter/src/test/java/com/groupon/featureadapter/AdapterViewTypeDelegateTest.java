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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;

public class AdapterViewTypeDelegateTest {

  @Test
  public void createItemComparatorForType_should_returnNonNullComparator() {
    //GIVEN

    AdapterViewTypeDelegate<?, String> stubAdapterViewTypeDelegate =
        new StubAdapterViewTypeDelegate();

    //WHEN
    final DiffUtilComparator diffUtilComparator =
        stubAdapterViewTypeDelegate.createDiffUtilComparator();

    //THEN
    assertThat(diffUtilComparator, not(nullValue()));
  }
}
