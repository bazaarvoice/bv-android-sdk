package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SummarisedFeatures extends ProductSentimentsResponse{

    @SerializedName("bestFeatures")
    private List<BestFeature> bestFeatures;
    @SerializedName("worstFeatures")
    private List<WorstFeature> worstFeatures ;

    public List<BestFeature> getBestFeatures() {
        return bestFeatures;
    }

    public void setBestFeatures(List<BestFeature> bestFeatures) {
        this.bestFeatures = bestFeatures;
    }

    public List<WorstFeature> getWorstFeatures() {
        return worstFeatures;
    }

    public void setWorstFeatures(List<WorstFeature> worstFeatures) {
        this.worstFeatures = worstFeatures;
    }
}
