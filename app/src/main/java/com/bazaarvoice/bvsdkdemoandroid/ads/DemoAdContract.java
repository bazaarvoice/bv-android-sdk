/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.ads;

import com.google.android.gms.ads.formats.NativeContentAd;

public interface DemoAdContract {

    interface View {
        void showAd(NativeContentAd nativeContentAd);
        void transitionToAdInWebview(String url);
    }

    interface UserActionsListener {
        void loadAd(boolean forceRefresh);
    }
}
