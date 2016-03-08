/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

/**
 *  This class is for setting up ad listeners for each kind of Ad
 */
public abstract class BVAdListener extends AdListener {

    private AdTracker adTracker;
    private Ads ads;
    /**
     * Sets up an Ad Listener for an interstitial Ad
     * @param interstitialAd
     */
    public BVAdListener(Ads ads, PublisherInterstitialAd interstitialAd) {
        this.ads = ads;
        this.adTracker = new AdTracker(interstitialAd);
    }

    /**
     * Sets up an Ad Listener for a Publisher Ad View
     * @param adView
     */
    public BVAdListener(Ads ads, PublisherAdView adView){
        this.ads = ads;
        this.adTracker = new AdTracker(adView);
    }

    /**
     * Sets up an Ad Listener based on an Ad Unit Id and an AdTrackerType
     * Lets you select from different kinds of ads
     * @param adUnitId
     * @param adTrackerType
     */
    public BVAdListener(Ads ads, String adUnitId, AdTracker.AdTrackerType adTrackerType) {
        this.ads = ads;
        this.adTracker = new AdTracker(adUnitId, adTrackerType);
    }

    /**
     * For tracking Ad lifecycle events
     */
    @Override
    public final void onAdClosed() {
        ads.onAdClosed(adTracker);
        bvOnAdClosed();
    }

    @Override
    public final void onAdFailedToLoad(int errorCode) {
        ads.onAdFailedToLoad(adTracker, errorCode);
        bvOnAdFailedToLoad(errorCode);
    }

    @Override
    public final void onAdLeftApplication() {
        ads.onAdLeftApplication(adTracker);
        bvOnAdLeftApplication();
    }

    @Override
    public final void onAdOpened() {
        ads.onAdOpened(adTracker);
        bvOnAdOpened();
    }

    @Override
    public final void onAdLoaded() {
        ads.onAdLoaded(adTracker);
        bvOnAdLoaded();
    }

    public void bvOnAdClosed(){}
    public void bvOnAdFailedToLoad(int errorCode){}
    public void bvOnAdLeftApplication(){}
    public void bvOnAdOpened(){}
    public void bvOnAdLoaded(){}


}
