/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConversationsInclude {
    @SerializedName("Products")
    private Map<String, Product> productMap;
    @SerializedName("Answers")
    private Map<String, Answer> answerMap;
    @SerializedName("Questions")
    private Map<String, Question> questionMap;
    @SerializedName("Reviews")
    private Map<String, Review> reviewMap;

    private transient List<Product> products;
    private transient List<Answer> answers;
    private transient List<Question> questions;
    private transient List<Review> reviews;

    protected Map<String, Product> getProductMap() {
        return productMap;
    }

    protected Map<String, Answer> getAnswerMap() {
        return answerMap;
    }

    protected Map<String, Question> getQuestionMap() {
        return questionMap;
    }

    protected Map<String, Review> getReviewMap() {
        return reviewMap;
    }

    public List<Review> getReviews() {
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

    public List<Product> getProducts() {
        if (this.products == null && this.questionMap != null) {
            this.products = processContent(this.productMap);
        }
        return this.products;
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