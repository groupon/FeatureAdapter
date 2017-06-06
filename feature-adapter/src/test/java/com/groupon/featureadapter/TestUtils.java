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

import static org.easymock.EasyMock.createMock;

import android.database.Observable;
import android.support.v7.widget.RecyclerView;
import java.lang.reflect.Field;
import java.util.ArrayList;

//from https://github.com/bignerdranch/expandable-recycler-view/blob/master/expandablerecyclerview/src/test/java/com/bignerdranch/expandablerecyclerview/TestUtils.java
//under MIT licence
public final class TestUtils {

  private TestUtils() {}

  /**
   * Fixes internal dependencies to android.database.Observable so that a RecyclerView.Adapter can
   * be tested using regular unit tests while verifying changes to the data.
   *
   * <p>Pulled from:
   * https://github.com/badoo/Chateau/blob/master/ExampleApp/src/test/java/com/badoo/chateau/example/ui/utils/TestUtils.java
   */
  public static RecyclerView.AdapterDataObserver fixAdapterForTesting(RecyclerView.Adapter adapter)
      throws NoSuchFieldException, IllegalAccessException {
    // Observables are not mocked by default so we need to hook the adapter up to an observer so we can track changes
    Field observableField = RecyclerView.Adapter.class.getDeclaredField("mObservable");
    observableField.setAccessible(true);
    Object observable = observableField.get(adapter);
    Field observersField = Observable.class.getDeclaredField("mObservers");
    observersField.setAccessible(true);
    final ArrayList<Object> observers = new ArrayList<>();
    RecyclerView.AdapterDataObserver dataObserver =
        createMock(RecyclerView.AdapterDataObserver.class);
    observers.add(dataObserver);
    observersField.set(observable, observers);
    return dataObserver;
  }
}
