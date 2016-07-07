/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

class RatingDistributionContainer {

    @SerializedName("Count")
    private Integer count;
    @SerializedName("RatingValue")
    private Integer ratingValue;

    Integer getCount() {
        return count;
    }

    Integer getRatingValue() {
        return ratingValue;
    }
}