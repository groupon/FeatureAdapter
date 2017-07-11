/*
 * Copyright (c) 2017, Groupon, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of GROUPON nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.groupon.featureadapter;

import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.featureadapter.events.FeatureEventListener;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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
