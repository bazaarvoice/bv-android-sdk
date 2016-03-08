/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.android.gms.ads.formats.NativeContentAd;

/**
 * Adloader listener for Native Content Ads
 */
public abstract class BVContentAdLoadedListener implements NativeContentAd.OnContentAdLoadedListener {

    private AdTracker adTracker;
    private Ads ads;
    public BVContentAdLoadedListener(Ads ads, String adUnitId) {
        this.ads = ads;
        this.adTracker = new AdTracker(adUnitId, AdTracker.AdTrackerType.NATIVE);
    }

    @Override
    public final void onContentAdLoaded(NativeContentAd nativeContentAd) {
        ads.onAdLoaded(adTracker);
        bvOnContentAdLoaded(nativeContentAd);
    }

    public void bvOnContentAdLoaded(NativeContentAd nativeContentAd){}
}
