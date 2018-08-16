/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Bazaarvoice Product
 */
public class BVProduct implements BVDisplayableProductContent{

    protected boolean impressed = false;

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

    @SerializedName("CategoryId")
    private String categoryId;

    @SerializedName("category_ids")
    private List<String> categoryIds;

    public boolean isImpressed() {
        return impressed;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    private boolean sponsored;

    private BVReview review;

    @SerializedName("RS")
    private String rs;

    private RecommendationStats recommendationStats;

    @Deprecated //Use getId
    public String getProductId() {
        return productId;
    }

    public int getNumReviews() {
        return numReviews;
    }

    @Deprecated //Use getDisplayImageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    @Deprecated //Use getDisplayName
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

    public String getCategoryId() {
        return categoryId;
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

    @Override @NonNull
    public String getId() {
        return productId;
    }

    @Override @Nullable
    public String getDisplayName() {
        return productName;
    }

    @Override @Nullable
    public String getDisplayImageUrl() {
        return imageUrl;
    }

    @Override
    public float getAverageRating() {
        return averageRating;
    }
}
