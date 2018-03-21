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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DefaultDiffUtilComparatorTest {

  @Test
  public void areItemsTheSame_should_alwaysReturnTrue() throws Exception {
    //GIVEN
    DefaultDiffUtilComparator<String> itemComparatorUnderTest = new DefaultDiffUtilComparator<>();

    //WHEN
    final boolean areItemsTheSame = itemComparatorUnderTest.areItemsTheSame("a", "b");

    //THEN
    assertThat(areItemsTheSame, is(true));
  }

  @Test
  public void areContentsTheSame_should_returnFalse_when_Not_equals() throws Exception {
    //funny but we can't actually mock equals with any mock lib !
    //SOF: https://stackoverflow.com/q/3007532/693752
    //GIVEN
    DefaultDiffUtilComparator<String> itemComparatorUnderTest = new DefaultDiffUtilComparator<>();

    //WHEN
    final boolean areContentsTheSame = itemComparatorUnderTest.areContentsTheSame("a", "b");

    //THEN
    assertThat(areContentsTheSame, is(false));
  }

  @Test
  public void areContentsTheSame_should_returnTrue_when_equals() throws Exception {
    //funny but we can't actually mock equals with any mock lib !
    //SOF: https://stackoverflow.com/q/3007532/693752
    //GIVEN
    DefaultDiffUtilComparator<String> itemComparatorUnderTest = new DefaultDiffUtilComparator<>();

    //WHEN
    final boolean areContentsTheSame = itemComparatorUnderTest.areContentsTheSame("a", "a");

    //THEN
    assertThat(areContentsTheSame, is(true));
  }
}
