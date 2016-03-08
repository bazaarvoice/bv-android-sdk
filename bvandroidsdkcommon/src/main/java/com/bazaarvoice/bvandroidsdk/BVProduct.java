/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Bazaarvoice Product
 */
public class BVProduct {

    @SerializedName("product")
    private String productId;

    @SerializedName("num_reviews")
    private int numReviews;

    @SerializedName("avg_rating")
    private float averageRating;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product_page_url")
    private String productPageUrl;

    @SerializedName("name")
    private String productName;

    private boolean sponsored;

    private BVReview review;

    @SerializedName("RS")
    private String rs;

    private RecommendationStats recommendationStats;

    public String getProductId() {
        return productId;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public BVReview getReview() {
        return review;
    }

    public String getRs() {
        return rs;
    }

    void mergeRecommendationStats(RecommendationStats recommendationStats) {
        this.recommendationStats = recommendationStats;
    }

    RecommendationStats getRecommendationStats() {
        return recommendationStats;
    }

    @Override
    public String toString() {
        return "BVProduct{" +
                ", productId='" + productId + '\'' +
                ", numReviews=" + numReviews +
                ", averageRating=" + averageRating +
                ", imageUrl='" + imageUrl + '\'' +
                ", productPageUrl='" + productPageUrl + '\'' +
                ", productName='" + productName + '\'' +
                ", sponsored=" + sponsored +
                ", review=" + review +
                ", rs=" + rs +
                '}';
    }
}
