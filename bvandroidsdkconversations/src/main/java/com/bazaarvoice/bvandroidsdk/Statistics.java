/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("ProductStatistics")
    private ProductStatistics productStatistics;

    public ProductStatistics getProductStatistics() {
        return productStatistics;
    }
}