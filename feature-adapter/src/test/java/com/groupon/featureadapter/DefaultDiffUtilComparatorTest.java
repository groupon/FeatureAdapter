/**
 * Copyright (c) 2017, Groupon, Inc. All rights reserved.
 *
 * <p>Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * <p>Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * <p>Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * <p>Neither the name of GROUPON nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
