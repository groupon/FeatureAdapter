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

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Single;

/**
 * Mimics an Api request to fetch a {@link Deal} from the network.
 */
public class DealApiClient {

  @Inject
  public DealApiClient() {}

  public Single<Deal> getDeal() {
    return Single
      .fromCallable(this::createDeal)
      .delay(1, TimeUnit.SECONDS);
  }

  private Deal createDeal() {
    Deal deal = new Deal();
    deal.title = "Beats by Dr. Dre Solo2 On-Ear Headphones";
    deal.imageUrl = "https://img.grouponcdn.com/deal/mWnYduAspd9fNJuHcgq7/on-1560x990/v1/t300x300.png";

    Option pink = new Option();
    pink.uuid = "pink";
    pink.title = "Beats Solo 2: Pink";
    pink.imageUrl = "https://img.grouponcdn.com/deal/juqRMzE5twcgJ92swjBj/B2-2550x1530/v1/t300x300.png";
    pink.price = "$111.11";
    deal.options.add(pink);

    Option blue = new Option();
    blue.title = "Beats Solo 2: Sapphire Blue";
    blue.uuid = "blue";
    blue.imageUrl = "https://img.grouponcdn.com/deal/5GwZMNzwC6PeHL9Sn6yL/wD-1734x1040/v1//t300x300.png";
    blue.price = "$222.22";
    deal.options.add(blue);

    Option red = new Option();
    red.uuid = "red";
    red.title = "Beats Solo 2: Blush Rose";
    red.imageUrl = "https://img.grouponcdn.com/deal/cTCZaFHeNQkdrBtdRGnz/tD-2048x1229/v1//t300x300.png";
    red.price = "$333.33";
    deal.options.add(red);

    return deal;
  }
}
