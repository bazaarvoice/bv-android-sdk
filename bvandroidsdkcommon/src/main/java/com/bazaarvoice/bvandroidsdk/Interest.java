/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Recommendation Interest rating
 */
enum Interest {

@SerializedName("LOW")
LOW("LOW"),
@SerializedName("MED")
MEDIUM("MED"),
@SerializedName("HIGH")
HIGH("HIGH");

    private String value;

    Interest(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
