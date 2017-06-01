package com.bazaarvoice.bvandroidsdk;

public enum AuthorIncludeType implements IncludeType {
  REVIEWS("Reviews"),
  QUESTIONS("Questions"),
  ANSWERS("Answers"),
  COMMENTS("Comments");

  private final String key;

  AuthorIncludeType(String key) {
    this.key = key;
  }

  public String toString() {
    return this.key;
  }
}
