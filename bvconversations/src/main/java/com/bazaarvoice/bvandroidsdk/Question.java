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

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Question about a {@link Product}
 */
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

}