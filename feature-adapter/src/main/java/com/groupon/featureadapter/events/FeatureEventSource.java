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
package com.groupon.featureadapter.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Internal helper to addFeatureEventListener/removeFeatureEventListener {@link
 * FeatureEventListener} to a list of listeners and fire events to all of them.
 */
public class FeatureEventSource {
  /** Concurrency robust list of listener. */
  private List<FeatureEventListener> featureEventListeners = new CopyOnWriteArrayList<>();

  /**
   * Adds a listener to the list of listeners.
   *
   * @param featureEventListener the listener to be added.
   */
  public void addFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventListeners.add(featureEventListener);
  }

  /**
   * Removes a listener to the list of listeners.
   *
   * @param featureEventListener the listener to be removed.
   */
  public void removeFeatureEventListener(FeatureEventListener featureEventListener) {
    featureEventListeners.remove(featureEventListener);
  }

  /**
   * Emits an {@link FeatureEvent} to all listeners that were previously added.
   *
   * @param featureEvent the event to emit to all listeners.
   */
  public void fireEvent(FeatureEvent featureEvent) {
    for (FeatureEventListener featureEventListener : featureEventListeners) {
      featureEventListener.onFeatureEvent(featureEvent);
    }
  }
}
