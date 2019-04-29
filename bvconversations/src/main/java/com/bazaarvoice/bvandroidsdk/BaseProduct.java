/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Common attributes for items that are considered 'products' by
 * the Conversations API.
 *
 * @param <AttributeType> Type of product specific attributes
 */
public abstract class BaseProduct<AttributeType> extends IncludeableContent implements BVDisplayableProductContent{
    @SerializedName("Brand")
    private Map<String, Object> brand;
    @SerializedName("Attributes")
    private Map<String, AttributeType> attributes;
    private List<String> ISBNs, UPCs, EANs;
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
    @Nullable private ReviewStatistics reviewStatistics;
    @SerializedName("FilteredReviewStatistics")
    @Nullable private ReviewStatistics filteredReviewStatistics;
    @SerializedName("StoryStatistics")
    @Nullable private StoryStatistics storyStatistics;
    @SerializedName("ReviewIds")
    private List<String> reviewsIds;

    private transient List<BaseReview> reviews;

    protected @Nullable <ReviewType extends BaseReview> List<ReviewType> getReviewList() {

        if (this.reviews == null && this.reviewsIds != null && super.getIncludedIn().getReviewsList() != null) {
            this.reviews = new ArrayList<>();
            for (String reviewId : this.reviewsIds) {
                ReviewType review = (ReviewType) super.getIncludedIn().getReviewMap().get(reviewId);
                if (review != null) {
                    this.reviews.add(review);
                }
            }
        }

        return (List<ReviewType>) this.reviews;
    }

    public Map<String, Object> getBrand() {
        return brand;
    }

    public Map<String, AttributeType> getAttributes() {
        return attributes;
    }

    public List<String> getISBNs() {
        return ISBNs;
    }

    public List<String> getUPCs() {
        return UPCs;
    }

    public List<String> getEANs() {
        return EANs;
    }

    public List<String> getManufacturerPartNumbers() {
        return manufacturerPartNumbers;
    }

    public List<String> getFamilyIds() {
        return familyIds;
    }

    public List<String> getAttributesOrder() {
        return attributesOrder;
    }

    public List<String> getModelNumbers() {
        return modelNumbers;
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

    @Deprecated //Use getDisplayImageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    @Deprecated //Use getDisplayName
    public String getName() {
        return name;
    }

    public String getCategoryId() {
        return categoryId;
    }


    public List<String> getReviewsIds(){
        return reviewsIds;
    }

    @Nullable
    public ReviewStatistics getReviewStatistics() {
        if (filteredReviewStatistics != null) {
            return filteredReviewStatistics;
        }
        return reviewStatistics;
    }

    @Override @NonNull
    public String getId() {
        return id;
    }

    @Override @Nullable
    public String getDisplayName() {
        return name;
    }

    @Override @Nullable
    public String getDisplayImageUrl() {
        return imageUrl;
    }

    /**
     * Get the averageRating from reviewStatistics. ReviewStats must have been included in the request.
     * @return averageRating or 0f if reviewStatistics is null
     */
    @Override
    public float getAverageRating() {
        if(reviewStatistics != null) {
            return reviewStatistics.getAverageOverallRating();
        }
        return 0f;
    }
}