package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicFeatureResults extends IncludeableContent {

    @SerializedName("features")
    private List<TopicFeature> features;

    @SerializedName("language")
    private String language;

    @SerializedName("productId")
    private String productId;


    public List<TopicFeature> getFeatures() {
        return features;
    }

    public String getLanguage() {
        return language;
    }

    public String getProductId() {
        return productId;
    }
}
