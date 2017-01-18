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

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;

import java.util.Map;

/**
 * Request class for submitting feedback on {@link Review}s, {@link Question}s,
 * and {@link Answer}s.
 */
public class FeedbackSubmissionRequest extends ConversationsSubmissionRequest {

    private static final String kCONTENT_ID = "ContentId";
    private static final String kCONTENT_TYPE = "ContentType";
    private static final String kFEEDBACK_TYPE = "FeedbackType";
    private static final String kUSER_ID = "UserId";
    private static final String kVOTE = "Vote";
    private static final String kREASON_TEXT = "ReasonText";

    public FeedbackSubmissionRequest(ConversationsSubmissionRequest.Builder builder) {
        super(builder);
    }

    String getContentId() {
        Map<String, Object> queryParams = makeQueryParams();
        return queryParams.containsKey(kCONTENT_ID) ? (String) queryParams.get(kCONTENT_ID) : "";
    }

    String getContentType() {
        Map<String, Object> queryParams = makeQueryParams();
        return queryParams.containsKey(kCONTENT_TYPE) ? (String) queryParams.get(kCONTENT_TYPE) : "";
    }

    String getFeedbackType() {
        Map<String, Object> queryParams = makeQueryParams();
        return queryParams.containsKey(kFEEDBACK_TYPE) ? (String) queryParams.get(kFEEDBACK_TYPE) : "";
    }

    @Override
    protected String getApiKey() {
        return BVSDK.getInstance().getApiKeys().getApiKeyConversations();
    }

    @Override
    void addRequestQueryParams(Map<String, Object> queryParams) {

        FeedbackSubmissionRequest.Builder builder = (FeedbackSubmissionRequest.Builder) getBuilder();
        queryParams.put(kCONTENT_ID, builder.contentId);
        queryParams.put(kCONTENT_TYPE, builder.contentType);
        queryParams.put(kFEEDBACK_TYPE, builder.feedbackType);
        queryParams.put(kUSER_ID, builder.userId);
        queryParams.put(kVOTE, builder.feedbackVote);
        queryParams.put(kREASON_TEXT, builder.reasonFlaggedText);

    }

    @Override
    String getEndPoint() {
        return "submitfeedback.json";
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsSubmissionRequest.Builder<FeedbackSubmissionRequest.Builder> {

        private final String contentId;
        private String contentType;
        private String feedbackType;
        private String userId;
        private String feedbackVote;
        private String reasonFlaggedText;

        public Builder(String contentId) {
            super(Action.Preview);  // Feedback does not support action, so forcing preview will make a proper submission.
            this.contentId = contentId;
        }

        public FeedbackSubmissionRequest.Builder feedbackContentType(FeedbackContentType feedbackContentType) {
            this.contentType = feedbackContentType.getTypeString();
            return this;
        }

        public FeedbackSubmissionRequest.Builder feedbackType(FeedbackType feedbackType) {
            this.feedbackType = feedbackType.getTypeString();
            return this;
        }

        public FeedbackSubmissionRequest.Builder feedbackVote(FeedbackVoteType feedbackVote){
            this.feedbackVote = feedbackVote.getTypeString();
            return this;
        }

        public FeedbackSubmissionRequest.Builder userId(String userId){
            this.userId = userId;
            return this;
        }

        public FeedbackSubmissionRequest.Builder reasonFlaggedText(String reasonFlaggedText){
            this.reasonFlaggedText = reasonFlaggedText;
            return this;
        }

        public FeedbackSubmissionRequest build() {
            return new FeedbackSubmissionRequest(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            // TODO: Return unsupported?
            return PhotoUpload.ContentType.Question;
        }
    }

}
