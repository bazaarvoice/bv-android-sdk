/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.HashMap;
import java.util.Map;

/**
 * Bazaarvoice Ads SDK class for fetching Bazaarvoice tailored ad content
 */
public class BVAds implements Ads {
    private static final String TAG = BVAds.class.getName();
    private boolean previousValueOfLimitAdTracking;
    protected BVAuthenticatedUser bvUser;
    protected AdsAnalyticsManager analyticsManager;
    private BVSDK bvsdk;

    public BVAds() {
        bvsdk = BVSDK.getInstance();

        String apiKeyShopperAdvertising = bvsdk.getApiKeyShopperAdvertising();
        if (apiKeyShopperAdvertising == null || apiKeyShopperAdvertising.isEmpty()) {
            throw new IllegalStateException("BVAds SDK requires a shopper advertising api key");
        }

        bvUser = bvsdk.getAuthenticatedUser();
        analyticsManager = new AdsAnalyticsManager(bvsdk.getAnalyticsManager(), bvsdk.getClientId());
        bvsdk.getAdvertisingIdClient().getAdInfo(new BVSDK.GetAdInfoCompleteAction() {
            @Override
            public void completionAction(AdInfo adInfo) {
                setIsLimitAdTrackingEnabled(adInfo.isLimitAdTrackingEnabled());
            }
        });
    }

    @Override
    public PublisherAdRequest.Builder getTargetedAdRequest(){
        PublisherAdRequest.Builder publisherAdRequestBuilder = new PublisherAdRequest.Builder();
        ShopperProfile shopperProfile = bvsdk.getAuthenticatedUser().getShopperProfile();
        Map<String, String> targetingKeywords = (shopperProfile == null || shopperProfile.getProfile() == null) ? new HashMap<String, String>() : shopperProfile.getProfile().getTargetingKeywords();

        for (Map.Entry<String, String> entry : targetingKeywords.entrySet()) {
            publisherAdRequestBuilder.addCustomTargeting(entry.getKey(), entry.getValue());
        }

        return publisherAdRequestBuilder;
    }

    @Override
    public AdLoader.Builder getTargetedAdLoader(Context context, String adUnitId) {
        AdLoader.Builder targetedAdLoader = new AdLoader.Builder(context, adUnitId);
        targetedAdLoader.withAdListener(new BVAdListener(this, adUnitId, AdTracker.AdTrackerType.NATIVE){});
        return targetedAdLoader;
    }

    @Override
    public PublisherAdView getTargetedAdView(Context context) {
        PublisherAdView targetedAdView = new PublisherAdView(context);
        targetedAdView.setAdListener(new BVAdListener(this, targetedAdView){

        });
        return targetedAdView;
    }

    @Override
    public PublisherInterstitialAd getTargetedInterstitialAd(Context context, String adUnitId) {
        PublisherInterstitialAd targetedInterstitialAd = new PublisherInterstitialAd(context);
        targetedInterstitialAd.setAdUnitId(adUnitId);
        targetedInterstitialAd.setAdListener(new BVAdListener(this, targetedInterstitialAd){});
        return targetedInterstitialAd;
    }

    private void setIsLimitAdTrackingEnabled(boolean isLimitAdTrackingEnabled) {
        // "strongly" inform developers that limit ad tracking is enabled
        if(isLimitAdTrackingEnabled) {
            if(previousValueOfLimitAdTracking != isLimitAdTrackingEnabled) {
                previousValueOfLimitAdTracking = isLimitAdTrackingEnabled;
                Logger.w(TAG, "!!!BVAds will disable most targeting functionality, as LimitAdTracking is enabled!!!");
            }
        }
    }

    @Override
    public void onAdOpened(AdTracker adTracker) {
        analyticsManager.onAdOpened(adTracker, bvUser);
    }

    @Override
    public void onAdLoaded(AdTracker adTracker) {
        analyticsManager.onAdLoaded(adTracker, bvUser);
    }

    @Override
    public void onAdLeftApplication(AdTracker adTracker) {
        analyticsManager.onAdLeftApplication(adTracker, bvUser);
    }

    @Override
    public void onAdFailedToLoad(AdTracker adTracker, int errorCode) {
        analyticsManager.onAdFailedToLoad(adTracker, errorCode, bvUser);
    }

    @Override
    public void onAdClosed(AdTracker adTracker) {
        analyticsManager.onAdClosed(adTracker, bvUser);
    }

}
