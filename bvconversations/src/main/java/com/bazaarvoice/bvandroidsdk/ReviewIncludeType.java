package com.bazaarvoice.bvandroidsdk;

public enum ReviewIncludeType {
  PRODUCTS("products");

  private String value;

  ReviewIncludeType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
