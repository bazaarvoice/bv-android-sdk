/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Product extends BaseProduct<Object> {
    @SerializedName("QAStatistics")
    private QAStatistics qaStatistics;
    @SerializedName("QuestionIds")
    private List<String> questionsIds;

    private transient List<Question> questions;

    public
    @Nullable
    List<Question> getQuestions() {

        if (this.questions == null && this.questionsIds != null && super.getIncludedIn().getQuestions() != null) {
            this.questions = new ArrayList<>();

            for (String questionId : this.questionsIds) {
                Question question = (Question) super.getIncludedIn().getQuestionMap().get(questionId);
                if (question != null) {
                    this.questions.add(question);
                }
            }
        }

        return this.questions;
    }

    @Nullable
    public QAStatistics getQaStatistics() {
        return qaStatistics;
    }

    public List<Review> getReviews() {
        return getReviewList();
    }
}