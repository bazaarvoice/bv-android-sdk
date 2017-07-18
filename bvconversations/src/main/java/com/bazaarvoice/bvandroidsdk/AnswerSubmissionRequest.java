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

/**
 * Request to submit an {@link Answer}
 */
public class AnswerSubmissionRequest extends ConversationsSubmissionRequest {
    private final String questionId;
    private final String answerText;

    private AnswerSubmissionRequest(Builder builder) {
        super(builder);
        this.questionId = builder.questionId;
        this.answerText = builder.answerText;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    String getQuestionId() {
        return questionId;
    }

    String getAnswerText() {
        return answerText;
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
            return PhotoUpload.ContentType.ANSWER;
        }
    }
}