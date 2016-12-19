/*******************************************************************************
 * Copyright 2016 Bazaarvoice
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
 ******************************************************************************/


package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Response for a successful FeedbackSubmissionRequest from either a helpfulness vote or inappropriate feedback response.
 */
public class FeedbackSubmissionResponse extends ConversationsResponse {

    @SerializedName("Locale")
    private String locale;
    @SerializedName("SubmissionId")
    private String submissionId;
    @SerializedName("TypicalHoursToPost")
    private Integer typicalHoursToPost;
    @SerializedName("Feedback")
    private SubmittedFeedback feedback;

    public String getLocale() {
        return locale;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public Integer getTypicalHoursToPost() {
        return typicalHoursToPost;
    }

    public SubmittedFeedback getFeedback() {
        return feedback;
    }

}
