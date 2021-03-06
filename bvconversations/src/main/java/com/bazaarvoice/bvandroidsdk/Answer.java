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

/**
 * Answer for a {@link Question} about a CGC item.
 */
public class Answer extends IncludedContentBase<ConversationsIncludeProduct> {
    @SerializedName("QuestionId")
    private String questionId;
    @SerializedName("AnswerText")
    private String answerText;
    @SerializedName("BrandImageLogoURL")
    private String brandImageLogoUrl;
    @SerializedName("IsSyndicated")
    private boolean isSyndicated;
    @SerializedName(value = "SourceClient", alternate = "sourceClient")
    private String sourceClient;
    @SerializedName("SyndicationSource")
    private SyndicatedSource syndicatedSource;
    private transient Question question;

    public Question getQuestion() {
        if (this.question == null && getIncludedIn() != null && getIncludedIn().getQuestions() != null){
            this.question = (Question) getIncludedIn().getQuestionMap().get(this.questionId);
        }

        return this.question;
    }

    public String getBrandImageLogoUrl() {
        return brandImageLogoUrl;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isSyndicated() {
        return isSyndicated;
    }

    public String getSourceClient() { return sourceClient; }

    public SyndicatedSource getSyndicatedSource() {
        return syndicatedSource;
    }
}