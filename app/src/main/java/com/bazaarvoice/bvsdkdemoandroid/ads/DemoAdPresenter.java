/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.ads;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BVAds;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeContentAd;

import java.util.Map;

public class DemoAdPresenter implements DemoAdContract.UserActionsListener, NativeContentAd.OnContentAdLoadedListener {

    private static final String TAG = DemoAdPresenter.class.getSimpleName();
    private DemoAdContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private AdLoader targetedAdLoader;
    private NativeContentAd nativeContentAd;

    public DemoAdPresenter(DemoAdContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, AdLoader.Builder targetedAdBuilder) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.targetedAdLoader = targetedAdBuilder.forContentAd(this).withAdListener(adListener).build();
    }

    @Override
    public void loadAd(boolean forceRefresh) {
        // Add Bazaarvoice targeting keywords
        PublisherAdRequest.Builder publisherAdRequest = new PublisherAdRequest.Builder();
        Map<String, String> targetingKeywords = BVAds.getCustomTargeting();
        for (Map.Entry<String, String> entry : targetingKeywords.entrySet()) {
            publisherAdRequest.addCustomTargeting(entry.getKey(), entry.getValue());
        }

        publisherAdRequest.addCustomTargeting("cities", "undefined");

        // Add deviceId for emulator
        // You can also add your own for a specific hardware device
//        if (BuildConfig.DEBUG) {
//            publisherAdRequest.addTestDevice(PublisherAdRequest.DEVICE_ID_EMULATOR);
//            String testDeviceId = DemoConstants.DFP_TEST_DEVICE_ID;
//            if (DemoConstants.isSet(testDeviceId)) {
//                publisherAdRequest.addTestDevice(testDeviceId);
//            }
//        }

        targetedAdLoader.loadAd(publisherAdRequest.build());
    }

    @Override
    public void onContentAdLoaded(NativeContentAd nativeContentAd) {
        this.nativeContentAd = nativeContentAd;
        view.showAd(nativeContentAd);
    }

    private AdListener adListener = new AdListener() {

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.e(TAG, "Ad loaded");
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.e(TAG, "Ad closed");
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            Log.e(TAG, "Ad left application");
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Log.e(TAG, "Ad opened");
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            Log.e(TAG, "Ad failed to load with errorCode: " + errorCode + ", " + DemoUtils.getDfpAdErrorMessage(errorCode));
        }
    };
}
