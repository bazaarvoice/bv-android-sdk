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

import java.util.Date;

/**
 * {@link Answer} model for a successful {@link AnswerSubmissionResponse}
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