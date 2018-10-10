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

import androidx.annotation.Nullable;

/**
 * A Bazaarvoice Product
 */
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