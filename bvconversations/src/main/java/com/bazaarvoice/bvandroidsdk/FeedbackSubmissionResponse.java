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
 * Response for a successful FeedbackSubmissionRequest from either
 * a helpfulness vote or inappropriate feedback response.
 */
public class FeedbackSubmissionResponse extends ConversationsSubmissionResponse {
    @SerializedName("Locale") private String locale;
    @SerializedName("Feedback") private SubmittedFeedback feedback;

    public String getLocale() {
        return locale;
    }

    public SubmittedFeedback getFeedback() {
        return feedback;
    }
}
