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

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.INVALID_TYPE;
import static android.support.v7.widget.RecyclerView.ViewHolder;
import static com.groupon.featureadapter.TestUtils.fixAdapterForTesting;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FeaturesAdapterTest {

  @Test
  public void registerFeatures_should_assignValidViewTypesToAdapterViewTypeDelegates()
      throws Exception {
    //GIVEN
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate = new StubAdapterViewTypeDelegate();
    List<FeatureController<String>> featureControllers =
        asList(new StubFeatureController<>(asList(stubAdapterViewTypeDelegate)));

    //WHEN
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    //THEN
    assertThat(stubAdapterViewTypeDelegate.getViewType(), not(INVALID_TYPE));
  }

  @Test
  public void getItemCount_should_returnAllItems() throws Exception {
    //GIVEN
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate = new StubAdapterViewTypeDelegate();
    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
        asList(new StubFeatureController<>(asList(stubAdapterViewTypeDelegate), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate));
    items.add(new ViewItem<>("a1", stubAdapterViewTypeDelegate));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    final int itemCount = featuresAdapter.getItemCount();

    //THEN
    assertThat(itemCount, is(2));
  }

  @Test
  public void
      getItemViewType_should_returnCorrectViewTypeOfAdapterViewTypeDelegateForAGivenPosition()
          throws Exception {
    //GIVEN
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate0 = new StubAdapterViewTypeDelegate();
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate1 = new StubAdapterViewTypeDelegate();
    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
        asList(
            new StubFeatureController<>(
                asList(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate0));
    items.add(new ViewItem<>("a1", stubAdapterViewTypeDelegate1));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    final int viewType0 = featuresAdapter.getItemViewType(0);
    final int viewType1 = featuresAdapter.getItemViewType(1);

    //THEN
    assertThat(viewType0, is(stubAdapterViewTypeDelegate0.getViewType()));
    assertThat(viewType1, is(stubAdapterViewTypeDelegate1.getViewType()));
  }

  @Test
  public void updateFeatureItems_should_updateOldItems() throws Exception {
    //GIVEN
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate0 = new StubAdapterViewTypeDelegate();
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate1 = new StubAdapterViewTypeDelegate();
    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
        asList(
            new StubFeatureController<>(
                asList(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate0));
    items.add(new ViewItem<>("a1", stubAdapterViewTypeDelegate1));
    featuresAdapter.updateFeatureItems("a");
    items.clear();
    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate0));
    items.add(new ViewItem<>("a1", stubAdapterViewTypeDelegate0));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    final int viewType0 = featuresAdapter.getItemViewType(0);
    final int viewType1 = featuresAdapter.getItemViewType(1);

    //THEN
    assertThat(viewType0, is(stubAdapterViewTypeDelegate0.getViewType()));
    assertThat(viewType1, is(stubAdapterViewTypeDelegate0.getViewType()));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void onCreateViewHolder_should_useTheRightAdapterViewTypeDelegateForAGivenPosition()
      throws Exception {
    //GIVEN
    final LinearLayout parent = new LinearLayout(createMock(Context.class));
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate0 =
        createNiceMock(StubAdapterViewTypeDelegate.class);
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate1 =
        createNiceMock(StubAdapterViewTypeDelegate.class);
    expect(stubAdapterViewTypeDelegate0.getViewType()).andReturn(0).anyTimes();
    expect(stubAdapterViewTypeDelegate1.getViewType()).andReturn(1).anyTimes();
    expect(stubAdapterViewTypeDelegate1.createViewHolder(anyObject(ViewGroup.class)))
        .andReturn(new ViewHolder(parent) {});
    replay(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1);

    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
        asList(
            new StubFeatureController<>(
                asList(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem("a0", stubAdapterViewTypeDelegate0));
    items.add(new ViewItem("a1", stubAdapterViewTypeDelegate1));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    featuresAdapter.onCreateViewHolder(parent, 1);

    //THEN
    verify(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1);
  }

  @Test
  public void onBindViewHolder_should_useTheRightAdapterViewTypeDelegateForAGivenPosition()
      throws Exception {
    //GIVEN
    final LinearLayout parent = new LinearLayout(createMock(Context.class));
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate0 =
        createNiceMock(StubAdapterViewTypeDelegate.class);
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate1 =
        createNiceMock(StubAdapterViewTypeDelegate.class);
    expect(stubAdapterViewTypeDelegate0.getViewType()).andReturn(0).anyTimes();
    expect(stubAdapterViewTypeDelegate1.getViewType()).andReturn(1).anyTimes();
    stubAdapterViewTypeDelegate1.bindViewHolder(anyObject(ViewHolder.class), eq("a1"));
    replay(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1);

    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
        singletonList(
            new StubFeatureController<>(
                asList(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate0));
    items.add(new ViewItem<>("a1", stubAdapterViewTypeDelegate1));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    featuresAdapter.onBindViewHolder(new ViewHolder(parent) {}, 1);

    //THEN
    verify(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1);
  }

  @Test
  public void
      onBindViewHolder_with_payload_should_useTheRightAdapterViewTypeDelegateForAGivenPosition()
          throws Exception {
    //GIVEN
    final LinearLayout parent = new LinearLayout(createMock(Context.class));
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate0 =
        createNiceMock(StubAdapterViewTypeDelegate.class);
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate1 =
        createNiceMock(StubAdapterViewTypeDelegate.class);
    expect(stubAdapterViewTypeDelegate0.getViewType()).andReturn(0).anyTimes();
    expect(stubAdapterViewTypeDelegate1.getViewType()).andReturn(1).anyTimes();
    stubAdapterViewTypeDelegate1.bindViewHolder(
        anyObject(ViewHolder.class), eq("a1"), eq(singletonList("payload")));
    replay(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1);

    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
        singletonList(
            new StubFeatureController<>(
                asList(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate0));
    items.add(new ViewItem<>("a1", stubAdapterViewTypeDelegate1));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    featuresAdapter.onBindViewHolder(new ViewHolder(parent) {}, 1, singletonList("payload"));

    //THEN
    verify(stubAdapterViewTypeDelegate0, stubAdapterViewTypeDelegate1);
  }

  @Test
  public void registerFeatures_should_callTheBindErrorDelegateWhenThisIsSetAndAnExceptionOccurs()
          throws Exception {

    //GIVEN
    final LinearLayout parent = new LinearLayout(createMock(Context.class));
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate =
            createNiceMock(StubAdapterViewTypeDelegate.class);
    expect(stubAdapterViewTypeDelegate.getViewType()).andReturn(0).anyTimes();

    stubAdapterViewTypeDelegate.bindViewHolder(
            anyObject(ViewHolder.class), eq("a0"), eq(singletonList("payload")));
    expectLastCall().andThrow(new NullPointerException());

    replay(stubAdapterViewTypeDelegate);

    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
            singletonList(
                    new StubFeatureController<>(
                            asList(stubAdapterViewTypeDelegate), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    BindErrorHandler bindErrorHandler = createMock(BindErrorHandler.class);
    bindErrorHandler.handleBindError(anyObject(Throwable.class), eq(0));
    featuresAdapter.setBindErrorHandler(bindErrorHandler);
    replay(bindErrorHandler);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    featuresAdapter.onBindViewHolder(new ViewHolder(parent) {}, 0, singletonList("payload"));


    //THEN
    verify(bindErrorHandler, stubAdapterViewTypeDelegate);
  }

  @Test(expected = NullPointerException.class)
  public void registerFeatures_should_throwExceptionWhenBindErrorDelegateIsNotSetAndAnExceptionOccurs()
          throws Exception {

    //GIVEN
    final LinearLayout parent = new LinearLayout(createMock(Context.class));
    StubAdapterViewTypeDelegate stubAdapterViewTypeDelegate =
            createNiceMock(StubAdapterViewTypeDelegate.class);
    expect(stubAdapterViewTypeDelegate.getViewType()).andReturn(0).anyTimes();

    stubAdapterViewTypeDelegate.bindViewHolder(
            anyObject(ViewHolder.class), eq("a0"), eq(singletonList("payload")));
    expectLastCall().andThrow(new NullPointerException());

    replay(stubAdapterViewTypeDelegate);

    List<ViewItem> items = new ArrayList<>();
    List<FeatureController<String>> featureControllers =
            singletonList(
                    new StubFeatureController<>(
                            asList(stubAdapterViewTypeDelegate), items));
    FeaturesAdapter<String> featuresAdapter = new FeaturesAdapter<>(featureControllers);
    fixAdapterForTesting(featuresAdapter);

    items.add(new ViewItem<>("a0", stubAdapterViewTypeDelegate));
    featuresAdapter.updateFeatureItems("a");

    //WHEN
    featuresAdapter.onBindViewHolder(new ViewHolder(parent) {}, 0, singletonList("payload"));


    //THEN
    verify(stubAdapterViewTypeDelegate);
  }
}
