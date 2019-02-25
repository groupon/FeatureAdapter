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

import static org.easymock.EasyMock.createMock;

import android.database.Observable;
import androidx.recyclerview.widget.RecyclerView;
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
