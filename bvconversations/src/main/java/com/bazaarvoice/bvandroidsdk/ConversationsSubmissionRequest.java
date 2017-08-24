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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Base options for a Conversations submission request
 */
abstract class ConversationsSubmissionRequest extends ConversationsRequest {
    private final Builder builder;
    private List<Photo> photos;
    private boolean forcePreview;

    // region Builder Fields
    private final String campaignId;
    private final String fingerPrint;
    private final String hostedAuthenticationEmail;
    private final String hostedAuthenticationCallback;
    private final String locale;
    private final String user;
    private final String userEmail;
    private final String userId;
    private final String userLocation;
    private final String userNickname;
    private final Boolean sendEmailAlertWhenPublished;
    private final Boolean agreedToTermsAndConditions;
    private final Action action;
    private final List<FormPair> formPairs;
    private final List<PhotoUpload> photoUploads;
    // endregion

    ConversationsSubmissionRequest(Builder builder) {
        this.builder = builder;
        this.campaignId = builder.campaignId;
        this.fingerPrint = builder.fingerPrint;
        this.hostedAuthenticationEmail = builder.hostedAuthenticationEmail;
        this.hostedAuthenticationCallback = builder.hostedAuthenticationCallback;
        this.locale = builder.locale;
        this.user = builder.user;
        this.userEmail = builder.userEmail;
        this.userId = builder.userId;
        this.userLocation = builder.userLocation;
        this.userNickname = builder.userNickname;
        this.sendEmailAlertWhenPublished = builder.sendEmailAlertWhenPublished;
        this.agreedToTermsAndConditions = builder.agreedToTermsAndConditions;
        this.action = builder.action;
        this.formPairs = builder.formPairs;
        this.photoUploads = builder.photoUploads;
    }

    Builder getBuilder() {
        return this.builder;
    }

    void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    // TODO: Remove this stateful logic from the request object
    void setForcePreview(boolean forcePreview) {
        this.forcePreview = forcePreview;
    }

    boolean getForcePreview() {
        return this.forcePreview;
    }

    List<Photo> getPhotos() {
        return photos;
    }

    boolean isForcePreview() {
        return forcePreview;
    }

    String getCampaignId() {
        return campaignId;
    }

    String getFingerPrint() {
        return fingerPrint;
    }

    String getHostedAuthenticationEmail() {
        return hostedAuthenticationEmail;
    }

    String getHostedAuthenticationCallback() {
        return hostedAuthenticationCallback;
    }

    String getLocale() {
        return locale;
    }

    String getUser() {
        return user;
    }

    String getUserEmail() {
        return userEmail;
    }

    String getUserId() {
        return userId;
    }

    String getUserLocation() {
        return userLocation;
    }

    String getUserNickname() {
        return userNickname;
    }

    Boolean getSendEmailAlertWhenPublished() {
        return sendEmailAlertWhenPublished;
    }

    Boolean getAgreedToTermsAndConditions() {
        return agreedToTermsAndConditions;
    }

    Action getAction() {
        return action;
    }

    List<FormPair> getFormPairs() {
        return formPairs;
    }

    List<PhotoUpload> getPhotoUploads() {
        return photoUploads;
    }

    static class FormPair {
        private final String key, value;

        public FormPair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public abstract static class Builder<BuilderChildType extends Builder> {
        private String campaignId;
        private String fingerPrint;
        private String hostedAuthenticationEmail;
        private String hostedAuthenticationCallback;
        private String locale;
        private String user;
        private String userEmail;
        private String userId;
        private String userLocation;
        private String userNickname;
        private Boolean sendEmailAlertWhenPublished;
        private Boolean agreedToTermsAndConditions;
        private final Action action;
        private final List<FormPair> formPairs;
        transient final List<PhotoUpload> photoUploads = new ArrayList<>();

        abstract PhotoUpload.ContentType getPhotoContentType();

        Builder(Action action) {
            this.action = action;
            this.formPairs = new ArrayList<>();
        }

        /**
         * This method adds extra user provided form parameters to a
         * submission request, and will be encoded with MediaType,
         * application/x-www-form-urlencoded.
         *
         * @param key Custom non-encoded url query param key
         * @param value Custom non-encoded url query param value
         * @return The Builder
         */
        public BuilderChildType addCustomSubmissionParameter(String key, String value) {
            final FormPair formPair = new FormPair(key, value);
            formPairs.add(formPair);
            return (BuilderChildType) this;
        }

        public BuilderChildType campaignId(String campaignId) {
            this.campaignId = campaignId;
            return (BuilderChildType)this;
        }

        public BuilderChildType fingerPrint(String fingerPrint) {
            this.fingerPrint = fingerPrint;
            return (BuilderChildType)this;
        }

        public BuilderChildType hostedAuthenticationEmail(String hostedAuthenticationEmail) {
            this.hostedAuthenticationEmail = hostedAuthenticationEmail;
            return (BuilderChildType)this;
        }

        public BuilderChildType hostedAuthenticationCallback(String hostedAuthenticationCallback) {
            this.hostedAuthenticationCallback = hostedAuthenticationCallback;
            return (BuilderChildType)this;
        }

        public BuilderChildType locale(String locale) {
            this.locale = locale;
            return (BuilderChildType)this;
        }

        public BuilderChildType user(String user) {
            this.user = user;
            return (BuilderChildType)this;
        }

        public BuilderChildType userEmail(String userEmail) {
            this.userEmail = userEmail;
            return (BuilderChildType)this;
        }

        public BuilderChildType userId(String userId) {
            this.userId = userId;
            return (BuilderChildType)this;
        }

        public BuilderChildType userLocation(String userLocation) {
            this.userLocation = userLocation;
            return (BuilderChildType)this;
        }

        public BuilderChildType userNickname(String userNickname) {
            this.userNickname = userNickname;
            return (BuilderChildType)this;
        }

        public BuilderChildType agreedToTermsAndConditions(Boolean agreedToTermsAndConditions) {
            this.agreedToTermsAndConditions = agreedToTermsAndConditions;
            return (BuilderChildType)this;
        }

        public BuilderChildType sendEmailAlertWhenPublished(Boolean sendEmailAlertWhenPublished) {
            this.sendEmailAlertWhenPublished = sendEmailAlertWhenPublished;
            return (BuilderChildType)this;
        }

        /**
         * TODO: Need the ability for users to add whatever form params they want.
         * addAdditionalParams is actually something specific to review submission
         * So we still need a name for a generic submission method. Display requests
         * unknowingly supported extra query params with the name addAdditionalField
          */

        /**
         * Only available for Review, Question, and Answer unless PRR.
         * If PRR, then it's available for ReviewComments as well.
         *
         * @param photo
         * @param caption
         * @return
         */
        public BuilderChildType addPhoto(File photo, String caption) {
            PhotoUpload upload = new PhotoUpload(photo, caption, getPhotoContentType());
            photoUploads.add(upload);
            return (BuilderChildType)this;
        }
    }
}