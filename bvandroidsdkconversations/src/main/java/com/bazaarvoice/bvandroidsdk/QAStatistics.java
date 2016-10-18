/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.getIntegerSafe;

public class QAStatistics {

    @SerializedName("HelpfulVoteCount")
    private Integer helpfulVoteCount;
    @SerializedName("BestAnswerCount")
    private Integer bestAnswerCount;
    @SerializedName("QuestionHelpfulVoteCount")
    private Integer questionHelpfulVoteCount;
    @SerializedName("TotalAnswerCount")
    private Integer totalAnswerCount;
    @SerializedName("AnswerNotHelpfulVoteCount")
    private Integer answerNotHelpfulVoteCount;
    @SerializedName("TotalQuestionCount")
    private Integer totalQuestionCount;
    @SerializedName("QuestionNotHelpfulVoteCount")
    private Integer questionNotHelpfulVoteCount;
    @SerializedName("FeaturedQuestionCount")
    private Integer featuredQuestionCount;
    @SerializedName("FeaturedAnswerCount")
    private Integer featuredAnswerCount;
    @SerializedName("AnswerHelpfulVoteCount")
    private Integer answerHelpfulVoteCount;
    @SerializedName("LastQuestionAnswerTime")
    private String lastQuestionAnswerTime;
    @SerializedName("FirstAnswerTime")
    private String firstAnswerTime;
    @SerializedName("LastQuestionTime")
    private String lastQuestionTime;
    @SerializedName("LastAnswerTime")
    private String lastAnswerTime;
    @SerializedName("FirstQuestionTime")
    private String firstQuestionTime;
    @SerializedName("TagDistribution")
    DistributionElement tagDistribution;
    @SerializedName("ContextDataDistribution")
    DistributionElement contextDataDistribution;

    private transient Date lastQuestionAnswerDate;
    private transient Date firstAnswerDate;
    private transient Date lastQuestionDate;
    private transient Date lastAnswerDate;
    private transient Date firstQuestionDate;

    public Integer getHelpfulVoteCount() {
        return getIntegerSafe(helpfulVoteCount);
    }

    public Integer getBestAnswerCount() {
        return getIntegerSafe(bestAnswerCount);
    }

    public Integer getQuestionHelpfulVoteCount() {
        return getIntegerSafe(questionHelpfulVoteCount);
    }

    public Integer getTotalAnswerCount() {
        return getIntegerSafe(totalAnswerCount);
    }

    public Integer getAnswerNotHelpfulVoteCount() {
        return getIntegerSafe(answerNotHelpfulVoteCount);
    }

    public Integer getTotalQuestionCount() {
        return getIntegerSafe(totalQuestionCount);
    }

    public Integer getQuestionNotHelpfulVoteCount() {
        return getIntegerSafe(questionNotHelpfulVoteCount);
    }

    public Integer getFeaturedQuestionCount() {
        return getIntegerSafe(featuredQuestionCount);
    }

    public Integer getFeaturedAnswerCount() {
        return getIntegerSafe(featuredAnswerCount);
    }

    public Integer getAnswerHelpfulVoteCount() {
        return getIntegerSafe(answerHelpfulVoteCount);
    }

    public DistributionElement getTagDistribution() {
        return tagDistribution;
    }

    public DistributionElement getContextDataDistribution() {
        return contextDataDistribution;
    }

    public Date getLastQuestionAnswerDate() {
        if (lastQuestionAnswerDate == null) {
            lastQuestionAnswerDate = DateUtil.dateFromString(lastQuestionAnswerTime);
        }
        return lastQuestionAnswerDate;
    }

    public Date getFirstAnswerDate() {
        if (firstAnswerDate == null) {
            firstAnswerDate = DateUtil.dateFromString(firstAnswerTime);
        }
        return firstAnswerDate;
    }

    public Date getLastQuestionDate() {
        if (lastQuestionDate == null) {
            lastQuestionDate = DateUtil.dateFromString(lastQuestionTime);
        }
        return lastQuestionDate;
    }

    public Date getLastAnswerDate() {
        if (lastAnswerDate == null) {
            lastAnswerDate = DateUtil.dateFromString(lastAnswerTime);
        }
        return lastAnswerDate;
    }

    public Date getFirstQuestionDate() {
        if (firstQuestionDate == null) {
            firstAnswerDate = DateUtil.dateFromString(firstQuestionTime);
        }
        return firstQuestionDate;
    }
}