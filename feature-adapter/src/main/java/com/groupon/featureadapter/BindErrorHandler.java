package com.groupon.featureadapter;

/**
 * Implement this interface and set it to the {@link FeaturesAdapter} to catch and handle any error that happens during the binding process
 * see {@link FeaturesAdapter#setBindErrorHandler(BindErrorHandler)}
 */
@FunctionalInterface
public interface BindErrorHandler {

  /**
  * Implement this method to catch and handle any error that happens during the binding process
  *
  * @param throwable the error
  * @param position the position of the feature that caused the error
  */
  void handleBindError(Throwable throwable, int position);
}
