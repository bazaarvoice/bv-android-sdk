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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Common fields for UGC items
 *
 * @param <ConversationsIncludeType> Type of {@link ConversationsInclude}
 */
class IncludedContentBase<ConversationsIncludeType extends ConversationsInclude> extends IncludeableContent<ConversationsIncludeType> {

    @SerializedName(value = "UserNickname", alternate = "userNickname")
    private String userNickname;
    @SerializedName(value = "SubmissionId", alternate ={"submissionId", "submissionID"})
    private String submissionId;
    @SerializedName(value = "UserLocation", alternate = "userLocation")
    private String userLocation;
    @SerializedName(value = "AuthorId", alternate = "authorId")
    private String authorId;
    @SerializedName(value = "CampaignId", alternate = "campaignId")
    private String campaignId;
    @SerializedName(value = "ContentLocale", alternate = "contentLocale")
    private String contentLocale;
    @SerializedName(value = "ModerationStatus", alternate = "moderationStatus")
    private String moderationStatus;
    @SerializedName(value = "Id", alternate = "id")
    private String id;
    @SerializedName(value = "Photos", alternate = "photos")
    private List<Photo> photos;
    @SerializedName(value = "ContextDataValues", alternate = "contextDataValues")
    private Map<String, ContextDataValue> contextDataValues;
    @SerializedName(value = "Videos", alternate = "videos")
    private List<Video> videos;
    @SerializedName(value = "Badges", alternate = "badges")
    private Map<String, Badge> badges;
    @SerializedName(value = "ProductRecommendationIds", alternate = "productRecommendationIds")
    private List<String> productRecommendationIds;
    @SerializedName(value = "TotalFeedbackCount", alternate = "totalFeedbackCount")
    private Integer totalFeedbackCount;
    @SerializedName(value = "TotalPositiveFeedbackCount", alternate = "totalPositiveFeedbackCount")
    private Integer totalPositiveFeedbackCount;
    @SerializedName(value = "IsFeatured", alternate = "isFeatured")
    private Boolean isFeatured;
    @SerializedName(value = "TotalNegativeFeedbackCount", alternate = "totalNegativeFeedbackCount")
    private Integer totalNegativeFeedbackCount;
    @SerializedName(value = "LastModificationTime", alternate = "lastModificationTime")
    private String lastModificationTime;
    @SerializedName(value = "LastModeratedTime", alternate = "lastModeratedTime")
    private String lastModeratedTime;
    @SerializedName(value = "SubmissionTime", alternate = "submissionTime")
    private String submissionTime;
    @SerializedName(value = "AdditionalFields", alternate = "additionalFields")
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

    @Nullable
    public List<Photo> getPhotos() {
        return photos;
    }

    @Nullable
    public Map<String, ContextDataValue> getContextDataValues() {
        return contextDataValues;
    }

    public List<Video> getVideos() {
        return videos;
    }

    @Nullable
    public Map<String, Badge> getBadges() {
        return badges;
    }

    @NonNull
    public List<Badge> getBadgeList() {
        if (badges == null) {
            return Collections.<Badge>emptyList();
        }
        List<Badge> badgeList = new ArrayList<>();
        for (Map.Entry<String, Badge> entry : badges.entrySet()) {
            badgeList.add(entry.getValue());
        }
        return badgeList;
    }

    @NonNull
    public List<ContextDataValue> getContextDataValueList() {
        if (contextDataValues == null) {
            return Collections.<ContextDataValue>emptyList();
        }
        List<ContextDataValue> cdvList = new ArrayList<>();
        for (Map.Entry<String, ContextDataValue> entry : contextDataValues.entrySet()) {
            cdvList.add(entry.getValue());
        }
        return cdvList;
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
        if (submissionDate == null && submissionTime != null) {
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
        if (lastModificationDate == null) {
            lastModificationDate = DateUtil.dateFromString(lastModificationTime);
        }
        return lastModificationDate;
    }

    static class ProductIncludedContentBase extends IncludedContentBase {
        @SerializedName(value = "ProductId", alternate = "productExternalID")
        private String productId;

        private transient Product product;

        @Nullable
        public Product getProduct() {

            if (this.product == null && super.getIncludedIn() != null && super.getIncludedIn().getItemMap() != null) {
                this.product = (Product) getIncludedIn().getItemMap().get(productId);
            }

            return this.product;
        }

        public String getProductId() {
            return productId;
        }

    }


}