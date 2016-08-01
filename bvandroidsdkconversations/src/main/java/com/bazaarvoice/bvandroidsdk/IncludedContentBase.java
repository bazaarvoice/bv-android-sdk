/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Map;

class IncludedContentBase extends IncludeableContent {

    @SerializedName("UserNickname")
    private String userNickname;
    @SerializedName("SubmissionId")
    private String submissionId;
    @SerializedName("UserLocation")
    private String userLocation;
    @SerializedName("AuthorId")
    private String authorId;
    @SerializedName("CampaignId")
    private String campaignId;
    @SerializedName("ContentLocale")
    private String contentLocale;
    @SerializedName("ModerationStatus")
    private String moderationStatus;
    @SerializedName("Id")
    private String id;
    @SerializedName("Photos")
    private List<Photo> photos;
    @SerializedName("ContextDataValues")
    private Map<String, ContextDataValue> contextDataValues;
    @SerializedName("Videos")
    private List<Video> videos;
    @SerializedName("Badges")
    private Map<String, Badge> badges;
    @SerializedName("ProductRecommendationIds")
    private List<String> productRecommendationIds;
    @SerializedName("TotalFeedbackCount")
    private Integer totalFeedbackCount;
    @SerializedName("TotalPositiveFeedbackCount")
    private Integer totalPositiveFeedbackCount;
    @SerializedName("IsFeatured")
    private Boolean isFeatured;
    @SerializedName("TotalNegativeFeedbackCount")
    private Integer totalNegativeFeedbackCount;
    @SerializedName("LastModificationTime")
    private String lastModificationTime;
    @SerializedName("LastModeratedTime")
    private String lastModeratedTime;
    @SerializedName("SubmissionTime")
    private String submissionTime;
    @SerializedName("AdditionalFields")
    private Map<String, Object> additionalFields;

    private transient Date submissionDate;
    private transient Date lastModeratedDate;
    private transient Date lastModificationDate;


    public String getUserNickname() {
        return userNickname;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getContentLocale() {
        return contentLocale;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public String getId() {
        return id;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public Map<String, ContextDataValue> getContextDataValues() {
        return contextDataValues;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public Map<String, Badge> getBadges() {
        return badges;
    }

    public List<String> getProductRecommendationIds() {
        return productRecommendationIds;
    }

    public Integer getTotalFeedbackCount() {
        return totalFeedbackCount;
    }

    public Integer getTotalPositiveFeedbackCount() {
        return totalPositiveFeedbackCount;
    }

    public Boolean getFeatured() {
        return isFeatured;
    }

    public Integer getTotalNegativeFeedbackCount() {
        return totalNegativeFeedbackCount;
    }

    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public Date getSubmissionDate() {
        if (submissionDate == null) {
            submissionDate = DateUtil.dateFromString(submissionTime);
        }
        return submissionDate;
    }

    public Date getLastModeratedDate() {
        if (lastModeratedDate == null) {
            lastModeratedDate = DateUtil.dateFromString(lastModeratedTime);
        }
        return lastModeratedDate;
    }

    public Date getLastModificationDate() {
        if (lastModeratedDate == null) {
            lastModeratedDate = DateUtil.dateFromString(lastModeratedTime);
        }
        return lastModificationDate;
    }

    static class ProductIncludedContentBase extends IncludedContentBase {
        @SerializedName("ProductId")
        private String productId;

        private transient Product product;

        public Product getProduct() {

            if (this.product == null && super.getIncludedIn() != null && super.getIncludedIn().getProducts() != null) {
                this.product = super.getIncludedIn().getProductMap().get(this.productId);
            }

            return this.product;
        }

        public String getProductId() {
            return productId;
        }
    }
}