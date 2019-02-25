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
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GroupAdapterViewTypeDelegateTest extends EasyMockSupport {

  @Mock Context context;

  @Test(expected = IllegalStateException.class)
  public void constructor_shouldThrowException_whenSharingDelegates() {
    // GIVEN
    AdapterViewTypeDelegate delegate = createMock(AdapterViewTypeDelegate.class);
    expect(delegate.getViewType()).andReturn(0);
    replay(delegate);

    // WHEN
    new TargetGroupAdapterViewTypeDelegate(singletonList(delegate));

    // THEN
    // expected exception
  }

  @Test
  public void constructor_shouldAssignIncrementingViewTypes_whenGivenMultipleDelegates() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates = asList(
      createValidAdapterViewTypeDelegate(0),
      createValidAdapterViewTypeDelegate(1),
      createValidAdapterViewTypeDelegate(2)
    );

    replayAll();

    // WHEN
    new TargetGroupAdapterViewTypeDelegate(delegates);

    // THEN
    verifyAll();
  }

  @Test
  public void createViewHolder_shouldUseImplementationLayoutAndViewGroup_whenGivenValidParent() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates =
      singletonList(createValidAdapterViewTypeDelegate(0));

    ViewGroup expectedParent = createNiceMock(ViewGroup.class);
    View layout = createNiceMock(View.class);
    ViewGroup rootViewGroup = createNiceMock(ViewGroup.class);

    replayAll();

    TargetGroupAdapterViewTypeDelegate target = new TargetGroupAdapterViewTypeDelegate(delegates);
    target.expectedParent = expectedParent;
    target.layout = layout;
    target.rootViewGroup = rootViewGroup;

    // WHEN
    target.createViewHolder(expectedParent);

    // THEN
    verifyAll();
  }

  @Test
  public void unbindViewHolder_shouldDoNothing_whenProcessingAFreshViewHolder() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates =
      singletonList(createValidAdapterViewTypeDelegate(0));

    // Fresh Group ViewHolder
    GroupViewHolder groupHolder = createGroupViewHolder();

    replayAll();

    TargetGroupAdapterViewTypeDelegate target = new TargetGroupAdapterViewTypeDelegate(delegates);

    // WHEN
    target.unbindViewHolder(groupHolder);

    // THEN
    verifyAll(); // no unexpected method invocations
  }

  @Test
  public void unbindViewHolder_shouldClearViewHolder_whenProcessingExistingViewHolder() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates =
      singletonList(createValidAdapterViewTypeDelegate(0));

    // Delegate already bound to single child item
    GroupViewHolder groupHolder = createGroupViewHolder();
    List<ViewItem> viewItems = singletonList(createViewItem(new ChildModel(0, ""), 0));
    List<RecyclerView.ViewHolder> childViewHolders = singletonList(createChildViewHolder("0"));
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, viewItems, childViewHolders, groupHolder);

    // EXPECT
    delegates.get(0).unbindViewHolder(childViewHolders.get(0));

    replayAll();

    // WHEN
    target.unbindViewHolder(groupHolder);

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(0));
    verifyAll();
  }

  @Test
  public void bindViewHolder_shouldCreateNewViews_whenBindingWithNoCachedViews() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates = asList(
      createValidAdapterViewTypeDelegate(0),
      createValidAdapterViewTypeDelegate(1));

    // Fresh Delegate
    GroupViewHolder groupHolder = createGroupViewHolder();
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, emptyList(), emptyList(), groupHolder);

    // New data expected to be bound to fresh Delegate
    List<RecyclerView.ViewHolder> childViewHolders = asList(
      createChildViewHolder("0"),
      createChildViewHolder("1")
    );
    List<ViewItem> viewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(1, ""), 1)
    );

    // EXPECT
    expect(delegates.get(0).createViewHolder(groupHolder.viewGroup)).andReturn(childViewHolders.get(0));
    expect(delegates.get(1).createViewHolder(groupHolder.viewGroup)).andReturn(childViewHolders.get(1));
    delegates.get(0).bindViewHolder(childViewHolders.get(0), viewItems.get(0).model);
    delegates.get(1).bindViewHolder(childViewHolders.get(1), viewItems.get(1).model);

    replayAll();

    // WHEN
    target.bindViewHolder(groupHolder, viewItems);

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(2));
    assertThat(groupHolder.viewGroup.getChildAt(0), is(childViewHolders.get(0).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(1), is(childViewHolders.get(1).itemView));
    verifyAll();
  }

  @Test
  public void bindViewHolder_shouldCacheViewsByType_whenRebindingViewHolder() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates = asList(
      createValidAdapterViewTypeDelegate(0),
      createValidAdapterViewTypeDelegate(1));

    GroupViewHolder groupHolder = createGroupViewHolder();
    List<ViewItem> viewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(1, ""), 1),
      createViewItem(new ChildModel(2, ""), 0),
      createViewItem(new ChildModel(3, ""), 1)
    );
    List<RecyclerView.ViewHolder> childViewHolders = asList(
      createChildViewHolder("viewType 0 index 0"),
      createChildViewHolder("viewType 1 index 1"),
      createChildViewHolder("viewType 0 index 2"),
      createChildViewHolder("viewType 1 index 3")
    );

    // Delegate already bound to multiple child items of different types
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, viewItems, childViewHolders, groupHolder);

    // EXPECT
    delegates.get(0).unbindViewHolder(childViewHolders.get(0));
    delegates.get(1).unbindViewHolder(childViewHolders.get(1));
    delegates.get(0).unbindViewHolder(childViewHolders.get(2));
    delegates.get(1).unbindViewHolder(childViewHolders.get(3));

    delegates.get(0).bindViewHolder(childViewHolders.get(0), viewItems.get(0).model);
    delegates.get(1).bindViewHolder(childViewHolders.get(1), viewItems.get(1).model);
    delegates.get(0).bindViewHolder(childViewHolders.get(2), viewItems.get(2).model);
    delegates.get(1).bindViewHolder(childViewHolders.get(3), viewItems.get(3).model);

    replayAll();

    // WHEN
    target.bindViewHolder(groupHolder, viewItems);

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(4));
    assertThat(groupHolder.viewGroup.getChildAt(0), is(childViewHolders.get(0).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(1), is(childViewHolders.get(1).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(2), is(childViewHolders.get(2).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(3), is(childViewHolders.get(3).itemView));
    verifyAll();
  }

  @Test
  public void bindViewHolderPayload_shouldCacheViewsByType_whenRebindingViewHolder_andNullPayloads() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates = asList(
      createValidAdapterViewTypeDelegate(0),
      createValidAdapterViewTypeDelegate(1));

    GroupViewHolder groupHolder = createGroupViewHolder();
    List<ViewItem> viewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(1, ""), 1),
      createViewItem(new ChildModel(2, ""), 0),
      createViewItem(new ChildModel(3, ""), 1)
    );
    List<RecyclerView.ViewHolder> childViewHolders = asList(
      createChildViewHolder("viewType 0 index 0"),
      createChildViewHolder("viewType 1 index 1"),
      createChildViewHolder("viewType 0 index 2"),
      createChildViewHolder("viewType 1 index 3")
    );

    // Delegate already bound to multiple child items of different types
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, viewItems, childViewHolders, groupHolder);

    // EXPECT
    delegates.get(0).unbindViewHolder(childViewHolders.get(0));
    delegates.get(1).unbindViewHolder(childViewHolders.get(1));
    delegates.get(0).unbindViewHolder(childViewHolders.get(2));
    delegates.get(1).unbindViewHolder(childViewHolders.get(3));

    delegates.get(0).bindViewHolder(childViewHolders.get(0), viewItems.get(0).model);
    delegates.get(1).bindViewHolder(childViewHolders.get(1), viewItems.get(1).model);
    delegates.get(0).bindViewHolder(childViewHolders.get(2), viewItems.get(2).model);
    delegates.get(1).bindViewHolder(childViewHolders.get(3), viewItems.get(3).model);

    replayAll();

    // WHEN
    target.bindViewHolder(groupHolder, viewItems, null); // NULL payload

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(4));
    assertThat(groupHolder.viewGroup.getChildAt(0), is(childViewHolders.get(0).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(1), is(childViewHolders.get(1).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(2), is(childViewHolders.get(2).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(3), is(childViewHolders.get(3).itemView));
    verifyAll();
  }

  @Test
  public void bindViewHolderPayload_shouldCacheViewsByType_whenRebindingViewHolder_andEmptyPayloads() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates = asList(
      createValidAdapterViewTypeDelegate(0),
      createValidAdapterViewTypeDelegate(1));

    GroupViewHolder groupHolder = createGroupViewHolder();
    List<ViewItem> viewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(1, ""), 1),
      createViewItem(new ChildModel(2, ""), 0),
      createViewItem(new ChildModel(3, ""), 1)
    );
    List<RecyclerView.ViewHolder> childViewHolders = asList(
      createChildViewHolder("viewType 0 index 0"),
      createChildViewHolder("viewType 1 index 1"),
      createChildViewHolder("viewType 0 index 2"),
      createChildViewHolder("viewType 1 index 3")
    );

    // Delegate already bound to multiple child items of different types
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, viewItems, childViewHolders, groupHolder);

    // EXPECT
    delegates.get(0).unbindViewHolder(childViewHolders.get(0));
    delegates.get(1).unbindViewHolder(childViewHolders.get(1));
    delegates.get(0).unbindViewHolder(childViewHolders.get(2));
    delegates.get(1).unbindViewHolder(childViewHolders.get(3));

    delegates.get(0).bindViewHolder(childViewHolders.get(0), viewItems.get(0).model);
    delegates.get(1).bindViewHolder(childViewHolders.get(1), viewItems.get(1).model);
    delegates.get(0).bindViewHolder(childViewHolders.get(2), viewItems.get(2).model);
    delegates.get(1).bindViewHolder(childViewHolders.get(3), viewItems.get(3).model);

    replayAll();

    // WHEN
    target.bindViewHolder(groupHolder, viewItems, emptyList()); // EMPTY payload

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(4));
    assertThat(groupHolder.viewGroup.getChildAt(0), is(childViewHolders.get(0).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(1), is(childViewHolders.get(1).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(2), is(childViewHolders.get(2).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(3), is(childViewHolders.get(3).itemView));
    verifyAll();
  }

  @Test
  public void bindViewHolderPayload_shouldInsertItemsInCorrectPosition_whenItemsInsertedTogether() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates =
      singletonList(createValidAdapterViewTypeDelegate(0));

    GroupViewHolder groupHolder = createGroupViewHolder();
    List<ViewItem> viewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(3, ""), 0),
      createViewItem(new ChildModel(4, ""), 0)
    );
    List<RecyclerView.ViewHolder> childViewHolders = asList(
      createChildViewHolder("0"),
      createChildViewHolder("3"),
      createChildViewHolder("4")
    );

    // Delegate already bound to multiple child items
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, viewItems, childViewHolders, groupHolder);

    // New state for Group ViewHolder with 2 inserted items
    List<ViewItem> newViewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(1, ""), 0), // INSERTED ViewItem
      createViewItem(new ChildModel(2, ""), 0), // INSERTED ViewItem
      createViewItem(new ChildModel(3, ""), 0),
      createViewItem(new ChildModel(4, ""), 0)
    );
    List<RecyclerView.ViewHolder> newChildViewHolders = asList(
      childViewHolders.get(0),
      createChildViewHolder("1 INSERTED"), // INSERTED child ViewHolder
      createChildViewHolder("2 INSERTED"), // INSERTED child ViewHolder
      childViewHolders.get(1),
      childViewHolders.get(2)
    );

    // EXPECT
    expect(delegates.get(0).createViewHolder(groupHolder.viewGroup)).andReturn(newChildViewHolders.get(1)).times(1);
    expect(delegates.get(0).createViewHolder(groupHolder.viewGroup)).andReturn(newChildViewHolders.get(2)).times(1);

    delegates.get(0).bindViewHolder(newChildViewHolders.get(1), newViewItems.get(1).model);
    delegates.get(0).bindViewHolder(newChildViewHolders.get(2), newViewItems.get(2).model);

    replayAll();

    // generate payload
    Object changePayload = target.createDiffUtilComparator().getChangePayload(viewItems, newViewItems);

    // WHEN
    target.bindViewHolder(groupHolder, newViewItems, singletonList(changePayload));

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(5));
    assertThat(groupHolder.viewGroup.getChildAt(0), is(newChildViewHolders.get(0).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(1), is(newChildViewHolders.get(1).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(2), is(newChildViewHolders.get(2).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(3), is(newChildViewHolders.get(3).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(4), is(newChildViewHolders.get(4).itemView));
    verifyAll();
  }

  @Test
  public void bindViewHolderPayload_shouldInsertItemsInCorrectPosition_whenItemsInsertedWithGap() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates =
      singletonList(createValidAdapterViewTypeDelegate(0));

    GroupViewHolder groupHolder = createGroupViewHolder();
    List<ViewItem> viewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(2, ""), 0),
      createViewItem(new ChildModel(4, ""), 0)
    );
    List<RecyclerView.ViewHolder> childViewHolders = asList(
      createChildViewHolder("0"),
      createChildViewHolder("2"),
      createChildViewHolder("4")
    );

    // Delegate already bound to multiple child items
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, viewItems, childViewHolders, groupHolder);

    // New state for Group ViewHolder with 2 inserted items
    List<ViewItem> newViewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(1, "INSERTED"), 0), // INSERTED ViewItem
      createViewItem(new ChildModel(2, ""), 0),
      createViewItem(new ChildModel(3, "INSERTED"), 0), // INSERTED ViewItem
      createViewItem(new ChildModel(4, ""), 0)
    );
    List<RecyclerView.ViewHolder> newChildViewHolders = asList(
      childViewHolders.get(0),
      createChildViewHolder("1 INSERTED"), // INSERTED child ViewHolder
      childViewHolders.get(1),
      createChildViewHolder("3 INSERTED"), // INSERTED child ViewHolder
      childViewHolders.get(2)
    );

    // EXPECT
    expect(delegates.get(0).createViewHolder(groupHolder.viewGroup)).andReturn(newChildViewHolders.get(1)).times(1);
    expect(delegates.get(0).createViewHolder(groupHolder.viewGroup)).andReturn(newChildViewHolders.get(3)).times(1);

    delegates.get(0).bindViewHolder(newChildViewHolders.get(1), newViewItems.get(1).model);
    delegates.get(0).bindViewHolder(newChildViewHolders.get(3), newViewItems.get(3).model);

    replayAll();

    // generate payload
    Object changePayload = target.createDiffUtilComparator().getChangePayload(viewItems, newViewItems);

    // WHEN
    target.bindViewHolder(groupHolder, newViewItems, singletonList(changePayload));

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(5));
    assertThat(groupHolder.viewGroup.getChildAt(0), is(newChildViewHolders.get(0).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(1), is(newChildViewHolders.get(1).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(2), is(newChildViewHolders.get(2).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(3), is(newChildViewHolders.get(3).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(4), is(newChildViewHolders.get(4).itemView));
    verifyAll();
  }

  @Test
  public void bindViewHolderPayload_shouldInsertChangeAndRemoveItemsInCorrectPositions_whenItemsChangesGoCrazy() {
    // GIVEN
    List<AdapterViewTypeDelegate> delegates =
      singletonList(createValidAdapterViewTypeDelegate(0));

    GroupViewHolder groupHolder = createGroupViewHolder();
    List<ViewItem> viewItems = asList(
      createViewItem(new ChildModel(0, ""), 0),
      createViewItem(new ChildModel(1, ""), 0),
      createViewItem(new ChildModel(2, ""), 0)
    );
    List<RecyclerView.ViewHolder> childViewHolders = asList(
      createChildViewHolder("0"),
      createChildViewHolder("1"),
      createChildViewHolder("2")
    );

    // Group ViewHolder already bound to multiple child items
    TargetGroupAdapterViewTypeDelegate target = createTargetDelegateWithState(delegates, viewItems, childViewHolders, groupHolder);

    // New state for Group ViewHolder with 2 removed, 1 changed and 1 inserted items
    List<ViewItem> newViewItems = asList(
      // REMOVED ViewItem
      // REMOVED ViewItem
      createViewItem(new ChildModel(2, "CHANGED"), 0), // CHANGED ViewItem
      createViewItem(new ChildModel(3, "INSERTED"), 0) // INSERTED ViewItem
    );
    List<RecyclerView.ViewHolder> newChildViewHolders = asList(
      childViewHolders.get(2), // CHANGED child ViewHolder
      childViewHolders.get(0) // INSERTED child ViewHolder (Recycled from first removed)
    );

    // EXPECT
    //removes
    delegates.get(0).unbindViewHolder(childViewHolders.get(0));
    delegates.get(0).unbindViewHolder(childViewHolders.get(1));

    //update
    delegates.get(0).bindViewHolder(newChildViewHolders.get(0), newViewItems.get(0).model);

    // insert (using recycled child holder)
    delegates.get(0).bindViewHolder(newChildViewHolders.get(1), newViewItems.get(1).model);

    replayAll();

    // generate payload
    Object changePayload = target.createDiffUtilComparator().getChangePayload(viewItems, newViewItems);

    // WHEN
    target.bindViewHolder(groupHolder, newViewItems, singletonList(changePayload));

    // THEN
    assertThat(groupHolder.viewGroup.getChildCount(), is(2));
    assertThat(groupHolder.viewGroup.getChildAt(0), is(newChildViewHolders.get(0).itemView));
    assertThat(groupHolder.viewGroup.getChildAt(1), is(newChildViewHolders.get(1).itemView));
    verifyAll();
  }

  private AdapterViewTypeDelegate createValidAdapterViewTypeDelegate(int expectedViewType) {
    AdapterViewTypeDelegate delegate = createMock(AdapterViewTypeDelegate.class);
    expect(delegate.getViewType()).andStubReturn(RecyclerView.INVALID_TYPE);
    delegate.setViewType(expectedViewType);
    delegate.addFeatureEventListener(anyObject());
    // we build a real DiffUtilComparator so we can generate realistic DiffUtil.DiffResult
    expect(delegate.createDiffUtilComparator()).andStubReturn(new DiffUtilComparator<ChildModel>() {
      @Override
      public boolean areItemsTheSame(ChildModel oldModel, ChildModel newModel) {
        return oldModel.key == newModel.key;
      }

      @Override
      public boolean areContentsTheSame(ChildModel oldModel, ChildModel newModel) {
        return oldModel.value.equals(newModel.value);
      }

      @Override
      public Object getChangePayload(ChildModel oldModel, ChildModel newModel) {
        return null;
      }
    });
    return delegate;
  }

  private ViewItem<ChildModel> createViewItem(ChildModel model, int viewType) {
    AdapterViewTypeDelegate tempDelegate = new StubAdapterViewTypeDelegate();
    tempDelegate.setViewType(viewType);
    return new ViewItem<>(model, tempDelegate);
  }

  private RecyclerView.ViewHolder createChildViewHolder(String identity) {
    return new RecyclerView.ViewHolder(new StubView(context)) {
      @Override
      public String toString() {
        return "ChildViewHolder " + identity;
      }
    };
  }

  private GroupViewHolder createGroupViewHolder() {
    View itemLayout = createNiceMock(View.class);
    ViewGroup viewGroup = new StubViewGroup(context);
    return new GroupViewHolder(itemLayout, viewGroup);
  }

  private TargetGroupAdapterViewTypeDelegate createTargetDelegateWithState(List<AdapterViewTypeDelegate> delegates,
                                                                           List<ViewItem> viewItems,
                                                                           List<RecyclerView.ViewHolder> childViewHolders,
                                                                           GroupViewHolder groupHolder) {
    for (int i = 0; i < viewItems.size(); i++) {
      ViewItem viewItem = viewItems.get(i);
      RecyclerView.ViewHolder childViewHolder = childViewHolders.get(i);
      AdapterViewTypeDelegate delegate = delegates.get(viewItem.viewType);
      expect(delegate.createViewHolder(groupHolder.viewGroup)).andReturn(childViewHolder).times(1);
      delegate.bindViewHolder(childViewHolder, viewItem.model);
    }

    replayAll();

    TargetGroupAdapterViewTypeDelegate target = new TargetGroupAdapterViewTypeDelegate(delegates);
    target.bindViewHolder(groupHolder, viewItems);

    resetAll();

    return target;
  }

  private static class GroupViewHolder extends RecyclerView.ViewHolder {

    final ViewGroup viewGroup;

    GroupViewHolder(View itemView, ViewGroup viewGroup) {
      super(itemView);
      this.viewGroup = viewGroup;
    }

    @Override
    public String toString() {
      return "GroupViewHolder";
    }
  }

  private static class TargetGroupAdapterViewTypeDelegate extends GroupAdapterViewTypeDelegate<GroupViewHolder> {

    // to be set by @Test method implementations
    ViewGroup expectedParent;
    View layout;
    ViewGroup rootViewGroup;

    TargetGroupAdapterViewTypeDelegate(List<AdapterViewTypeDelegate> delegates) {
      super(delegates);
    }

    @Override
    public GroupViewHolder createViewHolder(ViewGroup parent) {
      if (parent != expectedParent) {
        throw new IllegalArgumentException("Did not pass correct parent");
      }
      return new GroupViewHolder(layout, rootViewGroup);
    }

    @Override
    protected ViewGroup getRootViewGroup(GroupViewHolder groupViewHolder) {
      return groupViewHolder.viewGroup;
    }
  }

  private static class ChildModel {
    final int key;
    final String value;

    ChildModel(int key, String value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String toString() {
      return "ChildModel " +
        "key=" + key +
        ", value='" + value + '\'';
    }
  }

  private static class StubView extends View {

    private Object tag;

    StubView(Context context) {
      super(context);
    }

    @Override
    public Object getTag() {
      return tag;
    }

    @Override
    public void setTag(Object tag) {
      this.tag = tag;
    }

    @Override
    public String toString() {
      return "StubView tag " + tag;
    }

  }

  private static class StubViewGroup extends ViewGroup {

    private final List<View> children = new ArrayList<>();

    StubViewGroup(Context context) {
      super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
      // no op
    }

    @Override
    public int getChildCount() {
      return children.size();
    }

    @Override
    public void addView(View child) {
      children.add(child);
    }

    @Override
    public void addView(View child, int index) {
      children.add(index, child);
    }

    @Override
    public View getChildAt(int index) {
      return children.get(index);
    }

    @Override
    public void removeViewAt(int index) {
      children.remove(index);
    }

    @Override
    public void removeAllViews() {
      children.clear();
    }

    @Override
    public String toString() {
      return "StubViewGroup childCount " + children.size();
    }
  }
}
