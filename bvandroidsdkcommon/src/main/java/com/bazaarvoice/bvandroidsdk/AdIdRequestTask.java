/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

/**
 * Get Google Advertising Id using an AsyncTask
 */
class AdIdRequestTask extends AsyncTask<Void, Void, AdIdResult> {

    private static final String TAG = AdIdRequestTask.class.getSimpleName();

    private Context applicationContext;

    // Strong reference that will be nulled out after delivering the result
    private AdIdCallback adIdCallback;

    AdIdRequestTask(Context applicationContext, AdIdCallback callback) {
        if (applicationContext == null) {
            throw new IllegalArgumentException("Must set application context");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must set callback");
        }
        this.applicationContext = applicationContext;
        this.adIdCallback = callback;
    }

    /**
     * Must not be called on the main thread
     *
     * @param applicationContext
     * @return {@link AdIdResult}
     */
    static AdIdResult getAdId(Context applicationContext) {
        AdvertisingIdClient.Info adInfo = null;
        String errorMessage = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext);
        } catch (IOException e) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).
            errorMessage = e.getMessage();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Google Play services is not available entirely.
            errorMessage = e.getMessage();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }

        return new AdIdResult(adInfo, errorMessage);
    }

    @Override
    protected AdIdResult doInBackground(Void... params) {
        return getAdId(applicationContext);
    }

    @Override
    protected void onPostExecute(AdIdResult result) {
        super.onPostExecute(result);
        if (result.getErrorMessage() != null) {
            Log.e(TAG, result.getErrorMessage());
        }
        if (adIdCallback != null) {
            adIdCallback.onAdInfoComplete(result);
        }
        adIdCallback = null;
    }

    interface AdIdCallback {
        void onAdInfoComplete(AdIdResult result);
    }

}