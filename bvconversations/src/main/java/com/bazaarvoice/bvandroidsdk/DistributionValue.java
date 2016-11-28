/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class DistributionValue {
    @SerializedName("Count")
    private Integer count;
    @SerializedName("Value")
    private String value;

    public Integer getCount() {
        return count;
    }

    public String getValue() {
        return value;
    }
}