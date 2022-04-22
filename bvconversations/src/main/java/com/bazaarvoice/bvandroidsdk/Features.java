package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Features extends IncludeableContent {

    @SerializedName("features")
    private List<Feature> features;

    @SerializedName("language")
    private String language;

    @SerializedName("productId")
    private String productId;


    public List<Feature> getFeatures() {
        return features;
    }

    public String getLanguage() {
        return language;
    }

    public String getProductId() {
        return productId;
    }
}
