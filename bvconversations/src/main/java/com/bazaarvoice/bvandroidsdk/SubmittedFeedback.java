/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Feedback model for a successful Conversations Feedback Submission on a Review, Question, or Answer.
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



