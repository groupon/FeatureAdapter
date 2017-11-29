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
package com.groupon.android.featureadapter.sample.features.collapsible;

import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.AdapterViewTypeDelegate;
import com.groupon.featureadapter.FeatureAnimatorController;
import com.groupon.featureadapter.FeatureController;
import com.groupon.featureadapter.ViewItem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class CollapsibleController extends FeatureController<SampleModel> {

  private static final String TITLE = "Animating Collapsible Feature";

  @Inject FeatureAnimatorController featureAnimatorController;

  private CollapsibleParentAnimatorListener parentAnimatorListener;

  private final CollapsibleParentAdapterViewTypeDelegate parentDelegate = new CollapsibleParentAdapterViewTypeDelegate();

  @Inject
  public CollapsibleController() {
  }

  @Override
  public Collection<AdapterViewTypeDelegate> getAdapterViewTypeDelegates() {
    return Collections.singletonList(parentDelegate);
  }

  @Override
  public List<ViewItem> buildItems(SampleModel sampleModel) {
    if (sampleModel.deal() == null) {
      return Collections.emptyList();
    }

    if (parentAnimatorListener == null) {
      // register animator
      parentAnimatorListener = new CollapsibleParentAnimatorListener();
      featureAnimatorController.registerFeatureAnimatorListener(parentAnimatorListener, parentDelegate);
    }

    final CollapsibleParentModel parentModel = new CollapsibleParentModel(TITLE, sampleModel.collapsibleFeatureState().isCollapsed);
    return Collections.singletonList(new ViewItem<>(parentModel, parentDelegate));
  }
}
