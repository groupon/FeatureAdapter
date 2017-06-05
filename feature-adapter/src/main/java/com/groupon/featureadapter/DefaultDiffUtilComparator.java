package com.groupon.featureadapter;

/**
 * Default implementation of {@link DiffUtilComparator}. It considers that items at the same
 * position are the same (basically items are place holders), but can have changed between the new
 * list and the old list.
 *
 * @param <MODEL>
 */
public class DefaultDiffUtilComparator<MODEL> implements DiffUtilComparator<MODEL> {

  /**
   * @param oldModel the item in the old list.
   * @param newModel the item in the new list.
   * @return true. Basically items are place holders.
   */
  @Override
  public boolean areItemsTheSame(MODEL oldModel, MODEL newModel) {
    return true;
  }

  /**
   * @param oldModel the item in the old list.
   * @param newModel the item in the new list.
   * @return true if {@code oldModel.equals(newModel)} and false otherwise.
   */
  @Override
  public boolean areContentsTheSame(MODEL oldModel, MODEL newModel) {
    return oldModel.equals(newModel);
  }
}
