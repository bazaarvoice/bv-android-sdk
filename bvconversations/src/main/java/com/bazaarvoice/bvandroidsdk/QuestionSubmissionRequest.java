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

import androidx.annotation.NonNull;

/**
 * Request for submitting {@link Question}s
 */
public class QuestionSubmissionRequest extends ConversationsSubmissionRequest {
    private final String productId;
    private final String questionSummary;
    private final String questionDetails;
    private final Boolean isUserAnonymous;
    private final Boolean sendEmailAlertWhenAnswered;

    private QuestionSubmissionRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.questionSummary = builder.questionSummary;
        this.questionDetails = builder.questionDetails;
        this.isUserAnonymous = builder.isUserAnonymous;
        this.sendEmailAlertWhenAnswered = builder.sendEmailAlertWhenAnswered;
    }

    String getProductId() {
        return productId;
    }

    public String getQuestionSummary() {
        return questionSummary;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public Boolean getUserAnonymous() {
        return isUserAnonymous;
    }

    public Boolean getSendEmailAlertWhenAnswered() {
        return sendEmailAlertWhenAnswered;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsSubmissionRequest.Builder<Builder> {

        final String productId;
        String questionSummary;
        String questionDetails;
        Boolean isUserAnonymous;
        Boolean sendEmailAlertWhenAnswered;

        public Builder(@NonNull Action action, @NonNull String productId) {
            super(action);
            this.productId = productId;
        }

        public Builder questionSummary(@NonNull String questionSummary) {
            this.questionSummary = questionSummary;
            return  this;
        }

        public Builder questionDetails(@NonNull String questionDetails) {
            this.questionDetails = questionDetails;
            return this;
        }

        public Builder isUserAnonymous(@NonNull Boolean isUserAnonymous) {
            this.isUserAnonymous = isUserAnonymous;
            return this;
        }

        public Builder sendEmailAlertWhenAnswered(@NonNull Boolean sendEmailAlertWhenAnswered) {
            this.sendEmailAlertWhenAnswered = sendEmailAlertWhenAnswered;
            return this;
        }

        public QuestionSubmissionRequest build() {
            return new QuestionSubmissionRequest(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            return PhotoUpload.ContentType.QUESTION;
        }
    }
}