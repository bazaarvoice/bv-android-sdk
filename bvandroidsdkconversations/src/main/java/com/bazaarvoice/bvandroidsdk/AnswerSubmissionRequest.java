/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

public class AnswerSubmissionRequest extends ConversationsSubmissionRequest {

    private static final String kQUESTIONID = "QuestionId";
    private static final String kANSWERTEXT = "AnswerText";
    public static final String ANSWER_ENDPOINT = "submitanswer.json";

    private AnswerSubmissionRequest(Builder builder) {
        super(builder);
    }

    @Override
    String getEndPoint() {
        return ANSWER_ENDPOINT;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    String getProductId() {
        Map<String, Object> queryParams = makeQueryParams();
        return queryParams.containsKey(kQUESTIONID) ? (String) queryParams.get(kQUESTIONID) : "";
    }

    @Override
    protected String getApiKey() {
        return BVSDK.getInstance().getApiKeyConversations();
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {
        Builder builder = (AnswerSubmissionRequest.Builder) getBuilder();
        queryParams.put(kQUESTIONID, builder.questionId);
        queryParams.put(kANSWERTEXT, builder.answerText);
    }

    public static final class Builder extends ConversationsSubmissionRequest.Builder<Builder>{
        private final String questionId;
        private final String answerText;

        public Builder(Action action, String questionId, String answerText) {
            super(action);
            this.questionId = questionId;
            this.answerText = answerText;
        }

        public AnswerSubmissionRequest build() {
            return new AnswerSubmissionRequest(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            return PhotoUpload.ContentType.Answer;
        }
    }
}