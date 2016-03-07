/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

/**
 * Interface defining Ads module API
 */
public interface Ads {

    /**
     * Creates a {@code PublishedAdRequest.Builder} that has appropriately set custom targeting
     * values based on BVAds's personalization data. The Builder is returned to allow further
     * use of {@code PublishedAdRequest.Builder} if desired, before passing to
     * {@code getTargetedAdView}, {@code getTargetedInterstitialAd}, or {@code getTargetedAdLoader}.
     * @return PublisherAdRequest.Builder
     */
    PublisherAdRequest.Builder getTargetedAdRequest();

    /**
     * Creates a AdLoader.Builder to be used with {@code getTargetedAdRequest} in order to request
     * and display a targeted native ads.
     * @param context The activity's context
     * @param adUnitId Your DoubleClick ad's adUnitId, available through DoubleClick's console.
     * @return AdLoader.Builder
     */
    AdLoader.Builder getTargetedAdLoader(Context context, String adUnitId);

    /**
     * Creates a PublisherAdView to be used with {@code getTargetedAdRequest} in order to request
     * and display targeted banner ads.
     * @param context The activity's context
     * @return PublisherAdView
     */
    PublisherAdView getTargetedAdView(Context context);

    /**
     * Creates a PublisherInterstitialAd to be used with {@code getTargetedAdRequest} in order to
     * get and display a targeted intersitial ad.
     * @param context The activity's context
     * @param adUnitId Your DoubleClick ad's adUnitId, available through DoubleClick's console.
     * @return PublisherInterstitialAd
     */
    PublisherInterstitialAd getTargetedInterstitialAd(Context context, String adUnitId);

    /**
     * Send analytics event when an Ad is shown/opened
     * @param adTracker
     */
    public void onAdOpened(AdTracker adTracker);

    /**
     * Send analytics event when Ad is loaded
     * @param adTracker
     */
    public void onAdLoaded(AdTracker adTracker);

    /**
     * Send analytics event when user leaves application when an Ad is displayed
     * @param adTracker
     */
    public void onAdLeftApplication(AdTracker adTracker);

    /**
     * Send analytics event when Ad fails to load
     * @param adTracker
     * @param errorCode
     */
    public void onAdFailedToLoad(AdTracker adTracker, int errorCode);

    /**
     * Send analytics event when Ad is closed
     * @param adTracker
     */
    public void onAdClosed(AdTracker adTracker);
}
