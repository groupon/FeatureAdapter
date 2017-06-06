package com.groupon.featureadapter;

/**
 * Compares item of a given model class. Classes implementing this interface will be used internally
 * with {@link android.support.v7.util.DiffUtil.Callback} to compare items in the recycler view.
 * When an adapter updates the list of items for a given feature controller, items of the old list
 * and of the new list will be compared. Depending on the result of this comparison, items will be
 * animated in the underlying recycler view in different ways (additions, deletions, modifications).
 *
 * @param <MODEL> the class of the items to compare.
 * @see android.support.v7.util.DiffUtil.Callback
 * @see FeaturesAdapter#updateFeatureItems(FeatureUpdate)
 */
public interface DiffUtilComparator<MODEL> {

  /** @see android.support.v7.util.DiffUtil.Callback#areItemsTheSame(int, int) */
  boolean areItemsTheSame(MODEL oldModel, MODEL newModel);

  /** @see android.support.v7.util.DiffUtil.Callback#areContentsTheSame(int, int) */
  boolean areContentsTheSame(MODEL oldModel, MODEL newModel);
}
