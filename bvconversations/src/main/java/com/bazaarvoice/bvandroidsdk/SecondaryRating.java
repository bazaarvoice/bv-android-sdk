/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class SecondaryRating {
    @SerializedName("ValueLabel")
    private String valueLabel;
    @SerializedName("MaxLabel")
    private String maxLabel;
    @SerializedName("Label")
    private String label;
    @SerializedName("Id")
    private String id;
    @SerializedName("MinLabel")
    private String minLabel;
    @SerializedName("DisplayType")
    private String displayType;
    @SerializedName("Value")
    private Integer value;
    @SerializedName("ValueRange")
    private Integer valueRange;

    public String getValueLabel() {
        return valueLabel;
    }

    public String getMaxLabel() {
        return maxLabel;
    }

    public String getLabel() {
        return label;
    }

    public String getId() {
        return id;
    }

    public String getMinLabel() {
        return minLabel;
    }

    public String getDisplayType() {
        return displayType;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getValueRange() {
        return valueRange;
    }
}