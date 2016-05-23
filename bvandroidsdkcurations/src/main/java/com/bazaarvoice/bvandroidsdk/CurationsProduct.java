package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bazaarvoice on 4/6/16.
 */
public class CurationsProduct {

    //public Brand Brand;
    @SerializedName("Description")
    protected String productDescription;
    //public Attributes att
    @SerializedName("ProductPageUrl")
    protected String productPageUrl;

    @SerializedName("ImageUrl")
    protected String productImageUrl;
    @SerializedName("Name")
    protected String productName;
    @SerializedName("ReviewStatistics")
    protected ReviewStatistics productReviewStatistics;
    @SerializedName("Id")
    protected String id;

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getId() {
        return id;
    }

    public ReviewStatistics getProductReviewStatistics() {
        return productReviewStatistics;
    }

    public class ReviewStatistics{
        @SerializedName("AverageOverallRating")
        protected float avgRating;

        @SerializedName("TotalReviewCount")
        protected int numReviews;

        public int getNumReviews() {
            return numReviews;
        }

        public float getAvgRating() {
            return avgRating;
        }
    }

}
