/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class ShopperProfile {

    private @SerializedName("api_version") String apiVersion;

    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public String toString() {
        return "ShopperProfile{" +
                "profile=" + profile +
                '}';
    }
}
