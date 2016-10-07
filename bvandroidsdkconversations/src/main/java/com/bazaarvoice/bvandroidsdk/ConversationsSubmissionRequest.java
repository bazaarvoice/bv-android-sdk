/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.Utils.mapPutSafe;

/**
 * TODO: Describe file here. asldkfjalksdjflkasjdflkasjdflkj
 */
abstract class ConversationsSubmissionRequest extends ConversationsRequest {

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

    ConversationsSubmissionRequest(Builder builder) {
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

    String getFingerprint() {
        Map<String, Object> queryParams = makeQueryParams();
        return queryParams.containsKey(kFINGER_PRINT) ? (String) queryParams.get(kFINGER_PRINT) : "";
    }

    Map<String, Object> makeQueryParams() {
        Map<String, Object> params = makeCommonQueryParams();
        addRequestQueryParams(params);
        return params;
    }
    private Map<String, Object> makeCommonQueryParams() {
        Map<String, Object> params = new HashMap<>();
        mapPutSafe(params, kAPI_VERSION, API_VERSION);
        mapPutSafe(params, kPASS_KEY, getApiKey());
        mapPutSafe(params, kAPP_ID, BVSDK.getInstance().analyticsManager.getPackageName());
        mapPutSafe(params, kAPP_VERSION, BVSDK.getInstance().analyticsManager.getVersionName());
        mapPutSafe(params, kBUILD_NUM, BVSDK.getInstance().analyticsManager.getVersionCode());
        mapPutSafe(params, kSDK_VERSION, BVSDK.SDK_VERSION);
        mapPutSafe(params, kCAMPAIGN_ID, builder.campaignId);
        mapPutSafe(params, kFINGER_PRINT, builder.fingerPrint);
        mapPutSafe(params, kHOSTED_AUTH_EMAIL, builder.hostedAuthenticationEmail);
        mapPutSafe(params, kHOST_AUTH_CALLBACK, builder.hostedAuthenticationCallback);
        mapPutSafe(params, kLOCALE, builder.locale);
        mapPutSafe(params, kUSER, builder.user);
        mapPutSafe(params, kUSER_EMAIL, builder.userEmail);
        mapPutSafe(params, kUSER_ID, builder.userId);
        mapPutSafe(params, kUSER_LOCATION, builder.userLocation);
        mapPutSafe(params, kUSER_NICKNAME, builder.userNickname);
        mapPutSafe(params, kSEND_EMAIL_PUBLISHED, builder.sendEmailAlertWhenPublished);
        mapPutSafe(params, kAGREE_TERMS, builder.agreedToTermsAndConditions);

        if (forcePreview){
            mapPutSafe(params, kACTION, Action.Preview.getKey());
        }else {
            mapPutSafe(params, kACTION, builder.getAction().getKey());
        }

        if (photos != null) {
            int idx = 0;
            for (Photo photo : photos) {
                mapPutSafe(params, "photourl_" + idx, photo.getContent().getNormalUrl());
                mapPutSafe(params, "photocaption_" + idx, photo.getCaption());
                idx++;
            }
        }

        return params;
    }

    protected abstract String getApiKey();

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
        transient final List<PhotoUpload> photoUploads = new ArrayList<>();

        abstract PhotoUpload.ContentType getPhotoContentType();

        Builder(Action action) {
            this.action = action;
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
        Action getAction() {
            return action;
        }

        public BuilderChildType addPhoto(File photo, String caption) {
            PhotoUpload upload = new PhotoUpload(photo, caption, getPhotoContentType());
            photoUploads.add(upload);
            return (BuilderChildType)this;
        }
    }
}