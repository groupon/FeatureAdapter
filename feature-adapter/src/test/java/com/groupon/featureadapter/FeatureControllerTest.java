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

import static java.util.Arrays.asList;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.events.FeatureEventListener;
import org.junit.Test;

public class FeatureControllerTest {

  @Test
  public void addFeatureEventListener_should_relayTheEventsFromTheAdapterViewTypeDelegates() {
    //GIVEN
    AdapterViewTypeDelegate stubAdapterViewTypeDelegate = new StubAdapterViewTypeDelegate();
    FeatureController<String> featureController =
        new StubFeatureController<>(asList(stubAdapterViewTypeDelegate));
    FeatureEventListener mockListener = createMock(FeatureEventListener.class);
    final FeatureEvent featureEvent = new FeatureEvent() {};
    mockListener.onFeatureEvent(featureEvent);
    replay(mockListener);

    //WHEN
    featureController.addFeatureEventListener(mockListener);
    stubAdapterViewTypeDelegate.fireEvent(featureEvent);

    //THEN
    verify(mockListener);
  }

  @Test
  public void
      removeFeatureEventListener_should_stopRelayingTheEventsFromTheAdapterViewTypeDelegates() {
    //GIVEN
    AdapterViewTypeDelegate stubAdapterViewTypeDelegate = new StubAdapterViewTypeDelegate();
    FeatureController<String> featureController =
        new StubFeatureController<>(asList(stubAdapterViewTypeDelegate));
    FeatureEventListener mockListener = createMock(FeatureEventListener.class);
    final FeatureEvent featureEvent = new FeatureEvent() {};
    replay(mockListener);

    //WHEN
    featureController.addFeatureEventListener(mockListener);
    featureController.removeFeatureEventListener(mockListener);
    stubAdapterViewTypeDelegate.fireEvent(featureEvent);

    //THEN
    verify(mockListener);
  }
}
