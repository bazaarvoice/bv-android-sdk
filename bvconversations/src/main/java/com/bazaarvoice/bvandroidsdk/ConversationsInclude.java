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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Common options for included Product and Review
 *
 * @param <P> Product Type
 * @param <R> Review Type
 */
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