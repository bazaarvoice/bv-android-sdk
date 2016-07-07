/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Describe file here.
 */
abstract class ConversationsSubmission extends ConversationsBase{

    private static final String kCAMPAIGN_ID = "campaignid";
    private static final String kFINGER_PRINT = "fp";
    private static final String kHOSTED_AUTH_EMAIL = "hostedauthentication_authenticationemail";
    private static final String kHOST_AUTH_CALLBACK = "hostedauthentication_callbackurl";
    private static final String kLOCALE = "locale";
    private static final String kUSER = "User";
    private static final String kUSER_EMAIL = "UserEmail";
    private static final String kUSER_ID = "UserId";
    private static final String kUSER_LOCATION = "UserLocation";
    private static final String kUSER_NICKNAME = "UserNickname";
    private static final String kSEND_EMAIL_PUBLISHED = "sendemailalertwhenpublished";
    private static final String kAGREE_TERMS = "agreedToTermsAndConditions";
    private static final String kACTION = "action";

    private final Builder builder;
    private String urlQueryString;
    private List<Photo> photos;
    private boolean forcePreview;

    ConversationsSubmission(Builder builder) {
        this.builder = builder;
    }

    Builder getBuilder() {
        return this.builder;
    }

    void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    void setForcePreview(boolean forcePreview) {
        this.forcePreview = forcePreview;
    }

    boolean getForcePreview() {
        return this.forcePreview;
    }

    Map<String, Object> makeQueryParams() {
        Map<String, Object> params = makeCommonQueryParams();
        addRequestQueryParams(params);
        return params;
    }
    private Map<String, Object> makeCommonQueryParams() {
        Map<String, Object> params = new HashMap<>();
        params.put(kAPI_VERSION, API_VERSION);
        params.put(kPASS_KEY, BVSDK.getInstance().getApiKeyConversations());
        params.put(kAPP_ID, BVSDK.getInstance().analyticsManager.getPackageName());
        params.put(kAPP_VERSION, BVSDK.getInstance().analyticsManager.getVersionName());
        params.put(kBUILD_NUM, BVSDK.getInstance().analyticsManager.getVersionCode());
        params.put(kSDK_VERSION, BVSDK.SDK_VERSION);
        params.put(kCAMPAIGN_ID, builder.campaignId);
        params.put(kFINGER_PRINT, builder.fingerPrint);
        params.put(kHOSTED_AUTH_EMAIL, builder.hostedAuthenticationEmail);
        params.put(kHOST_AUTH_CALLBACK, builder.hostedAuthenticationCallback);
        params.put(kLOCALE, builder.locale);
        params.put(kUSER, builder.user);
        params.put(kUSER_EMAIL, builder.userEmail);
        params.put(kUSER_ID, builder.userId);
        params.put(kUSER_LOCATION, builder.userLocation);
        params.put(kUSER_NICKNAME, builder.userNickname);
        params.put(kSEND_EMAIL_PUBLISHED, builder.sendEmailAlertWhenPublished);
        params.put(kAGREE_TERMS, builder.agreedToTermsAndConditions);

        if (forcePreview){
            params.put(kACTION, Action.Preview.getKey());
        }else {
            params.put(kACTION, builder.getAction().getKey());
        }

        if (photos != null) {
            int idx = 0;
            for (Photo photo : photos) {
                params.put("photourl_" + idx, photo.getContent().getNormalUrl());
                params.put("photocaption_" + idx, photo.getCaption());
                idx++;
            }
        }

        return params;
    }

    private String createUrlQueryString(Map<String, Object> queryParams) {

        StringBuilder builder = new StringBuilder();
        for (String key : queryParams.keySet()) {
            if (queryParams.get(key) != null) {
                builder.append(String.format("%s=%s", key, queryParams.get(key)));
                builder.append("&");
            }
        }

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    abstract void addRequestQueryParams(Map<String, Object> queryParams);

    String getUrlQueryString() {
        final Map<String, Object> queryParams = makeQueryParams();
        urlQueryString = createUrlQueryString(queryParams);
        return urlQueryString;
    }

    public abstract static class Builder<T> {
        private String passKey = BVSDK.getInstance().getApiKeyConversations();
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
        transient final List<PhotoUpload> photoUploads = new ArrayList<>();

        abstract PhotoUpload.ContentType getPhotoContentType();

        Builder(Action action) {
            this.action = action;
        }

        public T campaignId(String campaignId) {
            this.campaignId = campaignId;
            return (T)this;
        }

        public T fingerPrint(String fingerPrint) {
            this.fingerPrint = fingerPrint;
            return (T)this;
        }

        public T hostedAuthenticationEmail(String hostedAuthenticationEmail) {
            this.hostedAuthenticationEmail = hostedAuthenticationEmail;
            return (T)this;
        }

        public T hostedAuthenticationCallback(String hostedAuthenticationCallback) {
            this.hostedAuthenticationCallback = hostedAuthenticationCallback;
            return (T)this;
        }

        public T locale(String locale) {
            this.locale = locale;
            return (T)this;
        }

        public T user(String user) {
            this.user = user;
            return (T)this;
        }

        public T userEmail(String userEmail) {
            this.userEmail = userEmail;
            return (T)this;
        }

        public T userId(String userId) {
            this.userId = userId;
            return (T)this;
        }

        public T userLocation(String userLocation) {
            this.userLocation = userLocation;
            return (T)this;
        }

        public T userNickname(String userNickname) {
            this.userNickname = userNickname;
            return (T)this;
        }

        public T agreedToTermsAndConditions(Boolean agreedToTermsAndConditions) {
            this.agreedToTermsAndConditions = agreedToTermsAndConditions;
            return (T)this;
        }

        public T sendEmailAlertWhenPublished(Boolean sendEmailAlertWhenPublished) {
            this.sendEmailAlertWhenPublished = sendEmailAlertWhenPublished;
            return (T)this;
        }
        Action getAction() {
            return action;
        }

        public T addPhoto(File photo, String caption) {
            PhotoUpload upload = new PhotoUpload(photo, caption, getPhotoContentType());
            photoUploads.add(upload);
            return (T)this;
        }
    }
}