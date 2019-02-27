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
package com.groupon.android.featureadapter.sample.model;

import com.groupon.android.featureadapter.sample.rx.R;
import io.reactivex.Observable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import static io.reactivex.Observable.fromCallable;

/**
 * Mimics an Api request to fetch a {@link Deal} from the network.
 */
public class DealApiClient {

  @Inject
  public DealApiClient() {}

  public Observable<Deal> getDeal() {
    return fromCallable(this::createDeal)
      .delay(1, TimeUnit.SECONDS);
  }

  private Deal createDeal() {
    Deal deal = new Deal();
    deal.title = "Arrow";
    deal.imageId = R.drawable.ic_keyboard_arrow_down_black_24px;

    Option left = new Option();
    left.uuid = "left";
    left.title = "Arrow left";
    left.imageId = R.drawable.ic_keyboard_arrow_left_black_24px;
    left.price = "$111.11";
    deal.options.add(left);

    Option up = new Option();
    up.uuid = "up";
    up.title = "Arrow up";
    up.imageId = R.drawable.ic_keyboard_arrow_up_black_24px;
    up.price = "$333.33";
    deal.options.add(up);

    Option right = new Option();
    right.uuid = "right";
    right.title = "Arrow right";
    right.imageId = R.drawable.ic_keyboard_arrow_right_black_24px;
    right.price = "$222.22";
    deal.options.add(right);



    deal.badges.addAll(Arrays.asList("Zero", "One", "Two", "Three", "Four", "Five", "Six"));

    return deal;
  }
}
