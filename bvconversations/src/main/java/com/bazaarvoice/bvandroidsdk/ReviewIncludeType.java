package com.bazaarvoice.bvandroidsdk;

public enum ReviewIncludeType implements IncludeType {
  PRODUCTS("products"),
  COMMENTS("comments"),
  AUTHORS("authors"),
  CATEGORIES("categories");

  private String value;

  ReviewIncludeType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
