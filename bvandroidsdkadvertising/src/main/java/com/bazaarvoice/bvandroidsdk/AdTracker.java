/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.Random;

/**
 * Tracker for each of the five kinds of ads
 */
public class AdTracker {
    private final String adUnitId;
    private final AdTrackerType adType;
    private final String loadId;

    public enum AdTrackerType {
        BANNER("Banner"),
        INTERSTITIAL("Interstitial"),
        NATIVE("Native");

        private String value;

        AdTrackerType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Set an Ad's Ad Unit Id and what kind of Ad it is
     * @param adUnitId
     * @param nativeContentAdType
     */
    AdTracker(String adUnitId, AdTrackerType nativeContentAdType) {
        this.adUnitId = adUnitId;
        this.adType = nativeContentAdType;
        this.loadId = String.valueOf(new Random().nextLong());
    }

    /**
     * Set ad tracker for an interstitial ad
     * @param interstitialAd
     */
    AdTracker(PublisherInterstitialAd interstitialAd){
        this(interstitialAd.getAdUnitId(), AdTrackerType.INTERSTITIAL);
    }

    /**
     * Set ad tracker for a banner ad
     * @param adView
     */
    AdTracker(PublisherAdView adView) {
        this(adView.getAdUnitId(), AdTrackerType.BANNER);
    }

    String getAdUnitId() {
        return adUnitId;
    }

    AdTrackerType getAdType() {
        return adType;
    }

    String getLoadId() {
        return loadId;
    }
}