/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DimensionElement {
    @SerializedName("Label")
    private String label;
    @SerializedName("Id")
    private String id;
    @SerializedName("Value")
    private List<String> values;

    public String getLabel() {
        return label;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public List<String> getValues() {
        return values;
    }
}