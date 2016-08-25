/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Product extends IncludeableContent {
    @SerializedName("Brand")
    private Map<String, Object> brand;
    @SerializedName("Attributes")
    private Map<String, Object> attributes;
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
    private ReviewStatistics reviewStatistics;
    @SerializedName("QAStatistics")
    private QAStatistics qaStatistics;
    @SerializedName("ReviewIds")
    private List<String> reviewsIds;
    @SerializedName("QuestionIds")
    private List<String> questionsIds;

    private transient List<Review> reviews;
    private transient List<Question> questions;

    public
    @Nullable
    List<Review> getReviews() {

        if (this.reviews == null && this.reviewsIds != null && super.getIncludedIn().getReviews() != null) {
            this.reviews = new ArrayList<>();
            for (String reviewId : this.reviewsIds) {
                Review review = super.getIncludedIn().getReviewMap().get(reviewId);
                if (review != null) {
                    this.reviews.add(review);
                }
            }
        }

        return this.reviews;
    }

    public
    @Nullable
    List<Question> getQuestions() {

        if (this.questions == null && this.questionsIds != null && super.getIncludedIn().getQuestions() != null) {
            this.questions = new ArrayList<>();

            for (String questionId : this.questionsIds) {
                Question question = super.getIncludedIn().getQuestionMap().get(questionId);
                if (question != null) {
                    this.questions.add(question);
                }
            }
        }

        return this.questions;
    }

    public Map<String, Object> getBrand() {
        return brand;
    }

    public Map<String, Object> getAttributes() {
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

    @Nullable
    public QAStatistics getQaStatistics() {
        return qaStatistics;
    }
}