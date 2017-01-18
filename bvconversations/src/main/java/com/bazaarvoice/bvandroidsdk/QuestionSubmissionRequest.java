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

import java.util.Map;

/**
 * Request for submitting {@link Question}s
 */
public class QuestionSubmissionRequest extends ConversationsSubmissionRequest {

    private static final String kPRODUCT_ID = "ProductId";
    private static final String kQUESTION_SUMMARY = "QuestionSummary";
    private static final String kQUESTION_DETAILS = "QuestionDetails";
    private static final String kIS_ANONUSER = "IsUserAnonymous";
    private static final String kSEND_EMAIL_ANSWERED = "SendEmailAlertWhenAnswered";

    public QuestionSubmissionRequest(ConversationsSubmissionRequest.Builder builder) {
        super(builder);
    }

    @Override
    protected String getApiKey() {
        return BVSDK.getInstance().getApiKeys().getApiKeyConversations();
    }

    String getProductId() {
        Map<String, Object> queryParams = makeQueryParams();
        return queryParams.containsKey(kPRODUCT_ID) ? (String) queryParams.get(kPRODUCT_ID) : "";
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {

        Builder builder = (Builder) getBuilder();
        queryParams.put(kPRODUCT_ID, builder.productId);
        queryParams.put(kQUESTION_SUMMARY, builder.questionSummary);
        queryParams.put(kQUESTION_DETAILS, builder.questionDetails);
        queryParams.put(kIS_ANONUSER, builder.isUserAnonymous);
        queryParams.put(kSEND_EMAIL_ANSWERED, builder.sendEmailAlertWhenAnswered);
    }

    @Override
    String getEndPoint() {
        return "submitquestion.json";
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsSubmissionRequest.Builder<Builder> {

        private final String productId;
        private String questionSummary;
        private String questionDetails;
        private Boolean isUserAnonymous;
        private Boolean sendEmailAlertWhenAnswered;

        public Builder(Action action, String productId) {
            super(action);
            this.productId = productId;
        }

        public Builder questionSummary(String questionSummary) {
            this.questionSummary = questionSummary;
            return  this;
        }

        public Builder questionDetails(String questionDetails) {
            this.questionDetails = questionDetails;
            return this;
        }

        public Builder isUserAnonymous(Boolean isUserAnonymous) {
            this.isUserAnonymous = isUserAnonymous;
            return this;
        }

        public Builder sendEmailAlertWhenAnswered(Boolean sendEmailAlertWhenAnswered) {
            this.sendEmailAlertWhenAnswered = sendEmailAlertWhenAnswered;
            return this;
        }

        public QuestionSubmissionRequest build() {
            return new QuestionSubmissionRequest(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            return PhotoUpload.ContentType.Question;
        }
    }
}