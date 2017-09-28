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

    return (title != null ? title.equals(that.title) : that.title == null) &&
      (imageUrl != null ? imageUrl.equals(that.imageUrl) : that.imageUrl == null);
  }

  @Override
  public int hashCode() {
    int result = title != null ? title.hashCode() : 0;
    result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
    return result;
  }
}
