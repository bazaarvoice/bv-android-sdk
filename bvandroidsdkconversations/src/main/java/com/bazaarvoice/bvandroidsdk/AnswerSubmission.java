/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

/**
 * TODO: Describe file here.
 */
public class AnswerSubmission extends ConversationsSubmission{

    private static final String kQUESTIONID = "QuestionId";
    private static final String kANSWERTEXT = "AnswerText";

    private AnswerSubmission(Builder builder) {
        super(builder);
    }

    @Override
    String getEndPoint() {
        return "submitanswer.json";
    }

    @Override
    BazaarException getError() {
        return null;
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (AnswerSubmission.Builder) getBuilder();
        queryParams.put(kQUESTIONID, builder.questionId);
        queryParams.put(kANSWERTEXT, builder.answerText);
    }

    public static final class Builder extends ConversationsSubmission.Builder<Builder>{
        private final String questionId;
        private final String answerText;

        public Builder(Action action, String questionId, String answerText) {
            super(action);
            this.questionId = questionId;
            this.answerText = answerText;
        }

        public Builder test(){
            return this;
        }

        public AnswerSubmission build() {
            return new AnswerSubmission(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            return PhotoUpload.ContentType.Answer;
        }
    }
}