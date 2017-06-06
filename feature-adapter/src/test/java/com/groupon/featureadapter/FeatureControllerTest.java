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
        new StubFeatureController(asList(stubAdapterViewTypeDelegate));
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
        new StubFeatureController(asList(stubAdapterViewTypeDelegate));
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
