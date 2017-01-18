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
 * Feedback model for a successful {@link FeedbackSubmissionResponse}
 */
public final class SubmittedFeedback {

    @SerializedName("Inappropriate")
    private InappropriateFeedback inappropriateFeedback;

    @SerializedName("Helpfulness")
    private HelpfulnessFeedback helpfulnessFeedback;

    public InappropriateFeedback getInappropriateFeedback() {
        return inappropriateFeedback;
    }

    public HelpfulnessFeedback getHelpfulnessFeedback() {
        return helpfulnessFeedback;
    }

    static final class InappropriateFeedback {

        @SerializedName("ReasonText")
        private String reasonText;

        @SerializedName("AuthorId")
        private String authorId;

        public String getReasonText() {
            return reasonText;
        }

        public String getAuthorId() {
            return authorId;
        }
    }

    static final class HelpfulnessFeedback {

        @SerializedName("Vote")
        private String vote;

        @SerializedName("AuthorId")
        private String authorId;

        public String getVote() {
            return vote;
        }

        public String getAuthorId() {
            return authorId;
        }
    }

}



