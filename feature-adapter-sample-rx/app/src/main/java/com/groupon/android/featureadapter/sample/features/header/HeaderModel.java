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
package com.groupon.android.featureadapter.sample.features.header;

public class HeaderModel {

  public final String title;
  public final String imageUrl;

  public HeaderModel(String title, String imageUrl) {
    this.title = title;
    this.imageUrl = imageUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HeaderModel that = (HeaderModel) o;

    return (title != null ? title.equals(that.title) : that.title == null)
        && (imageUrl != null ? imageUrl.equals(that.imageUrl) : that.imageUrl == null);
  }

  @Override
  public int hashCode() {
    int result = title != null ? title.hashCode() : 0;
    result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
    return result;
  }
}
