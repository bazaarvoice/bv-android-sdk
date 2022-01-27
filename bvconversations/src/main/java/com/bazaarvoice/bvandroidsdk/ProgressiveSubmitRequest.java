package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Request to submit a progressiveSubmission.
 *  A {@link ProgressiveSubmitRequest#submissionSessionToken} is obtained by
 * {@link InitiateSubmitResponse} or {@link ProgressiveSubmitResponse}
 */
public class ProgressiveSubmitRequest extends ConversationsSubmissionRequest{

    private String productId;
    private Map<String, Object> submissionFields;
    private String submissionSessionToken;
    private boolean isPreview;
    private boolean includeFields;
    private boolean isHostedAuth;

    ProgressiveSubmitRequest(Builder builder) {
        super(builder);
        this.productId = builder.productId;
        this.submissionFields = builder.submissionFields;
        this.submissionSessionToken = builder.submissionSessionToken;
        this.isPreview = builder.isPreview;
        this.includeFields = builder.includeFields;
        this.isHostedAuth = builder.isHostedAuth;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public String getProductId() {
        return productId;
    }

    public Map<String, Object> getSubmissionFields() {
        return submissionFields;
    }

    public String getSubmissionSessionToken() {
        return submissionSessionToken;
    }

    public boolean includeFields() {
        return includeFields;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public boolean isHostedAuth() {
        return isHostedAuth;
    }


    public static final class Builder extends ConversationsSubmissionRequest.Builder<Builder> {

        private String productId;
        private String submissionSessionToken;
        private  boolean isHostedAuth;
        private Map<String, Object> submissionFields = new HashMap<>();
        private boolean isPreview;
        private boolean includeFields;

        public Builder(@NonNull String productId, @NonNull String submissionSessionToken, @NonNull String locale) {
            super(Action.Submit);
            this.productId = productId;
            this.submissionSessionToken = submissionSessionToken;
            this.locale(locale);
        }

        public Builder submissionFields(@NonNull Map<String, Object> submissionFields) {
            this.submissionFields = submissionFields;
            return this;
        }

        public Builder isPreview(boolean isPreview) {
            this.isPreview = isPreview;
            return this;
        }

        public Builder hostedAuth(boolean isHostedAuth) {
            this.isHostedAuth = isHostedAuth;
            return this;
        }

        public Builder includeFields(boolean includeFields) {
            this.includeFields = includeFields;
            return this;
        }

        public ProgressiveSubmitRequest build() {
            return new ProgressiveSubmitRequest(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            return null;
        }
    }
}
