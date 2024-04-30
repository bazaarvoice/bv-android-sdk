package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Request to start a Progressive Submission Session.
 * This request will return {@link InitiateSubmitResponse}
 * Must be called before {@link ProgressiveSubmitRequest}
 */
public class InitiateSubmitRequest extends ConversationsSubmissionRequest {

    private final boolean isExtended;
    private final boolean isHostedAuth;
    private List<String> productIds;

    private InitiateSubmitRequest(Builder builder) {
        super(builder);
        this.productIds = builder.productIds;
        this.isExtended = builder.isExtended;
        this.isHostedAuth = builder.isHostedAuth;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public boolean isExtended() {
        return isExtended;
    }

    public boolean isHostedAuth() {
        return isHostedAuth;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    public static final class Builder extends ConversationsSubmissionRequest.Builder<Builder> {
        private List<String> productIds;
        private boolean isExtended;
        private boolean isHostedAuth;

        public Builder(@NonNull List<String> productIds, @NonNull String locale) {
            super(Action.Submit);
            this.productIds = productIds;
            this.locale(locale);
        }

        public Builder extended(boolean isExtended) {
            this.isExtended = isExtended;
            return this;
        }

        public Builder hostedAuth(boolean isHostedAuth) {
            this.isHostedAuth = isHostedAuth;
            return this;
        }

        public InitiateSubmitRequest build() {
            return new InitiateSubmitRequest(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            return null;
        }

        @Override
        VideoUpload.ContentType getVideoContentType() {
            return null;
        }
    }
}
