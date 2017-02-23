/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Bazaarvoice Ads SDK class for fetching Bazaarvoice tailored ad content
 */
public class BVAds {
    private static final String TAG = BVAds.class.getName();

    /**
     * Creates a {@code PublishedAdRequest.Builder} that has appropriately set custom targeting
     * values based on BVAds's personalization data. The Builder is returned to allow further
     * use of {@code PublishedAdRequest.Builder} if desired, before passing to
     * {@code getTargetedAdView}, {@code getTargetedInterstitialAd}, or {@code getTargetedAdLoader}.
     * @return PublisherAdRequest.Builder
     */
    public static Map<String, String> getCustomTargeting(){
        BVSDK bvsdk = BVSDK.getInstance();

        String apiKeyShopperAdvertising = bvsdk.getBvUserProvidedData().getBvApiKeys().getApiKeyShopperAdvertising();
        if (apiKeyShopperAdvertising == null || apiKeyShopperAdvertising.isEmpty()) {
            throw new IllegalStateException("BVAds SDK requires a shopper advertising api key");
        }

        ShopperProfile shopperProfile = bvsdk.getAuthenticatedUser().getShopperProfile();
        Map<String, String> targetingKeywords = (shopperProfile == null || shopperProfile.getProfile() == null) ? new HashMap<String, String>() : shopperProfile.getProfile().getTargetingKeywords();

        bvsdk.getBvLogger().d(TAG, "getCustomTargeting() - numTargetingKeywords: " + targetingKeywords.entrySet().size());

        return targetingKeywords;
    }

}
