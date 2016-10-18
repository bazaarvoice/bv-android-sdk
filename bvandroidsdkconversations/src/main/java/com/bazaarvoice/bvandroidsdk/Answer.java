/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class Answer extends IncludedContentBase {
    @SerializedName("QuestionId")
    private String questionId;
    @SerializedName("AnswerText")
    private String answerText;
    private transient Question question;

    public Question getQuestion() {
        if (this.question == null && getIncludedIn() != null && getIncludedIn().getQuestions() != null){
            this.question = (Question) getIncludedIn().getQuestionMap().get(this.questionId);
        }

        return this.question;
    }

    public String getAnswerText() {
        return answerText;
    }
}