/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.di;

import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;

/**
 * TODO: Description Here
 */
public class DemoUserConfigurationImpl implements DemoUserConfiguration {

    /**
     *Anything assigned to REPLACE_ME in this file should be replaced with your corresponding values
     */
    public static final String REPLACE_ME = "REPLACE_ME";


    /**
     *  Ads/Recommendations
     */

    // User auth string pre-populated with a small profile
    // interested in men's and women's apparel -- for testing and demonstration purposes
    public static final String BV_USER_AUTH_STRING = "0ce436b29697d6bc74f30f724b9b0bb6646174653d31323334267573657269643d5265636f6d6d656e646174696f6e7353646b54657374";


    private static final String SHOPPER_ADVERTISING_API_KEY = REPLACE_ME;

    /**
     * If using a physical test device Google DFP requires this test device id, and
     * "The deviceId can be obtained by viewing the logcat output after creating a new ad".
     * This is not necessary for emulator testing.
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/ads/AdRequest.Builder#addTestDevice(java.lang.String)">https://developers.google.com/android/reference/com/google/android/gms/ads/AdRequest.Builder#addTestDevice(java.lang.String)</a>
     */
    private static final String BV_TEST_DEVICE_ID = REPLACE_ME;

    /**
     * Ads
     */

    // Google DFP demo ad unit ids
    private static final String BANNER_AD_UNIT_ID = "/6449/example/banner";
    private static final String INTERSTITIAL_AD_UNIT_ID = "/6449/example/interstitial";
    private static final String NATIVE_CONTENT_AD_UNIT_ID = "/6449/example/native";

    /**
     * Conversations
      */

    private static final String BV_CONVERSATIONS_API_KEY = REPLACE_ME;


    /**
     * Curations
     */

    private static final String BV_CURATIONS_API_KEY = REPLACE_ME;

    /**
     * Ads/Recommendations/Conversations
     */

    private static final String BV_CLIENT_ID = REPLACE_ME;
    private static final BazaarEnvironment BV_ENVIRONMENT = BazaarEnvironment.STAGING; // BazaarEnvironment.PRODUCTION;

    @Override
    public String provideApiKeyShopperAdvertising() {
        return SHOPPER_ADVERTISING_API_KEY;
    }

    @Override
    public String provideApiKeyConversations() {
        return BV_CONVERSATIONS_API_KEY;
    }

    @Override
    public String provideApiKeyCurations() {
        return BV_CURATIONS_API_KEY;
    }

    @Override
    public String provideBannerAdUnitId() {
        return BANNER_AD_UNIT_ID;
    }

    @Override
    public String provideInterstitialAdUnitId() {
        return INTERSTITIAL_AD_UNIT_ID;
    }

    @Override
    public String provideNativeContentAdUnitId() {
        return NATIVE_CONTENT_AD_UNIT_ID;
    }

    @Override
    public BazaarEnvironment provideBazaarEnvironment() {
        return BV_ENVIRONMENT;
    }

    @Override
    public String provideBvClientId() {
        return BV_CLIENT_ID;
    }

    @Override
    public String provideTestDeviceId() {
        return BV_TEST_DEVICE_ID;
    }

}

