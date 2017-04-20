package com.bazaarvoice.bvandroidsdk;

/**
 * Request to get info and stats for many {@link Product}s
 */
public class BulkProductRequest extends SortableProductRequest {
  private BulkProductRequest(Builder builder) {
    super(builder);
  }

  public static final class Builder extends SortableProductRequest.Builder<Builder, BulkProductRequest> {
    public Builder() {
    }

    public BulkProductRequest build() {
      return new BulkProductRequest(this);
    }
  }
}
