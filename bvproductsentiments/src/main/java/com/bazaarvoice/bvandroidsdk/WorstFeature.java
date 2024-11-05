package com.bazaarvoice.bvandroidsdk;
import com.google.gson.annotations.SerializedName;


public class WorstFeature {
    @SerializedName("featureId")
    private String featureId;
    @SerializedName("feature")
    private String feature;
    @SerializedName("percentPositive")
    private int percentPositive;
    @SerializedName("nativeFeature")
    private String nativeFeature;
    @SerializedName("reviewsMentioned")
    private ReviewsMentioned reviewsMentioned;
    @SerializedName("averageRatingReviews")
    private AverageRatingReviews averageRatingReviews;
    @SerializedName("_embedded")
    private Embedded embedded;

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getPercentPositive() {
        return percentPositive;
    }

    public void setPercentPositive(int percentPositive) {
        this.percentPositive = percentPositive;
    }

    public String getNativeFeature() {
        return nativeFeature;
    }

    public void setNativeFeature(String nativeFeature) {
        this.nativeFeature = nativeFeature;
    }

    public ReviewsMentioned getReviewsMentioned() {
        return reviewsMentioned;
    }

    public void setReviewsMentioned(ReviewsMentioned reviewsMentioned) {
        this.reviewsMentioned = reviewsMentioned;
    }

    public AverageRatingReviews getAverageRatingReviews() {
        return averageRatingReviews;
    }

    public void setAverageRatingReviews(AverageRatingReviews averageRatingReviews) {
        this.averageRatingReviews = averageRatingReviews;
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }
}
