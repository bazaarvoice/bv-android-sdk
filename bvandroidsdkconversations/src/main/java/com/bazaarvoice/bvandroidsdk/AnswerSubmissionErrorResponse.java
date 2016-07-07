/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TODO: Describe file here.
 */
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

}