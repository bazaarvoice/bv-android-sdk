/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

class AdIdResult {
    private static final String NONTRACKING_TOKEN = "nontracking";

    private AdvertisingIdClient.Info adInfo;
    private String errorMessage;

    AdIdResult(AdvertisingIdClient.Info adInfo, String errorMessage) {
        this.adInfo = adInfo;
        this.errorMessage = errorMessage;
    }

    AdvertisingIdClient.Info getAdInfo() {
        return adInfo;
    }

    String getErrorMessage() {
        return errorMessage;
    }

    String getAdId() {
        if (adInfo == null || adInfo.isLimitAdTrackingEnabled()) {
            return NONTRACKING_TOKEN;
        } else {
            return adInfo.getId();
        }
    }
}
