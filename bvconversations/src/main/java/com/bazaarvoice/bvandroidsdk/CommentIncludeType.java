package com.bazaarvoice.bvandroidsdk;

public enum CommentIncludeType implements IncludeType {
  PRODUCTS("products"), REVIEWS("reviews"), AUTHORS("authors");

  private String value;

  CommentIncludeType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

}
