/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Question extends IncludedContentBase.ProductIncludedContentBase {
    @SerializedName("QuestionSummary")
    private String questionSummary;
    @SerializedName("QuestionDetails")
    private String questionDetails;
    @SerializedName("TotalAnswerCount")
    private Integer totalAnswerCount;
    @SerializedName("TotalInappropriateFeedbackCount")
    private Integer totalInappropriateFeedbackCount;
    @SerializedName("TagDimensions")
    private Map<String, DimensionElement> tagDimensions;
    @SerializedName("BrandImageLogoURL")
    private String brandImageLogoUrl;

    @SerializedName("AnswerIds")
    private List<String> answerIds;

    private transient List<Answer> answers;

    public String getQuestionSummary() {
        return questionSummary;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public Integer getTotalAnswerCount() {
        return totalAnswerCount;
    }

    public Integer getTotalInappropriateFeedbackCount() {
        return totalInappropriateFeedbackCount;
    }

    public Map<String, DimensionElement> getTagDimensions() {
        return tagDimensions;
    }

    public
    @Nullable
    List<Answer> getAnswers() {

        if (this.answers == null && this.answerIds != null && super.getIncludedIn().getAnswers() != null) {
            this.answers = new ArrayList<>();
            for (String answerId : this.answerIds) {
                Answer answer = (Answer) super.getIncludedIn().getAnswerMap().get(answerId);
                if (answer != null) {
                    this.answers.add(answer);
                }
            }
        }

        return this.answers;
    }

    public String getBrandImageLogoUrl() {
        return brandImageLogoUrl;
    }
}