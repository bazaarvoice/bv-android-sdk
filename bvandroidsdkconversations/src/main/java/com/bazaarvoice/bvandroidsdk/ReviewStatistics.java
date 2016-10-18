/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.getFloatSafe;
import static com.bazaarvoice.bvandroidsdk.internal.Utils.getIntegerSafe;

public class ReviewStatistics {

    @SerializedName("HelpfulVoteCount")
    private Integer helpfulVoteCount;
    @SerializedName("NotRecommendedCount")
    private Integer notRecommendedCount;
    @SerializedName("FeaturedReviewCount")
    private Integer featuredReviewCount;
    @SerializedName("NotHelpfulVoteCount")
    private Integer notHelpfulVoteCount;
    @SerializedName("OverallRatingRange")
    private Integer overallRatingRange;
    @SerializedName("TotalReviewCount")
    private Integer totalReviewCount;
    @SerializedName("RatingsOnlyReviewCount")
    private Integer ratingsOnlyReviewCount;
    @SerializedName("RecommendedCount")
    private Integer recommendedCount;
    @SerializedName("AverageOverallRating")
    private Float averageOverallRating;
    @SerializedName("FirstSubmissionTime")
    private String firstSubmissionTime;
    @SerializedName("LastSubmissionTime")
    private String lastSubmissionTime;
    @SerializedName("SecondaryRatingsAverages")
    private Map<String, SecondaryRatingsAverages> secondaryRatingsAverages;
    @SerializedName("RatingDistribution")
    private List<RatingDistributionContainer> ratingDistributions;
    @SerializedName("TagDistribution")
    private Map<String, DistributionElement> tagDistribution;
    @SerializedName("ContextDataDistribution")
    private Map<String, DistributionElement> contextDataDistribution;

    private transient Date firstSubmissionDate;
    private transient Date lastSubmissionDate;
    private transient RatingDistribution ratingDistribution;

    public Integer getHelpfulVoteCount() {
        return getIntegerSafe(helpfulVoteCount);
    }

    public Integer getNotRecommendedCount() {
        return getIntegerSafe(notRecommendedCount);
    }

    public Integer getFeaturedReviewCount() {
        return getIntegerSafe(featuredReviewCount);
    }

    public Integer getNotHelpfulVoteCount() {
        return getIntegerSafe(notHelpfulVoteCount);
    }

    public Integer getOverallRatingRange() {
        return getIntegerSafe(overallRatingRange);
    }

    public Integer getTotalReviewCount() {
        return getIntegerSafe(totalReviewCount);
    }

    public Integer getRatingsOnlyReviewCount() {
        return getIntegerSafe(ratingsOnlyReviewCount);
    }

    public Integer getRecommendedCount() {
        return getIntegerSafe(recommendedCount);
    }

    public Float getAverageOverallRating() {
        return getFloatSafe(averageOverallRating);
    }

    public Map<String, SecondaryRatingsAverages> getSecondaryRatingsAverages() {
        return secondaryRatingsAverages;
    }

    public Map<String, DistributionElement> getTagDistribution() {
        return tagDistribution;
    }

    public Map<String, DistributionElement> getContextDataDistribution() {
        return contextDataDistribution;
    }

    public Date getFirstSubmissionDate() {
        if (firstSubmissionDate == null) {
            firstSubmissionDate = DateUtil.dateFromString(firstSubmissionTime);
        }
        return firstSubmissionDate;
    }

    public Date getLastSubmissionDate() {
        if (lastSubmissionDate == null) {
            lastSubmissionDate = DateUtil.dateFromString(lastSubmissionTime);
        }
        return lastSubmissionDate;
    }

    public RatingDistribution getRatingDistribution() {
        if (ratingDistribution == null) {
            int [] temp = new int[5];
            for (RatingDistributionContainer container : this.ratingDistributions) {
                temp[container.getRatingValue() -1 ] += container.getCount();
            }
            ratingDistribution = new RatingDistribution(temp[0], temp[1], temp[2], temp[3], temp[4]);
        }

        return ratingDistribution;
    }
}