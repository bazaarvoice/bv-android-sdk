/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreLocationAttributes  {

    @SerializedName("Id")
    private String id;

    @SerializedName("Values")
    private List<StoreLocationElement> elements;

    public String getId() {
        return id;
    }

    public List<StoreLocationElement> getElements() {
        return elements;
    }
}