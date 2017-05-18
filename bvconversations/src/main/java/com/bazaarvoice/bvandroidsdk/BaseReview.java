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

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Common Review attributes
 */
public abstract class BaseReview extends IncludedContentBase.ProductIncludedContentBase {
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
    @SerializedName("IsSyndicated")
    private Boolean isSyndicated;
    @SerializedName("IsRatingsOnly")
    private Boolean isRatingsOnly;
    @SerializedName("Rating")
    private Integer rating;
    @SerializedName("TotalCommentCount")
    private Integer totalCommentCount;
    @SerializedName("ClientResponses")
    private List<Object> clientResponses;
    @SerializedName("SecondaryRatings")
    private Map<String, SecondaryRating> secondaryRatings;
    @SerializedName("CommentIds")
    private List<Integer> commentIds;
    @SerializedName("SyndicationSource")
    private SyndicatedSource syndicatedSource;

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

    public Map<String, SecondaryRating> getSecondaryRatings() {
        return secondaryRatings;
    }

    public List<Integer> getCommentIds() {
        return commentIds;
    }

    public List<Comment> getComments() {
        return getIncludedIn().getComments();
    }
}