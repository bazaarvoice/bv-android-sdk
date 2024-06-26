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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Base options for a Conversations submission request
 */
abstract class ConversationsSubmissionRequest extends ConversationsRequest {
    private final Builder builder;
    private List<Photo> photos;

    private List<Video> videos;
    private boolean forcePreview;

    // region Builder Fields
    private final String campaignId;
    private final String fingerPrint;
    private final AuthenticationProvider authenticationProvider;
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
    private final List<VideoUpload> videoUploads;
    // endregion

    ConversationsSubmissionRequest(Builder builder) {
        this.builder = builder;
        this.campaignId = builder.campaignId;
        this.fingerPrint = builder.fingerPrint;
        this.authenticationProvider = builder.authenticationProvider;
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
        this.videoUploads = builder.videoUploads;
    }

    Builder getBuilder() {
        return this.builder;
    }

    void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    void setVideos(List<Video> videos) {
        this.videos = videos;
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

    List<Video> getVideos() {
        return videos;
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

    AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
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

    List<VideoUpload> getVideoUploads() {
        return videoUploads;
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
        private AuthenticationProvider authenticationProvider;
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

        transient final List<VideoUpload> videoUploads = new ArrayList<>();

        abstract PhotoUpload.ContentType getPhotoContentType();

        abstract VideoUpload.ContentType getVideoContentType();

        Builder(@NonNull Action action) {
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
        public BuilderChildType addCustomSubmissionParameter(@NonNull String key, @NonNull String value) {
            final FormPair formPair = new FormPair(key, value);
            formPairs.add(formPair);
            return (BuilderChildType) this;
        }

        public BuilderChildType campaignId(@NonNull String campaignId) {
            this.campaignId = campaignId;
            return (BuilderChildType)this;
        }

        public BuilderChildType fingerPrint(@NonNull String fingerPrint) {
            this.fingerPrint = fingerPrint;
            return (BuilderChildType)this;
        }

        public BuilderChildType authenticationProvider(@NonNull AuthenticationProvider authenticationProvider) {
            this.authenticationProvider = authenticationProvider;
            return (BuilderChildType) this;
        }

        /**
         * @deprecated Use {@link #authenticationProvider(AuthenticationProvider)} instead
         *
         * @param hostedAuthenticationEmail The email address that a user will be sent a confirmation email to
         * @return This builder
         */
        public BuilderChildType hostedAuthenticationEmail(@NonNull String hostedAuthenticationEmail) {
            this.hostedAuthenticationEmail = hostedAuthenticationEmail;
            return (BuilderChildType)this;
        }

        /**
         * @deprecated Use {@link #authenticationProvider(AuthenticationProvider)} instead
         *
         * @param hostedAuthenticationCallback The URL that BV will add to the confirmation email for a user to
         *                                     tap/click
         * @return This builder
         */
        public BuilderChildType hostedAuthenticationCallback(@NonNull String hostedAuthenticationCallback) {
            this.hostedAuthenticationCallback = hostedAuthenticationCallback;
            return (BuilderChildType)this;
        }

        public BuilderChildType locale(@NonNull String locale) {
            this.locale = locale;
            return (BuilderChildType)this;
        }

        /**
         * @deprecated Use {@link #authenticationProvider(AuthenticationProvider)} instead
         *
         * @param user The User Authentication String (UAS) for a user that has granted this application permission
         *             to submit on their behalf.
         * @return This builder
         */
        public BuilderChildType user(@NonNull String user) {
            this.user = user;
            return (BuilderChildType)this;
        }

        public BuilderChildType userEmail(@NonNull String userEmail) {
            this.userEmail = userEmail;
            return (BuilderChildType)this;
        }

        public BuilderChildType userId(@NonNull String userId) {
            this.userId = userId;
            return (BuilderChildType)this;
        }

        public BuilderChildType userLocation(@NonNull String userLocation) {
            this.userLocation = userLocation;
            return (BuilderChildType)this;
        }

        public BuilderChildType userNickname(@NonNull String userNickname) {
            this.userNickname = userNickname;
            return (BuilderChildType)this;
        }

        public BuilderChildType agreedToTermsAndConditions(@NonNull Boolean agreedToTermsAndConditions) {
            this.agreedToTermsAndConditions = agreedToTermsAndConditions;
            return (BuilderChildType)this;
        }

        public BuilderChildType sendEmailAlertWhenPublished(@NonNull Boolean sendEmailAlertWhenPublished) {
            this.sendEmailAlertWhenPublished = sendEmailAlertWhenPublished;
            return (BuilderChildType)this;
        }

        /**
         * Only available for Review, Question, and Answer unless PRR.
         * If PRR, then it's available for ReviewComments as well.
         *
         * @param photo
         * @param caption
         * @return
         */
        public BuilderChildType addPhoto(@NonNull File photo, @Nullable String caption) {
            PhotoUpload upload = new PhotoUpload(photo, caption, getPhotoContentType());
            photoUploads.add(upload);
            return (BuilderChildType)this;
        }

        public BuilderChildType addVideo(@NonNull File video, @Nullable String caption) {
            VideoUpload upload = new VideoUpload(video, caption, getVideoContentType());
            videoUploads.add(upload);
            return (BuilderChildType)this;
        }
    }
}