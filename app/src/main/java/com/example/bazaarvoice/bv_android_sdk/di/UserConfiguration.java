/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.di;

import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;

/**
 * TODO: Description Here
 */
public interface UserConfiguration {

    String provideApiKeyShopperAdvertising();

    String provideApiKeyConversations();

    String provideBannerAdUnitId();

    String provideInterstitialAdUnitId();

    String provideNativeContentAdUnitId();

    BazaarEnvironment provideBazaarEnvironment();

    String provideBvClientId();

    BVRecommendations provideBvRecommendations();

    String provideTestDeviceId();

}
