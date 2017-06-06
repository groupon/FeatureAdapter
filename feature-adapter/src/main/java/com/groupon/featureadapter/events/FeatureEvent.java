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

/**
 * Marker interface for all events that can be fired from a feature.
 *
 * <p>Typically, when used in conjunction with grox-events, a class that implements this interface
 * will also implement the {@link com.groupon.events.Event} interface so that it can be turned into
 * {@link com.groupon.Action}s.
 *
 * <p>Note to maintainers: this class seems a bit strange as it looks both empty and redundant with
 * the {@link com.groupon.events.Event} class from grox-events. But this is done on purpose as we
 * don't want the feature control library to be coupled with grox directly, while letting developers
 * elegantly merge their own events with grox if they want to.
 */
public interface FeatureEvent {}
