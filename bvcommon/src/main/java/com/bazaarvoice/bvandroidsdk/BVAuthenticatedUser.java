/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.bazaarvoice.bvandroidsdk.internal.Utils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bazaarvoice.bvandroidsdk.AdIdRequestTask.getAdId;

/**
 * Internal SDK API for managing Shopper Profile state and updates
 */
class BVAuthenticatedUser {
    // User information can be included in the userAuthString - sent with BV.getAdsSdk().setUserWithAuthString();
    // See the github README for more discussion on how the user auth string is created and used.

    private static final String ENDPOINT_TEMPLATE = "users/magpie_idfa_%s?passkey=%s";
    private static final String TAG = BVAuthenticatedUser.class.getName();
    private static final String SHOPPER_PROFILE_THREAD_NAME = TAG;

    static final int DISPATCH_SHOPPER_PROFILE_UPDATE = 1;
    static final int DISPATCH_SHOPPER_PROFILE_UPDATE_IF_NEEDED = 2;

    private final String baseUrl;
    private final String apiKey;
    private final Context applicationContext;
    private final OkHttpClient okHttpClient;
    private final BVLogger bvLogger;
    private final Gson gson;
    private final List<Integer> profilePollTimes;
    private final HandlerThread bgHandlerThread;
    private final Handler shopperProfileHandler;

    private ShopperProfile shopperProfile;
    private String userAuthString;

    public BVAuthenticatedUser(final Context applicationContext, final String baseUrl, final String apiKey, OkHttpClient okHttpClient, final BVLogger bvLogger, Gson gson, final List<Integer> profilePollTimes, final HandlerThread bgHandlerThread) {
        this.applicationContext = applicationContext;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.okHttpClient = okHttpClient;
        this.bvLogger = bvLogger;
        this.gson = gson;
        this.profilePollTimes = profilePollTimes;
        this.bgHandlerThread = bgHandlerThread;
        this.shopperProfileHandler= new ShopperProfileHandler(this.bgHandlerThread.getLooper(), this);
    }

    /**
     * Internal BVAuthenticatedUser method to form the url for updating
     * a user. Must be done on a worker thread because we need to fetch
     * the latest advertising id, which cannot be done on the ui thread.
     *
     * @return the shopper profile endpoint for the current user
     */
    @WorkerThread
    private URL getUrl() {
        String adId = getAdId(applicationContext).getAdId();
        String relativeUrlStr = String.format(ENDPOINT_TEMPLATE, adId, apiKey);
        String fullUrlStr = baseUrl + relativeUrlStr;
        return Utils.toUrl(fullUrlStr);
    }

    /**
     * Internal BVAuthenticatedUser method to hit the network, and
     * fetch the user's latest shopper profile.
     *
     * @return the fetched shopper profile
     */
    @WorkerThread
    private ShopperProfile updateShopperProfile() {
        ShopperProfile shopperProfile = null;
        Response response = null;

        try {
            URL url = getUrl();
            bvLogger.d(TAG, "Search for profile at " + url);

            Request request = new Request.Builder()
                    .addHeader("User-Agent", BVSDK.getInstance().getBvWorkerData().getBvSdkUserAgent())
                    .url(url)
                    .get()
                    .build();

            response = okHttpClient.newCall(request).execute();

            bvLogger.d(TAG, "Profile response:\n" + response);

            if (response.isSuccessful()) {
                try {
                    shopperProfile = gson.fromJson(response.body().charStream(), ShopperProfile.class);

                    bvLogger.d("Profile", "Succesfully updated profile");
                    bvLogger.d("Profile", shopperProfile.toString());
                } catch (JsonSyntaxException | JsonIOException e) {
                    bvLogger.e("Profile", "Failed to parse profile", e);
                }
            } else {
                bvLogger.d("Profile", "Unsuccesfully updated profile, response code " + response.code() + "\nDid you forget to set a valid shopper advertising passkey?");
            }
        } catch (IOException e) {
            bvLogger.e("Profile", "Failed to update profile", e);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }

        return shopperProfile;
    }

    /**
     * Internal BVAuthenticatedUser method for determining if this
     * user has allowed the BVSDK to use their advertising id.
     *
     * @return true if BVSDK can use user's advertising id
     */
    @WorkerThread
    private boolean userAdIdEnabled() {
        AdIdResult adIdResult = getAdId(applicationContext);
        AdvertisingIdClient.Info adInfo = adIdResult.getAdInfo();
        return adInfo != null && !adInfo.isLimitAdTrackingEnabled();
    }

    /**
     * Internal BVAuthenticatedUser method for determining if the
     * current cached Shopper Profile (if it exists) has targeting
     * keywords.
     *
     * @return true if shopper profile has targeting keywords
     */
    private boolean haveShopperProfileInfo() {
        ShopperProfile shopperProfile = getShopperProfile();
        return shopperProfile != null
                && shopperProfile.getProfile() != null
                && shopperProfile.getProfile().getTargetingKeywords() != null
                && shopperProfile.getProfile().getTargetingKeywords().size() > 0;
    }

    /**
     * Internal BVSDK API method for updating the existing User
     * Auth String associated with a particular user
     *
     * @param authString
     */
    public void setUserAuthString(@NonNull String authString) {
        synchronized (this) {
            this.userAuthString = authString;
        }
    }

    String getUserAuthString() {
        synchronized (this) {
            return userAuthString;
        }
    }

    /**
     * Allows you to get a user's profile in map form in order to feed it into dfp for
     * targeting
     *
     * @return latest shopper profile
     */
    ShopperProfile getShopperProfile() {
        synchronized (this) {
            return shopperProfile;
        }
    }

    /**
     * Internal BVAuthenticatedUser API method for the handler to
     * synchronously update the shopper profile object
     *
     * @param shopperProfile
     */
    private void setShopperProfile(ShopperProfile shopperProfile) {
        synchronized (this) {
            this.shopperProfile = shopperProfile;
        }
    }

    /**
     * Internal BVSDK API method for users of this class to kick off
     * the required update mechanism for updating a user's profile.
     * Updates user's profile by using phone's advertising id.
     */
    @MainThread
    public void updateUser(String logSource) {
        Log.v(TAG, "update user backoff start, bvsdk instance: " + hashCode() + " from " + logSource);

        // Remove any pending shopper profile update messages from the
        // shopper profile thread looper so that we don't allow more than
        // one sequence of back off calls to happen
        shopperProfileHandler.removeMessages(DISPATCH_SHOPPER_PROFILE_UPDATE);
        shopperProfileHandler.removeMessages(DISPATCH_SHOPPER_PROFILE_UPDATE_IF_NEEDED);

        // For each of the poll times, send a message to the shopper
        // profile handler, which will manage the background thread work
        for (int i=0; i<profilePollTimes.size(); i++) {
            int profilePollTime = profilePollTimes.get(i);
            if (i==0) {
                // First poll should always be able to get a new profile
                shopperProfileHandler.sendMessageDelayed(shopperProfileHandler.obtainMessage(DISPATCH_SHOPPER_PROFILE_UPDATE), profilePollTime);
            } else {
                // Subsequent polls will only get a new profile if we don't have a valid one
                shopperProfileHandler.sendMessageDelayed(shopperProfileHandler.obtainMessage(DISPATCH_SHOPPER_PROFILE_UPDATE_IF_NEEDED), profilePollTime);
            }
        }
    }

    /**
     * Handler that orchestrates the actual shopper profile updating
     * work
     */
    static class ShopperProfileHandler extends Handler {
        private final BVAuthenticatedUser bvAuthenticatedUser;

        public ShopperProfileHandler(Looper looper, BVAuthenticatedUser bvAuthenticatedUser) {
            super(looper);
            this.bvAuthenticatedUser = bvAuthenticatedUser;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DISPATCH_SHOPPER_PROFILE_UPDATE: {
                    update();
                    break;
                }
                case DISPATCH_SHOPPER_PROFILE_UPDATE_IF_NEEDED: {
                    updateIfNotSet();
                    break;
                }
            }
        }

        /**
         * If there exists a cached shopper profile with interests,
         * then don't update, otherwise update.
         */
        private void updateIfNotSet() {
            if (!bvAuthenticatedUser.haveShopperProfileInfo()) {
                update();
            }
        }

        /**
         * <ol>
         *     <li>Check to see if user is ad id enabled. If not, stop</li>
         *     <li>Fetch the shopper profile from the shopper profile endpoint</li>
         *     <li>Synchronously update the shopper profile object</li>
         * </ol>
         */
        private void update() {
            if (!bvAuthenticatedUser.userAdIdEnabled()) {
                return;
            }
            ShopperProfile shopperProfile = bvAuthenticatedUser.updateShopperProfile();
            bvAuthenticatedUser.setShopperProfile(shopperProfile);
        }
    }

}