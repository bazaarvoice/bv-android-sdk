package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeaturesSentiment extends ProductSentimentsResponse {
    @SerializedName("features")
    private List<FeatureSentiment> features;

    public List<FeatureSentiment> getFeatures() {
        return features;
    }

}
