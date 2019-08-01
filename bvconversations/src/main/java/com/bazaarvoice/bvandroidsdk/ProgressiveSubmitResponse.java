package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProgressiveSubmitResponse extends ConversationsResponse {

    @SerializedName("response")
    private ProgressiveSubmitResponseData data;

    public ProgressiveSubmitResponseData getData() {
        return data;
    }

    public class ProgressiveSubmitResponseData {

        private Boolean isFormComplete;
        private String submissionSessionToken;
        private Review review;
        private String submissionId;
        List<FieldError> formValidationErrors;

        public String getSubmissionSessionToken() {
            return submissionSessionToken;
        }

        public Review getReview() {
            return review;
        }

        public String getSubmissionId() {
            return submissionId;
        }

        public List<FieldError> getFormValidationErrors() {
            return formValidationErrors;
        }

        public Boolean isFormComplete() {
            return isFormComplete;
        }
    }
}
