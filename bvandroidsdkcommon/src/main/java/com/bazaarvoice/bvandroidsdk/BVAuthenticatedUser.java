/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.support.annotation.WorkerThread;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private static final String RELATIVE_URL_TEMPLATE = "/users/magpie_idfa_%s?passkey=%s";
    private static final String TAG = BVAuthenticatedUser.class.getName();
    private static final long START_TIME_DELAY_SECONDS = 0;
    private static final long UPDATE_TIME_DELAY_SECONDS = 60 * 5;

    private final String baseUrl;
    private final String apiKey;
    private final Context applicationContext;
    private final ExecutorService immediateExecutorService;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    private ShopperProfile shopperProfile;
    private String userAuthString;

    public BVAuthenticatedUser(final Context applicationContext, final String baseUrl, final String apiKey, ExecutorService immediateExecutorService, ScheduledExecutorService scheduledExecutorService, OkHttpClient okHttpClient, Gson gson) {
        this.applicationContext = applicationContext;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.immediateExecutorService = immediateExecutorService;
        this.okHttpClient = okHttpClient;
        this.gson = gson;

        // setup updating the profile based on set interval
        scheduledExecutorService.scheduleWithFixedDelay(new ShopperProfileTask(applicationContext, this), START_TIME_DELAY_SECONDS, UPDATE_TIME_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    @WorkerThread
    URL getUrl() {
        String adId = getAdId(applicationContext).getAdId();
        String relativeUrlStr = String.format(RELATIVE_URL_TEMPLATE, adId, apiKey);
        String fullUrlStr = baseUrl + relativeUrlStr;
        return Utils.toUrl(fullUrlStr);
    }

    @WorkerThread
    void updateShopperProfile() {
        Response response = null;
        try {
            URL url = getUrl();
            Logger.d(TAG, "Search for profile at " + url);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            response = okHttpClient.newCall(request).execute();

            Logger.d(TAG, "Profile response:\n" + response);

            synchronized (this) {
                if (response.isSuccessful()) {
                    try {
                        shopperProfile = gson.fromJson(response.body().charStream(), ShopperProfile.class);
                    } catch (JsonSyntaxException | JsonIOException e) {
                        Logger.e("Profile", "Failed to parse profile", e);
                    }

                    Logger.d("Profile", "Succesfully updated profile");
                    Logger.d("Profile", shopperProfile.toString());
                } else {
                    Logger.d("Profile", "Unsuccesfully updated profile, response code " + response.code() + "\nDid you forget to set a valid shopper advertising passkey?");
                }
            }
        } catch (IOException e) {
            Logger.e("Profile", "Failed to update profile", e);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }

    }

    /**
     * Used for setting the User Auth String associated with a
     * particular user
     * @param authString
     */
    public void setUserAuthString(String authString) {
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
     * @return
     */
    synchronized ShopperProfile getShopperProfile() {
        return shopperProfile;
    }

    /**
     * Updates user's profile by using phone's advertising id
     */
    void updateProfile() {
        immediateExecutorService.submit(new ShopperProfileTask(applicationContext, this));
    }

    private static class ShopperProfileTask implements Runnable {
        private final Context applicationContext;
        private final BVAuthenticatedUser bvAuthenticatedUser;

        ShopperProfileTask(final Context applicationContext, final BVAuthenticatedUser bvAuthenticatedUser) {
            this.applicationContext = applicationContext;
            this.bvAuthenticatedUser = bvAuthenticatedUser;
        }

        @Override
        public void run() {
            AdIdResult adIdResult = AdIdRequestTask.getAdId(applicationContext);
            AdvertisingIdClient.Info adInfo = adIdResult.getAdInfo();
            if (adInfo != null && !adInfo.isLimitAdTrackingEnabled()) {
                bvAuthenticatedUser.updateShopperProfile();
            }
        }
    }
}