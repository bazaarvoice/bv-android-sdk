/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class StoreReview extends IncludeableStoresContent {
    @SerializedName("TagDimensions")
    private Map<String, DimensionElement> tagDimensions;
    @SerializedName("Cons")
    private String cons;
    @SerializedName("Pros")
    private String pros;
    @SerializedName("Title")
    private String title;
    @SerializedName("Helpfulness")
    private String helpfulness;
    @SerializedName("RatingRange")
    private String ratingRange;
    @SerializedName("ReviewText")
    private String reviewText;
    @SerializedName("IsRecommended")
    private Boolean isRecommended;
    @SerializedName("IsRatingsOnly")
    private Boolean isRatingsOnly;
    @SerializedName("Rating")
    private Integer rating;
    @SerializedName("ProductId")
    private String storeId;
    @SerializedName("Id")
    private String contentId;
    @SerializedName("TotalCommentCount")
    private Integer totalCommentCount;
    @SerializedName("ClientResponses")
    private List<Object> clientResponses;
    @SerializedName("SecondaryRatings")
    private Map<String, SecondaryRating> secondaryRatings;
    @SerializedName("CommentIds")
    private List<Integer> commentIds;

    public Map<String, DimensionElement> getTagDimensions() {
        return tagDimensions;
    }

    public String getCons() {
        return cons;
    }

    public String getPros() {
        return pros;
    }

    public String getTitle() {
        return title;
    }

    public String getHelpfulness() {
        return helpfulness;
    }

    public String getRatingRange() {
        return ratingRange;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Boolean getRecommended() {
        return isRecommended;
    }

    public Boolean getRatingsOnly() {
        return isRatingsOnly;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getTotalCommentCount() {
        return totalCommentCount;
    }

    public List<Object> getClientResponses() {
        return clientResponses;
    }

    public Map<String, SecondaryRating> getSecondaryRatings() {
        return secondaryRatings;
    }

    public List<Integer> getCommentIds() {
        return commentIds;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getContentId() {
        return contentId;
    }
}