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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.DiffResult;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.support.v7.util.DiffUtil.calculateDiff;

/**
 * An adapter of a {@link RecyclerView} that is based on features. Each feature is described a
 * {@link FeatureController} and its associated list of {@link ViewItem}.
 *
 * @param <MODEL> the input model of the {@link FeatureController}s.
 */
@SuppressWarnings("WeakerAccess")
public class FeaturesAdapter<MODEL> extends RecyclerView.Adapter<ViewHolder> {

  private final FeatureItems<MODEL> featureItems;
  private FeaturesAdapterErrorHandler featuresAdapterErrorHandler;
  private final Map<Integer, AdapterViewTypeDelegate> mapViewTypeToAdapterViewTypeDelegate =
      new HashMap<>();
  private final Map<Integer, DiffUtilComparator> mapViewTypeToItemComparator = new HashMap<>();

  /**
   * Setup method to install a list of feature controllers into the adapter.
   *
   * @param featureControllers the list of feature controllers to install in this adapter.
   */
  public FeaturesAdapter(List<FeatureController<MODEL>> featureControllers) {
    featureItems = new FeatureItems<>(featureControllers);
    registerAdapterViewTypeDelegates(featureItems.getFeatureControllers());
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return mapViewTypeToAdapterViewTypeDelegate.get(viewType).createViewHolder(parent);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final ViewItem item = featureItems.get(position);
    //noinspection unchecked
    mapViewTypeToAdapterViewTypeDelegate.get(item.viewType).bindViewHolder(holder, item.model);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
    final ViewItem item = featureItems.get(position);
    AdapterViewTypeDelegate adapterViewTypeDelegate = mapViewTypeToAdapterViewTypeDelegate.get(item.viewType);

    try {
      //noinspection unchecked
      adapterViewTypeDelegate.bindViewHolder(holder, item.model, payloads);

    }catch (Exception exception){
      if (featuresAdapterErrorHandler != null){
        featuresAdapterErrorHandler.onBindViewHolderError(exception, position);
      }else{
        throw exception;
      }
    }
  }

  @Override
  public int getItemCount() {
    return featureItems.size();
  }

  @Override
  public int getItemViewType(int position) {
    return featureItems.get(position).viewType;
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
    for (FeatureController<MODEL> featureController : featureItems.getFeatureControllers()) {
      dispatchFeatureUpdate(toFeatureUpdate(featureController, model));
    }
  }

  /* Visible for Rx module. */
  @Nullable
  FeatureUpdate toFeatureUpdate(FeatureController<MODEL> featureController, MODEL model) {
    final List<ViewItem> oldItems = featureItems.getItems(featureController);
    final List<ViewItem> newItems = featureController.buildItems(model);
    if (newItems == null) {
      return null;
    }
    final DiffUtilCallbackImpl callback =
        new DiffUtilCallbackImpl(mapViewTypeToItemComparator, oldItems, newItems);
    final DiffResult diffResult = calculateDiff(callback, false);
    return new FeatureUpdate(featureController, newItems, diffResult);
  }

  /* Visible for Rx module. */
  List<FeatureUpdate> dispatchFeatureUpdates(@NonNull List<FeatureUpdate> featureUpdates) {
    for (FeatureUpdate featureUpdate : featureUpdates) {
      dispatchFeatureUpdate(featureUpdate);
    }
    return featureUpdates;
  }

  /* Visible for Rx module. */
  FeatureUpdate dispatchFeatureUpdate(@Nullable FeatureUpdate featureUpdate) {
    if (featureUpdate == null) {
      return null;
    }
    // noinspection unchecked
    final int offset =
        featureItems.setItemsAndGetOffset(featureUpdate.newItems, featureUpdate.featureController);
    featureUpdate.diffResult.dispatchUpdatesTo(new ListUpdateCallbackImpl(this, offset));
    return featureUpdate;
  }

  /* Visible for Rx module. */
  List<FeatureController<MODEL>> getFeatureControllers() {
    return featureItems.getFeatureControllers();
  }

  /**
   * Returns the position of the first view item for a given view type
   *
   * @param adapterViewTypeDelegate of the feature views.
   * @return the first position of the view item defined by the given adapterViewTypeDelegate
   */
  @SuppressWarnings("unused")
  public int getFirstItemPositionForAdapterViewTypeDelegate(AdapterViewTypeDelegate adapterViewTypeDelegate) {
    int indexViewItem = 0;

    for (Iterator iterator = this.featureItems.iterator(); iterator.hasNext(); ++indexViewItem) {
      ViewItem recyclerViewItem = (ViewItem) iterator.next();
      if (recyclerViewItem.viewType == adapterViewTypeDelegate.getViewType()) {
        return indexViewItem;
      }
    }

    return -1;
  }

  private void registerAdapterViewTypeDelegates(List<FeatureController<MODEL>> featureControllers) {
    for (FeatureController<MODEL> featureController : featureControllers) {
      for (AdapterViewTypeDelegate delegate : featureController.getAdapterViewTypeDelegates()) {
        // assign unique view type
        delegate.setViewType(mapViewTypeToAdapterViewTypeDelegate.size());
        // register delegate
        mapViewTypeToAdapterViewTypeDelegate.put(delegate.getViewType(), delegate);
        // register item comparator
        mapViewTypeToItemComparator.put(
            delegate.getViewType(), delegate.createDiffUtilComparator());
      }
    }
  }

  public FeaturesAdapterErrorHandler getFeaturesAdapterErrorHandler() {
    return featuresAdapterErrorHandler;
  }

  public void setFeaturesAdapterErrorHandler(FeaturesAdapterErrorHandler featuresAdapterErrorHandler) {
    this.featuresAdapterErrorHandler = featuresAdapterErrorHandler;
  }

  public AdapterViewTypeDelegate getAdapterViewTypeDelegateForViewHolder(ViewHolder viewHolder){
    return mapViewTypeToAdapterViewTypeDelegate.get(viewHolder.getItemViewType());
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

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
      final ViewItem oldItem = oldList.get(oldItemPosition);
      final ViewItem newItem = newList.get(newItemPosition);
      // noinspection unchecked
      return mapViewTypeToItemComparator
          .get(oldItem.viewType)
          .getChangePayload(oldItem.model, newItem.model);
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
