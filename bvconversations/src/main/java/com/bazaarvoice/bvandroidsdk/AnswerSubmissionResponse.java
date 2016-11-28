/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class AnswerSubmissionResponse extends ConversationsResponse {

    @SerializedName("Locale")
    private String locale;
    @SerializedName("SubmissionId")
    private String submissionId;
    @SerializedName("AuthorSubmissionToken")
    private String authorSubmissionToken;
    @SerializedName("TypicalHoursToPost")
    private Integer typicalHoursToPost;
    @SerializedName("Answer")
    private SubmittedAnswer answer;

    public String getLocale() {
        return locale;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public String getAuthorSubmissionToken() {
        return authorSubmissionToken;
    }

    public Integer getTypicalHoursToPost() {
        return typicalHoursToPost;
    }

    public SubmittedAnswer getAnswer() {
        return answer;
    }
}