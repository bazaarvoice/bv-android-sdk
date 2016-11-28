/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class StoreLocationElement {

    @SerializedName("Value")
    private String value;
    @SerializedName("Locale")
    private String locale;

    public String getValue() {
        return value;
    }

    public String getLocale() {
        return locale;
    }
}