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
