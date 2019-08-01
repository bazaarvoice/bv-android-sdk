package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * Response data class for {@link InitiateSubmitRequest}
 *  Return the {@link FormData}needed to create the submission form for the product,
 *  the submissionSessionToken needed to submit reviews with {@link ProgressiveSubmitRequest},
 *  and a review summary as {@link Review}
 */
public class InitiateSubmitResponse extends ConversationsSubmissionResponse {

    @SerializedName("response")
    InitiateSubmitFormData data;

    public InitiateSubmitFormData getData() {
        return data;
    }

    public class InitiateSubmitFormData {

        private Map<String, InitiateSubmitResponseData> productFormData;

        public Map<String,InitiateSubmitResponseData> getProductFormData() {
            return productFormData;
        }
    }

    public class InitiateSubmitResponseData {
        private List<String> fieldsOrder;
        private Map<String, FormField> fields;
        private Review review;
        private String submissionSessionToken;

        public Review getReview() {
            return review;
        }

        public List<String> getFieldsOrder() {
            return fieldsOrder;
        }

        public Map<String, FormField> getFields() {
            return fields;
        }

        public String getSubmissionSessionToken() {
            return submissionSessionToken;
        }

        public List<FormField> getFormFields() {
            List<FormField> formFields;
            if (fields == null || fields.isEmpty()) {
                formFields = emptyList();
            } else {
                formFields = new ArrayList<>(fields.values());
            }

            return formFields;
        }

    }

}
