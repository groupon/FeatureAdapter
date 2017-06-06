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

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An adapter of a {@link RecyclerView} that is based on features. Each feature is described a
 * {@link FeatureController} and its associated list of {@link ViewItem}.
 *
 * @param <MODEL> the input model of the {@link FeatureController}s.
 */
public class FeaturesAdapter<MODEL> extends RecyclerView.Adapter<ViewHolder> {

  private final List<FeatureController<MODEL>> featureControllers = new ArrayList<>();
  private final List<FeatureDataSegment> featureDataSegments = new ArrayList<>();
  private final List<ViewItem> items = new ArrayList<>();
  private final Map<Integer, AdapterViewTypeDelegate> mapViewTypeToAdapterViewTypeDelegate =
      new HashMap<>();
  private final Map<Integer, DiffUtilComparator> mapViewTypeToItemComparator = new HashMap<>();

  /**
   * Setup method to install a list of feature controllers into the adapter.
   *
   * @param featureControllers the list of feature controllers to install in this adapter.
   */
  public FeaturesAdapter(List<FeatureController<MODEL>> featureControllers) {
    for (FeatureController featureController : featureControllers) {
      this.featureControllers.add(featureController);
      featureDataSegments.add(new FeatureDataSegment());
      final Collection<AdapterViewTypeDelegate> adapterViewTypeDelegateList =
          featureController.getAdapterViewTypeDelegates();
      registerAdapterViewTypeDelegates(adapterViewTypeDelegateList);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return mapViewTypeToAdapterViewTypeDelegate.get(viewType).createViewHolder(parent);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final ViewItem item = items.get(position);
    //noinspection unchecked
    mapViewTypeToAdapterViewTypeDelegate.get(item.viewType).bindViewHolder(holder, item.model);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  @Override
  public int getItemViewType(int position) {
    return items.get(position).viewType;
  }

  @Override
  public void onViewAttachedToWindow(ViewHolder holder) {
    //noinspection unchecked
    mapViewTypeToAdapterViewTypeDelegate.get(holder.getItemViewType()).onAttachToWindow(holder);
  }

  @Override
  public void onViewDetachedFromWindow(ViewHolder holder) {
    //noinspection unchecked
    mapViewTypeToAdapterViewTypeDelegate.get(holder.getItemViewType()).onDetachToWindow(holder);
  }

  @Override
  public void onViewRecycled(ViewHolder holder) {
    //noinspection unchecked
    mapViewTypeToAdapterViewTypeDelegate.get(holder.getItemViewType()).unbindViewHolder(holder);
  }

  /**
   * Updates the whole list of all items associated with all {@link FeatureController}.
   *
   * @param model the input model of all {@link FeatureController} of {@code
   *     featureControllerGroup}.
   */
  @SuppressWarnings("WeakerAccess")
  public void updateFeatureItems(MODEL model) {
    int featureIndex = 0;
    for (FeatureController<MODEL> featureController : featureControllers) {
      updateFeatureItems(toFeatureUpdate(model, featureIndex++, featureController));
    }
  }

  @SuppressWarnings("WeakerAccess")
  /* Visible for Rx module. */
  FeatureUpdate toFeatureUpdate(
      MODEL model, int featureIndex, FeatureController featureController) {
    final List<ViewItem> newItems = featureController.buildItems(model);
    FeatureDataSegment featureDataSegment = null;
    DiffUtil.DiffResult diffResult = null;
    if (newItems != null) {
      featureDataSegment = featureDataSegments.get(featureIndex);
      List<? extends ViewItem> oldItems = featureDataSegment.items;
      diffResult = computeDiffResult(oldItems, newItems);
    }
    return new FeatureUpdate(featureIndex, newItems, diffResult, featureDataSegment);
  }

  /**
   * Updates the list of items associated with a {@code featureController} with a new list of {@link
   * ViewItem}s. DiffUtil is used to update the recycler view and animate the update on the list of
   * items. <em>Warning</em>: Must be called on UI Thread.
   *
   * @param featureUpdate the update to apply.
   */
  @SuppressWarnings("WeakerAccess")
  /* Visible for Rx module. */
  void updateFeatureItems(@NonNull FeatureUpdate featureUpdate) {
    if (featureUpdate.newItems == null) {
      return;
    }
    //needs UI thread because we use the items list that is read on UI Thread
    //and anyway the list can't be modified in a thread pool like computation
    final int offset = computeFeatureOffset(featureUpdate.featureIndex);
    updateItems(offset, featureUpdate.dataSegment, featureUpdate.newItems);
    //needs UI thread to update UI
    dispatch(featureUpdate.diffResult, offset);
  }

  @SuppressWarnings("WeakerAccess")
  /* Visible for Rx module. */
  List<FeatureController<MODEL>> getFeatureControllers() {
    return featureControllers;
  }

  private DiffUtil.DiffResult computeDiffResult(
      List<? extends ViewItem> oldItems, List<? extends ViewItem> newItems) {
    // notify items changed/inserted/removed
    return DiffUtil.calculateDiff(
        new DiffUtilCallbackImpl(mapViewTypeToItemComparator, oldItems, newItems), false);
  }

  private void updateItems(
      int offset, FeatureDataSegment dataSegment, @NonNull List<? extends ViewItem> newItems) {
    // removeFeatureEventListener old items
    if (!dataSegment.items.isEmpty()) {
      for (int index = 0; index < dataSegment.items.size(); index++) {
        items.remove(offset);
      }
    }

    // addFeatureEventListener new items
    if (!newItems.isEmpty()) {
      items.addAll(offset, newItems);
    }
    dataSegment.items = newItems;
  }

  private void dispatch(DiffUtil.DiffResult diffResult, int offset) {
    if (diffResult == null) {
      return;
    }
    diffResult.dispatchUpdatesTo(new ListUpdateCallbackImpl(this, offset));
  }

  private int computeFeatureOffset(int featureIndex) {
    int offset = 0;
    for (int indexRange = 0; indexRange < featureIndex; indexRange++) {
      offset += featureDataSegments.get(indexRange).items.size();
    }
    return offset;
  }

  private void registerAdapterViewTypeDelegates(
      Collection<AdapterViewTypeDelegate> adapterViewTypeDelegates) {
    for (AdapterViewTypeDelegate adapterViewTypeDelegate : adapterViewTypeDelegates) {
      int viewType = mapViewTypeToAdapterViewTypeDelegate.size();

      //register the binder itself
      adapterViewTypeDelegate.setViewType(viewType);
      mapViewTypeToAdapterViewTypeDelegate.put(viewType, adapterViewTypeDelegate);

      //register its comparator
      DiffUtilComparator diffUtilComparator = adapterViewTypeDelegate.createDiffUtilComparator();
      mapViewTypeToItemComparator.put(viewType, diffUtilComparator);
    }
  }

  public static class FeatureDataSegment {
    List<? extends ViewItem> items = new ArrayList<>();
  }

  /**
   * Callback of DiffUtil to compare items. It uses the {@link FeatureController}s' {@link
   * DiffUtilComparator} to do so.
   */
  private static class DiffUtilCallbackImpl extends DiffUtil.Callback {

    private final Map<Integer, DiffUtilComparator> mapViewTypeToItemComparator;
    private final List<? extends ViewItem> oldList;
    private final List<? extends ViewItem> newList;

    DiffUtilCallbackImpl(
        Map<Integer, DiffUtilComparator> mapViewTypeToItemComparator,
        List<? extends ViewItem> oldList,
        List<? extends ViewItem> newList) {
      this.mapViewTypeToItemComparator = mapViewTypeToItemComparator;
      this.oldList = oldList;
      this.newList = newList;
    }

    @Override
    public int getOldListSize() {
      return oldList.size();
    }

    @Override
    public int getNewListSize() {
      return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
      final ViewItem oldItem = oldList.get(oldItemPosition);
      final ViewItem newItem = newList.get(newItemPosition);
      // noinspection unchecked
      return oldItem.viewType == newItem.viewType
          && mapViewTypeToItemComparator
              .get(oldItem.viewType)
              .areItemsTheSame(oldItem.model, newItem.model);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
      final ViewItem oldItem = oldList.get(oldItemPosition);
      final ViewItem newItem = newList.get(newItemPosition);
      // noinspection unchecked
      return mapViewTypeToItemComparator
          .get(oldItem.viewType)
          .areContentsTheSame(oldItem.model, newItem.model);
    }
  }

  /**
   * Propagates the changes to an adapter, and shifts all position by a given offset (the offset of
   * the feature controller).
   */
  private static class ListUpdateCallbackImpl implements ListUpdateCallback {

    private final RecyclerView.Adapter adapter;
    private final int offset;

    ListUpdateCallbackImpl(RecyclerView.Adapter adapter, int offset) {
      this.adapter = adapter;
      this.offset = offset;
    }

    @Override
    public void onInserted(int position, int count) {
      adapter.notifyItemRangeInserted(position + offset, count);
    }

    @Override
    public void onRemoved(int position, int count) {
      adapter.notifyItemRangeRemoved(position + offset, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
      // not detected
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
      adapter.notifyItemRangeChanged(position + offset, count, payload);
    }
  }
}
