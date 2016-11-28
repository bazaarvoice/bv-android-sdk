/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistributionElement {

    @SerializedName("Id")
    private String id;
    @SerializedName("Label")
    private String label;
    @SerializedName("Values")
    private List<DistributionValue> values;

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public List<DistributionValue> getValues() {
        return values;
    }
}