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
import java.util.List;
import java.util.Map;

/**
 * Common Review attributes
 */
public abstract class BaseReview extends IncludedContentBase.ProductIncludedContentBase {
    @SerializedName(value = "TagDimensions", alternate = "tagDimension")
    private Map<String, DimensionElement> tagDimensions;
    @SerializedName(value= "Cons", alternate ="cons")
    private String cons;
    @SerializedName(value = "Pros", alternate = "pros")
    private String pros;
    @SerializedName(value = "Title", alternate = "title")
    private String title;
    @SerializedName(value = "Helpfulness", alternate = "helpfulness")
    private String helpfulness;
    @SerializedName(value = "RatingRange", alternate = "ratingRange")
    private String ratingRange;
    @SerializedName(value = "ReviewText", alternate = "reviewText")
    private String reviewText;
    @SerializedName(value = "IsRecommended", alternate = "isRecommended")
    private Boolean isRecommended;
    @SerializedName(value = "IsSyndicated", alternate = "isSyndicated")
    private Boolean isSyndicated;
    @SerializedName(value = "IsRatingsOnly", alternate = "isRatingsOnly")
    private Boolean isRatingsOnly;
    @SerializedName(value = "Rating", alternate = "rating")
    private Integer rating;
    @SerializedName(value = "TotalCommentCount", alternate = "totalCommentCount")
    private Integer totalCommentCount;
    @SerializedName(value = "ClientResponses", alternate = "clientResponses")
    private List<Object> clientResponses;
    @SerializedName(value = "SecondaryRatings", alternate = "secondaryRatings")
    private Map<String, SecondaryRating> secondaryRatings;
    @SerializedName(value = "CommentIds", alternate = "commentIds")
    private List<Integer> commentIds;
    @SerializedName(value = "SourceClient", alternate = "sourceClient")
    private String sourceClient;
    @SerializedName(value = "SyndicationSource", alternate = "syndicationSource")
    private SyndicatedSource syndicatedSource;
    @SerializedName(value ="SendEmailAlertWhenCommented", alternate = "sendEmailAlertWhenCommented")
    private Boolean sendEmailAlertWhenCommented;
    @SerializedName(value = "SendEmailAlertWhenPublished", alternate = "sendEmailAlertWhenPublished")
    private Boolean sendEmailAlertWhenPublished;
    @SerializedName(value = "TypicalHoursToPost", alternate = "typicalHoursToPost")
    private Integer typicalHoursToPost;


    @Nullable
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

    public Boolean getSyndicated() {
        return isSyndicated;
    }

    public String getSourceClient() { return sourceClient; }

    public SyndicatedSource getSyndicatedSource(){
        return syndicatedSource;
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

    public Boolean getSendEmailAlertWhenCommented() {
        return sendEmailAlertWhenCommented;
    }

    public Boolean getSendEmailAlertWhenPublished() {
        return sendEmailAlertWhenPublished;
    }

    public Integer getTypicalHoursToPost() {
        return typicalHoursToPost;
    }

    @Nullable
    public Map<String, SecondaryRating> getSecondaryRatings() {
        return secondaryRatings;
    }

    @NonNull
    public List<SecondaryRating> getSecondaryRatingList() {
        if (secondaryRatings == null) {
            return Collections.emptyList();
        }
        List<SecondaryRating> secondaryRatingList = new ArrayList<>();
        for (Map.Entry<String, SecondaryRating> entry : secondaryRatings.entrySet()) {
            secondaryRatingList.add(entry.getValue());
        }
        return secondaryRatingList;
    }

    public List<Integer> getCommentIds() {
        return commentIds;
    }

    public List<Comment> getComments() {
        return getIncludedIn().getComments();
    }
}