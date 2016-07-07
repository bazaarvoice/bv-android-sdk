/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * TODO: Describe file here.
 */
public class SubmittedAnswer {
    @SerializedName("AnswerText")
    private String answerText;
    @SerializedName("SubmissionId")
    private String submissionId;
    @SerializedName("AnswerId")
    private String answerId;
    @SerializedName("SendEmailAlertWhenAnswered")
    private Boolean sendEmailAlertWhenAnswered;
    @SerializedName("SubmissionTime")
    private String submissionTime;
    @SerializedName("TypicalHoursToPost")
    private Integer typicalHoursToPost;

    private Date submissionDate;

    public String getAnswerText() {
        return answerText;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public Boolean getSendEmailAlertWhenAnswered() {
        return sendEmailAlertWhenAnswered;
    }

    public Integer getTypicalHoursToPost() {
        return typicalHoursToPost;
    }

    public Date getSubmissionDate() {

        if (submissionDate == null && submissionTime != null) {
            submissionDate = DateUtil.dateFromString(submissionTime);
        }

        return submissionDate;
    }
}