/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Internal SDK API for managing Shopper Profile state and updates
 */
class BVAuthenticatedUser {

    // User information can be included in the userAuthString - sent with BV.getAdsSdk().setUserWithAuthString();
    // See the github README for more discussion on how the user auth string is created and used.

    private static final String NONTRACKING_TOKEN = "nontracking";
    private static final String PENDING_TOKEN = "pending";
    private static final String STAGING_URL = "my.network-stg.bazaarvoice.com";
    private static final String PRODUCTION_BASE_URL = "my.network.bazaarvoice.com";
    private final String TAG = getClass().getName();
    private final boolean staging;

    private String userAuthString;
    private String userAdvertisingId;
    private ShopperProfile shopperProfile;
    private static final long START_TIME_DELAY_SECONDS = 0;
    private static final long UPDATE_TIME_DELAY_SECONDS = 60 * 5;
    private boolean isLimitAdTrackingEnabled;
    private boolean fetched = false;
    private ExecutorService immediateExecutorService;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private String apiKeyShopperAdvertising;

    public BVAuthenticatedUser(boolean staging, String apiKeyShopperAdvertising, ExecutorService immediateExecutorService, ScheduledExecutorService scheduledExecutorService, OkHttpClient okHttpClient, Gson gson) {
        this.staging = staging;
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.immediateExecutorService = immediateExecutorService;
        this.okHttpClient = okHttpClient;
        this.gson = gson;

        // setup updating the profile based on set interval
        scheduledExecutorService.scheduleWithFixedDelay(new ShopperProfileTask(), START_TIME_DELAY_SECONDS, UPDATE_TIME_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    public void setIsLimitAdTrackingEnabled(boolean isLimitAdTrackingEnabled) {
        this.isLimitAdTrackingEnabled = isLimitAdTrackingEnabled;
        this.fetched = true;
        Logger.d(TAG, "setIsLimitadTrackingEnabled: " + isLimitAdTrackingEnabled);
    }

    /**
     * Used for setting the User Auth String associated with a
     * particular user
     * @param authString
     */
    public void setUserAuthString(String authString) {
        this.userAuthString = authString;
    }

    String getUserAuthString()
    {
        return userAuthString;
    }
    /**
     * Sets the Advertising Id that is associated with device
     * @param idfa
     */
    void setUserAdvertisingId(String idfa) {
        this.userAdvertisingId = idfa;
    }

    /**
     * Returns the Advertising Id that is associated with device
     * @return
     */
    protected String getUserAdvertisingId() {
        if (!fetched) {
            return PENDING_TOKEN;
        } else if (userAdvertisingId == null) {
            return NONTRACKING_TOKEN;
        } else {
            return userAdvertisingId;
        }
    }

    /**
     * Allows you to get a user's profile in map form in order to feed it into dfp for
     * targeting
     * @return
     */
    public synchronized ShopperProfile getShopperProfile() {
        // if LimitAdTracking is enabled, don't use targetingData
        if(this.isLimitAdTrackingEnabled)
            return null;

        return shopperProfile;
    }

    /**
     * Updates user's profile by using phone's advertising id
     */
    public void updateProfile(boolean forceUpdate) {
        if(this.isLimitAdTrackingEnabled) {
            return;
        }

        if(!forceUpdate) {
            return;
        }

        immediateExecutorService.submit(new ShopperProfileTask());
    }

    private final class ShopperProfileTask implements Runnable {
        @Override
        public void run() {
            synchronized (BVAuthenticatedUser.this) {
                Response response = null;
                try {
                    String userAdId = getUserAdvertisingId();

                    if (userAdId.equals(PENDING_TOKEN) || userAdId.equals(NONTRACKING_TOKEN)) {
                        Logger.d("Profile", "Not sending shopper profile request because userAdId is " + userAdId);
                        return;
                    }

                    HttpUrl httpUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host(getBaseProfileApiUrl())
                            .addPathSegment("users")
                            .addPathSegment("magpie_idfa_" + getUserAdvertisingId())
                            .addQueryParameter("passkey", apiKeyShopperAdvertising)
                            .build();

                    Logger.d(TAG, "Search for profile at " + httpUrl);

                    Request request = new Request.Builder()
                            .url(httpUrl)
                            .get()
                            .build();

                    response = okHttpClient.newCall(request).execute();

                    Logger.d(TAG, "Profile response:\n" + response);

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
                } catch (IOException e) {
                    Logger.e("Profile", "Failed to update profile", e);
                } finally {
                    if (response != null && response.body() != null) {
                        response.body().close();
                    }
                }
            }
        }
    }

    /**
     * Use this in order to get the environment that user data will
     * be written to
     */
    public String getBaseProfileApiUrl() {
        if(staging){
            return STAGING_URL;
        }
        else {
            return PRODUCTION_BASE_URL;
        }
    }
}