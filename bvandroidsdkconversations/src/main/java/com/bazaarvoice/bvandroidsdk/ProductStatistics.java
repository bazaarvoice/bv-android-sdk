/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class ProductStatistics {

    @SerializedName("ReviewStatistics")
    private ReviewStatistics reviewStatistics;
    @SerializedName("NativeReviewStatistics")
    private ReviewStatistics nativeReviewStatistics;
    @SerializedName("ProductId")
    private String productId;

    public ReviewStatistics getReviewStatistics() {
        return reviewStatistics;
    }

    public ReviewStatistics getNativeReviewStatistics() {
        return nativeReviewStatistics;
    }

    public String getProductId() {
        return productId;
    }
}