/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.net.Network;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowAsyncTask;

import java.io.IOException;

final class Shadows {
    /**
     * Here because https://github.com/robolectric/robolectric/issues/2223
     */
    @Implements(Network.class)
    public static class ShadowNetwork {
    }

    @Implements(AdIdRequestTask.class)
    public static class BvShadowAsyncTask<Params, Progress, Result> extends ShadowAsyncTask<Params, Progress, Result> {
        @Implementation
        public AsyncTask<Params, Progress, Result> execute(Params... params) {
            return super.execute(params);
        }
    }

    @Implements(AdvertisingIdClient.class)
    public static class ShadowAdIdClient {
        @Implementation
        public static AdvertisingIdClient.Info getAdvertisingIdInfo(Context context) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
            return new AdvertisingIdClient.Info("test_ad_id", false);
        }

    }
}
