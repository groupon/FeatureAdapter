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

import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static java.util.Collections.singletonList;

/**
 * A wrapper AdapterViewTypeDelegate that wraps 1 to many child AdapterViewTypeDelegates.
 * Enables the child delegates to continue to use DiffUtilComparator and view Recycling by type.
 */
public abstract class GroupAdapterViewTypeDelegate<HOLDER extends RecyclerView.ViewHolder> extends AdapterViewTypeDelegate<HOLDER, List<ViewItem>> {

  private final List<AdapterViewTypeDelegate> childAdapterViewTypeDelegates = new ArrayList<>();
  private final List<DiffUtilComparator> childDiffUtilComparators = new ArrayList<>();
  private final List<Queue<RecyclerView.ViewHolder>> childViewHolderCache = new ArrayList<>();

  private final List<Operation> operations = new ArrayList<>(); // small memory optimisation

  public GroupAdapterViewTypeDelegate(List<AdapterViewTypeDelegate> delegates) {
    childAdapterViewTypeDelegates.addAll(delegates);
    int childViewType = 0;
    for (AdapterViewTypeDelegate delegate : childAdapterViewTypeDelegates) {
      if (delegate.getViewType() != RecyclerView.INVALID_TYPE) {
        throw new IllegalStateException("Do not reuse AdapterViewTypeDelegate instances");
      }
      delegate.setViewType(childViewType++);
      delegate.addFeatureEventListener(this::fireEvent);
      childDiffUtilComparators.add(delegate.createDiffUtilComparator());
      childViewHolderCache.add(new ArrayDeque<>());
    }
  }

  protected abstract ViewGroup getRootViewGroup(HOLDER holder);

  @Override
  public void bindViewHolder(HOLDER holder, List<ViewItem> viewItems) {
    unbindViewHolder(holder);
    for (ViewItem viewItem : viewItems) {
      final RecyclerView.ViewHolder childViewHolder = getChildViewHolder(viewItem.viewType, getRootViewGroup(holder));
      final AdapterViewTypeDelegate delegate = childAdapterViewTypeDelegates.get(viewItem.viewType);
      delegate.bindViewHolder(childViewHolder, viewItem.model);
      setChildViewState(childViewHolder, viewItem);
      getRootViewGroup(holder).addView(childViewHolder.itemView);
    }
  }

  @Override
  public void bindViewHolder(HOLDER holder, List<ViewItem> viewItems, List<Object> payloads) {
    if (payloads == null || payloads.isEmpty()) {
      bindViewHolder(holder, viewItems);
      return;
    }

    // Build list of operations to match old view items
    operations.clear();
    ViewGroup rootViewGroup = getRootViewGroup(holder);
    for (int i = 0; i < rootViewGroup.getChildCount(); i++) {
      operations.add(Operation.nilOperation());
    }

    // Iterate diff result instructions to update operations to match new view items
    DiffUtil.DiffResult diffResult = (DiffUtil.DiffResult) payloads.get(0);
    diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
      @Override
      public void onInserted(int position, int count) {
        for (int i = position; i < position + count; i++) {
          operations.add(i, Operation.addOperation());
        }
      }

      @Override
      public void onRemoved(int position, int count) {
        for (int i = position; i < position + count; i++) {
          operations.remove(position);
          unbindChildView(rootViewGroup.getChildAt(position));
          rootViewGroup.removeViewAt(position);
        }
      }

      @Override
      public void onChanged(int position, int count, Object payload) {
        for (int i = position; i < position + count; i++) {
          operations.set(i, Operation.changeOperation(payload));
        }
      }

      @Override
      public void onMoved(int fromPosition, int toPosition) {
        // not detected
      }
    });

    // Operations now match index for index with new view items
    // Apply operations to old view state to bring up to date
    for (int i = 0; i < operations.size(); i++) {
      Operation operation = operations.get(i);
      ViewItem viewItem = viewItems.get(i);
      AdapterViewTypeDelegate delegate = childAdapterViewTypeDelegates.get(viewItem.viewType);
      switch (operation.type) {
        case Operation.ADD:
          RecyclerView.ViewHolder childViewHolder = getChildViewHolder(viewItem.viewType, rootViewGroup);
          delegate.bindViewHolder(childViewHolder, viewItem.model);
          rootViewGroup.addView(childViewHolder.itemView, i);
          setChildViewState(childViewHolder, viewItem);
          break;

        case Operation.CHANGE:
          ChildViewState childViewState = getChildViewState(rootViewGroup.getChildAt(i));
          if (operation.payload != null) {
            delegate.bindViewHolder(childViewState.childViewHolder, viewItem.model, singletonList(operation.payload));
          } else {
            delegate.bindViewHolder(childViewState.childViewHolder, viewItem.model);
          }
          setChildViewState(childViewState.childViewHolder, viewItem);
          break;
      }
    }
  }

  private void unbindChildView(View childItemView) {
    ChildViewState childViewState = getChildViewState(childItemView);
    AdapterViewTypeDelegate delegate = childAdapterViewTypeDelegates.get(childViewState.viewItem.viewType);
    delegate.unbindViewHolder(childViewState.childViewHolder);
    childViewHolderCache.get(childViewState.viewItem.viewType).offer(childViewState.childViewHolder);
    childViewState.clearViewState();
  }

  @Override
  public void unbindViewHolder(HOLDER holder) {
    ViewGroup rootViewGroup = getRootViewGroup(holder);
    if (rootViewGroup.getChildCount() == 0) {
      return;
    }

    // clear / cache child views
    for (int i = 0; i < rootViewGroup.getChildCount(); i++) {
      unbindChildView(rootViewGroup.getChildAt(i));
    }

    // clear the view holder
    rootViewGroup.removeAllViews();
  }

  @Override
  public DiffUtilComparator<List<ViewItem>> createDiffUtilComparator() {
    return new GroupDiffUtilComparator(childDiffUtilComparators);
  }

  private RecyclerView.ViewHolder getChildViewHolder(int viewType, ViewGroup parent) {
    final Queue<RecyclerView.ViewHolder> viewHolders = childViewHolderCache.get(viewType);
    if (!viewHolders.isEmpty()) {
      return viewHolders.poll();
    }
    RecyclerView.ViewHolder childViewHolder = childAdapterViewTypeDelegates.get(viewType).createViewHolder(parent);
    childViewHolder.itemView.setTag(new ChildViewState());
    return childViewHolder;
  }

  private ChildViewState getChildViewState(View childItemView) {
    return (ChildViewState) childItemView.getTag();
  }

  private void setChildViewState(RecyclerView.ViewHolder childViewHolder, ViewItem viewItem) {
    ChildViewState childViewState = getChildViewState(childViewHolder.itemView);
    childViewState.childViewHolder = childViewHolder;
    childViewState.viewItem = viewItem;
  }

  /**
   * View State stored against child views
   */
  private static class ChildViewState {
    ViewItem viewItem;
    RecyclerView.ViewHolder childViewHolder;

    void clearViewState() {
      viewItem = null;
      childViewHolder = null;
    }
  }

  /**
   * We must buffer DiffUtil.DiffResult operations in order to be able to match an operation
   * to its ViewItem.
   */
  private static class Operation {
    static final String NIL = "NIL";
    static final String ADD = "ADD";
    static final String CHANGE = "CHANGE";

    private static final Operation NIL_INSTANCE = new Operation(NIL, null);
    private static final Operation ADD_INSTANCE = new Operation(ADD, null);
    private static final Operation FULL_CHANGE_INSTANCE = new Operation(CHANGE, null);

    final String type;
    final Object payload;

    static Operation nilOperation() {
      return NIL_INSTANCE;
    }

    static Operation addOperation() {
      return ADD_INSTANCE;
    }

    static Operation changeOperation(Object payload) {
      return payload == null ? FULL_CHANGE_INSTANCE : new Operation(CHANGE, payload);
    }

    private Operation(String type, Object payload) {
      this.type = type;
      this.payload = payload;
    }
  }
}
