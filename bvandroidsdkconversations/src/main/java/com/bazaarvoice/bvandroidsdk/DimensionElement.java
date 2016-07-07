/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DimensionElement {
    @SerializedName("Label")
    private String label;
    @SerializedName("Id")
    private String id;
    @SerializedName("Value")
    private List<String> values;
}