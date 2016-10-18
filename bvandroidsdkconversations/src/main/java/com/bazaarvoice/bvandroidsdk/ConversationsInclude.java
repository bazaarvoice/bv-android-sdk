/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ConversationsInclude<P extends BaseProduct, R extends BaseReview> {
    @SerializedName("Products")
    private Map<String, P> itemMap;
    @SerializedName("Answers")
    private Map<String, Answer> answerMap;
    @SerializedName("Questions")
    private Map<String, Question> questionMap;
    @SerializedName("Reviews")
    private Map<String, R> reviewMap;

    private transient List<P> items;
    private transient List<Answer> answers;
    private transient List<Question> questions;
    private transient List<R> reviews;

    protected Map<String, P> getItemMap() {
        return itemMap;
    }

    protected Map<String, Answer> getAnswerMap() {
        return answerMap;
    }

    protected Map<String, Question> getQuestionMap() {
        return questionMap;
    }

    protected Map<String, R> getReviewMap() {
        return reviewMap;
    }

    protected List<R> getReviewsList() {
        if (this.reviews == null && this.reviewMap != null) {
            this.reviews = processContent(this.reviewMap);
        }
        return reviews;
    }

    public List<Question> getQuestions() {
        if (this.questions == null && this.questionMap != null) {
            this.questions = processContent(this.questionMap);
        }
        return this.questions;
    }

    protected List<P> getItems() {
        if (this.items == null && this.itemMap != null) {
            this.items = processContent(this.itemMap);
        }
        return this.items;
    }

    public List<Answer> getAnswers() {
        if (this.answers == null && this.answerMap != null) {
            this.answers = processContent(this.answerMap);
        }
        return this.answers;
    }

    private <T extends IncludeableContent> List<T> processContent(Map<String, T> contents) {
        List<T> contentList = new ArrayList<>();
        for (T content : contents.values()) {
            content.setIncludedIn(this);
            contentList.add(content);
        }

        return contentList;
    }
}