/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Store object is equivalent to a Conversations Product but contains additional Geo-Location attributes
 * such as address, city, latitude, longitude.
 */
public class Store extends IncludeableStoresContent {
    @SerializedName("Brand")
    private Map<String, Object> brand;
    @SerializedName("Attributes")
    private Map<String, StoreLocationAttributes> locationAttributes;
    @SerializedName("ManufacturerPartNumbers")
    private List<String> manufacturerPartNumbers;
    @SerializedName("FamilyIds")
    private List<String> familyIds;
    @SerializedName("AttributesOrder")
    private List<String> attributesOrder;
    @SerializedName("ModelNumbers")
    private List<String> modelNumbers;
    @SerializedName("ProductDescription")
    private String productDescription;
    @SerializedName("BrandExternalId")
    private String brandExternalId;
    @SerializedName("ProductPageUrl")
    private String productPageUrl;
    @SerializedName("ImageUrl")
    private String imageUrl;
    @SerializedName("Name")
    private String name;
    @SerializedName("CategoryId")
    private String categoryId;
    @SerializedName("Id")
    private String id;
    @SerializedName("ReviewStatistics")
    private ReviewStatistics reviewStatistics;
    @SerializedName("ReviewIds")
    private List<String> reviewsIds;

    private transient List<StoreReview> reviews;

    public
    @Nullable
    List<StoreReview> getReviews() {

        if (this.reviews == null && this.reviewsIds != null && super.getIncludedIn().getReviews() != null) {
            this.reviews = new ArrayList<>();
            for (String reviewId : this.reviewsIds) {
                StoreReview review = super.getIncludedIn().getReviewMap().get(reviewId);
                if (review != null) {
                    this.reviews.add(review);
                }
            }
        }

        return this.reviews;
    }

    public Map<String, Object> getBrand() {
        return brand;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getBrandExternalId() {
        return brandExternalId;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public ReviewStatistics getReviewStatistics() {
        return reviewStatistics;
    }

    public enum StoreAttributeType {
        COUNTRY("Country"),
        CITY("City"),
        STATE("State"),
        ADDRESS("Address"),
        PHONE("Phone"),
        POSTALCODE("PostalCode"),
        LATITUDE("Latitude"),
        LONGITUDE("Longitude"),
        ;

        String val;
        StoreAttributeType(String val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val;
        }
    }

    /**
     * Get a location attribute on a Store object by supplying one of the StoreAttributeType enumerations.
     * May return null if the attribute is not defined in the original store feed.
     * @param storeAttributeType
     * @return String value of the attribute, or null if not defined.
     */
    public String getLocationAttribute(StoreAttributeType storeAttributeType) {
        String value = null;

        if (locationAttributes != null) {
            StoreLocationAttributes attribs = locationAttributes.get(storeAttributeType.toString());
            if (attribs != null){
                value = attribs.getElements().get(0).getValue();
            }
        }

        return value;
    }

}