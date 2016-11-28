/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

class MobileAppLifecycleSchema extends BvAnalyticsSchema {

    private static final String KEY_APP_MOBILE_OS = "mobileOS";
    private static final String KEY_APP_MOBILE_OS_VERSION = "mobileOSVersion";
    private static final String KEY_APP_MOBILE_DEVICE_NAME = "mobileDeviceName";
    private static final String KEY_APP_MOBILE_APP_IDENTIFIER = "mobileAppIdentifier";
    private static final String KEY_APP_MOBILE_APP_VERSION = "mobileAppVersion";
    private static final String KEY_APP_BV_SDK_VERSION = "bvSDKVersion";
    private static final String KEY_APP_STATE = "appState";
    private static final String eventClass = "Lifecycle";
    private static final String eventType = "MobileApp";
    private static final String source = "mobile-lifecycle";

    private MobileAppLifecycleSchema(Builder builder) {
        super(eventClass, eventType, source);
        addKeyVal(KEY_APP_STATE, builder.appState.getValue());
        addKeyVal(KEY_APP_STATE, builder.appState.getValue());
        addKeyVal(KEY_APP_MOBILE_OS, mobileOS);
        addKeyVal(KEY_APP_MOBILE_OS_VERSION, builder.mobileOSVersion);
        addKeyVal(KEY_APP_MOBILE_DEVICE_NAME, builder.mobileDeviceName);
        addKeyVal(KEY_APP_MOBILE_APP_IDENTIFIER, builder.mobileAppIdentifier);
        addKeyVal(KEY_APP_MOBILE_APP_VERSION, builder.mobileAppVersion);
        addKeyVal(KEY_APP_BV_SDK_VERSION, builder.bvSDKVersion);

        addPartialSchema(builder.magpieMobileAppPartialSchema);
        addPartialSchema(builder.profileCommonPartialSchema);
    }

    public enum AppState {
        LAUNCHED("launched"), PAUSED("background"), RESUMED("active");

        private String value;

        AppState(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * The name of the operating system this event is coming from. Ex: 'iOS', 'Android', etc.
     */
    private static final String mobileOS = "Android";

    public static final class Builder {
        private AppState appState;
        private String mobileOSVersion;
        private String mobileDeviceName;
        private String mobileAppIdentifier;
        private String mobileAppVersion;
        private String bvSDKVersion;
        private MagpieMobileAppPartialSchema magpieMobileAppPartialSchema;
        private ProfileCommonPartialSchema profileCommonPartialSchema;

        public Builder(AppState appState, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, ProfileCommonPartialSchema profileCommonPartialSchema) {
            this.appState = appState;
            this.magpieMobileAppPartialSchema = magpieMobileAppPartialSchema;
            this.profileCommonPartialSchema = profileCommonPartialSchema;
        }

        public Builder mobileOSVersion(String val) {
            mobileOSVersion = val;
            return this;
        }

        public Builder mobileDeviceName(String val) {
            mobileDeviceName = val;
            return this;
        }

        public Builder mobileAppIdentifier(String val) {
            mobileAppIdentifier = val;
            return this;
        }

        public Builder mobileAppVersion(String val) {
            mobileAppVersion = val;
            return this;
        }

        public Builder bvSDKVersion(String val) {
            bvSDKVersion = val;
            return this;
        }

        public MobileAppLifecycleSchema build() {
            return new MobileAppLifecycleSchema(this);
        }
    }
}
