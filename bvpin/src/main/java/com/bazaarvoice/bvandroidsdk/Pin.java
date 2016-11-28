package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Pin implements BVDisplayableProductContent{

    @SerializedName("product_page_url")
    private String productPageUrl;

    @SerializedName("avg_rating")
    private float averageRating;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @NonNull
    @Override
    public String getId() {
        return id;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return name;
    }

    @Nullable
    @Override
    public String getDisplayImageUrl() {
        return imageUrl;
    }

    @Override
    public float getAverageRating() {
        return averageRating;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }
}
