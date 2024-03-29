package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class FeatureKeyword extends IncludeableContent {

    @SerializedName("feature")
    private String feature;

    @SerializedName("localizedFeature")
    private String localizedFeature;

    public String getFeature() {
        return feature;
    }

    public String getLocalizedFeature() {
        return localizedFeature;
    }
}
