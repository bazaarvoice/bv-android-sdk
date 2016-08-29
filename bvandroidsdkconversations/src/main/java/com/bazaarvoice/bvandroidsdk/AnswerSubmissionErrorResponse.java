/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerSubmissionErrorResponse {
    @SerializedName("Locale")
    private String locale;
    @SerializedName("SubmissionId")
    private String submissionId;
    @SerializedName("AuthorSubmissionToken")
    private String authorSubmissionToken;
    @SerializedName("Errors")
    private List<Error> errors;
    @SerializedName("HasErrors")
    private Boolean hasErrors;
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

    public List<Error> getErrors() {
        return errors;
    }

    public Boolean getHasErrors() {
        return hasErrors;
    }

    public Integer getTypicalHoursToPost() {
        return typicalHoursToPost;
    }

    public SubmittedAnswer getAnswer() {
        return answer;
    }
}