package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bazaarvoice on 4/6/16.
 */
public class CurationsProduct implements BVDisplayableProductContent{

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

    @Deprecated //Use getDisplayImageUrl
    public String getProductImageUrl() {
        return productImageUrl;
    }

    @Deprecated //Use getDisplayName
    public String getProductName() {
        return productName;
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


    @Override @NonNull
    public String getId() {
        return id;
    }

    @Override @Nullable
    public String getDisplayName() {
        return productName;
    }

    @Override @Nullable
    public String getDisplayImageUrl() {
        return productImageUrl;
    }

    @Override
    public float getAverageRating() {
        return productReviewStatistics.getAvgRating();
    }
}
