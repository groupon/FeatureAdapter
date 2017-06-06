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
