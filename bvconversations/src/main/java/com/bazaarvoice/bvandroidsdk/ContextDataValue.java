/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class ContextDataValue {

    @SerializedName("Value")
    private String value;
    @SerializedName("ValueLabel")
    private String valueLabel;
    @SerializedName("DimensionLabel")
    private String dimensionLabel;
    @SerializedName("Id")
    private String id;

    public String getValue() {
        return value;
    }

    public String getValueLabel() {
        return valueLabel;
    }

    public String getDimensionLabel() {
        return dimensionLabel;
    }

    public String getId() {
        return id;
    }
}