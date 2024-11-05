package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Expressions extends ProductSentimentsResponse{

    @SerializedName("nativeFeature")
    private String nativeFeature;
    @SerializedName("expressions")
    private List<String> expressions;

    public String getNativeFeature() {
        return nativeFeature;
    }

    public void setNativeFeature(String nativeFeature) {
        this.nativeFeature = nativeFeature;
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }
}