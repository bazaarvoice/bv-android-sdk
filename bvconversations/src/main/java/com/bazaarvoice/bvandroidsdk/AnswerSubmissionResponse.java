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
 * Response for an {@link AnswerSubmissionRequest}
 */
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