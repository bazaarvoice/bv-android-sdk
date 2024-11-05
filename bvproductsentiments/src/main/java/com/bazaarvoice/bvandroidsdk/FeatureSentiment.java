package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class FeatureSentiment {
    @SerializedName("feature")
    private String feature;
    @SerializedName("nativeFeature")
    private String nativeFeature;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getNativeFeature() {
        return nativeFeature;
    }

    public void setNativeFeature(String nativeFeature) {
        this.nativeFeature = nativeFeature;
    }
}
