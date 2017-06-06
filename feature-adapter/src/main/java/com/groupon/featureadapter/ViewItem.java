package com.groupon.featureadapter;

/**
 * Wraps the model & view type into a single object.
 *
 * @param <MODEL> the view model of this item. It is an ouput class of a {@link FeatureController}.
 */
public final class ViewItem<MODEL> {

  /** The model of this item. */
  public final MODEL model;
  /** Must match a {@link AdapterViewTypeDelegate#getViewType()}. */
  public final int viewType;

  /**
   * Creates a new item.
   *
   * @param model the instance of the {@code MODEL} class.
   * @param adapterViewTypeDelegate the {@link AdapterViewTypeDelegate} associated with this item.
   */
  public ViewItem(MODEL model, AdapterViewTypeDelegate adapterViewTypeDelegate) {
    this.model = model;
    this.viewType = adapterViewTypeDelegate.getViewType();
  }
}
