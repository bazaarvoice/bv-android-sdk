/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import okhttp3.OkHttpClient;

/**
 * <p> Main class for initializing the Bazaarvoice SDK, and changing global SDK state. </p>
 * <p> Must initialize the SDK upon Application startup
 * with the {@link Builder#build()} method. </p>
 * <p> API keys for Shopper Advertising and Conversation must be provided to
 * the {@link Builder#apiKeyShopperAdvertising(String)} and
 * {@link Builder#apiKeyConversations(String)} methods respectively for each of the
 * desired components to function</p>
 * <p> Your client name and the application object must also be provided when
 * constructing the {@link Builder}</p>
 */
public class BVSDK {

    private static final String TAG = BVSDK.class.getSimpleName();
    private static final String SCHEDULED_BV_THREAD_NAME = "BV-ScheduledThread";
    private static final String SCHEDULED_BV_PROFILE_THREAD_NAME = "BV-Profile-ScheduledThread";
    private static final String IMMEDIATE_BV_THREAD_NAME = "BV-ImmediateThread";
    private static BVSDK instance;

    static final String SDK_VERSION = "3.1.0";

    final Application application;
    final ExecutorService scheduledExecutorService;
    final ExecutorService immediateExecutorService;
    final OkHttpClient okHttpClient;
    final AdvertisingIdClient advertisingIdClient;
    final AnalyticsManager analyticsManager;
    final BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks;
    final String clientId;
    final BazaarEnvironment environment;
    final String apiKeyShopperAdvertising;
    final String apiKeyConversations;
    final BVAuthenticatedUser bvAuthenticatedUser;
    final Gson gson;

    interface GetAdInfoCompleteAction {
        void completionAction(AdInfo adInfo);
    }

    BVSDK(Application application, String clientId, BazaarEnvironment environment, String apiKeyShopperAdvertising, String apiKeyConversations, BVLogLevel logLevel, ExecutorService scheduledExecutorService, ExecutorService immediateExecutorService, OkHttpClient okHttpClient, AdvertisingIdClient advertisingIdClient, final AnalyticsManager analyticsManager, BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks, final BVAuthenticatedUser bvAuthenticatedUser, Gson gson) {
        this.application = application;
        this.scheduledExecutorService = scheduledExecutorService;
        this.immediateExecutorService = immediateExecutorService;
        this.okHttpClient = okHttpClient;
        this.advertisingIdClient = advertisingIdClient;
        this.analyticsManager = analyticsManager;
        this.bvActivityLifecycleCallbacks = bvActivityLifecycleCallbacks;
        this.clientId = clientId;
        this.environment = environment;
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.apiKeyConversations = apiKeyConversations;
        this.bvAuthenticatedUser = bvAuthenticatedUser;
        this.gson = gson;

        /**
         * Register with ActivityLifeCycleCallbacks for App lifecycle analaytics, and
         * for triggering user profile updates
         */
        application.registerActivityLifecycleCallbacks(bvActivityLifecycleCallbacks);

        /**
         * Send Magpie lifecycle analytics signalling app launch
         */
        analyticsManager.sendAppStateEvent(MobileAppLifecycleSchema.AppState.LAUNCHED);
    }

    public static BVSDK getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Must initialize BVSDK first.");
        }

        return instance;
    }

    /**
     * Used for testing to emulate app restart when this singleton would be destroyed
     */
    static void destroy() {
        instance = null;
    }

    private static final class NamedThreadFactory implements ThreadFactory {

        private String threadName;

        public NamedThreadFactory(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(threadName);
            return thread;
        }
    }

    /**
     * Initialization Builder that must be called in the {@link android.app.Application#onCreate()} method
     * before any Bazaarvoice SDKs can be used.
     */
    public static class Builder {

        private Application application;
        private String clientId;
        private BazaarEnvironment bazaarEnvironment;
        private String apiKeyShopperAdvertising;
        private String apiKeyConversations;
        private BVLogLevel logLevel;

        private Builder() {}

        /**
         * @param application Required Application Object
         * @param clientId Client id used to get custom results
         */
        public Builder(Application application, String clientId) {
            this.application = application;
            this.clientId = clientId;
        }

        /**
         * @param bazaarEnvironment Bazaarvoice environment to get data from. Default is {@link BazaarEnvironment#STAGING}
         * @return
         */
        public Builder bazaarEnvironment(BazaarEnvironment bazaarEnvironment) {
            this.bazaarEnvironment = bazaarEnvironment;
            return this;
        }

        /**
         * @param apiKeyShopperAdvertising API Key required to access Recommendations and Ads SDK
         * @return
         */
        public Builder apiKeyShopperAdvertising(String apiKeyShopperAdvertising) {
            this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
            return this;
        }

        /**
         * @param apiKeyConversations API Key required to access Conversations SDK
         * @return
         */
        public Builder apiKeyConversations(String apiKeyConversations) {
            this.apiKeyConversations = apiKeyConversations;
            return this;
        }

        public Builder logLevel(BVLogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public BVSDK build() {
            if (application == null) {
                throw new IllegalStateException("Must provide an application object");
            }

            if (clientId == null) {
                throw new IllegalStateException("Must provide a client id");
            }

            if (bazaarEnvironment == null) {
                bazaarEnvironment = BazaarEnvironment.STAGING;
            }

            if (logLevel == null) {
                logLevel = BVLogLevel.ERROR;
            }

            Logger.setLogLevel(logLevel);

            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(SCHEDULED_BV_THREAD_NAME));
            ScheduledExecutorService profileExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(SCHEDULED_BV_PROFILE_THREAD_NAME));
            ExecutorService immediateExecutorService = Executors.newFixedThreadPool(1, new NamedThreadFactory(IMMEDIATE_BV_THREAD_NAME));
            OkHttpClient okHttpClient = new OkHttpClient();
            Gson gson = new Gson();
            AdvertisingIdClient advertisingIdClient = new AdvertisingIdClient(application.getApplicationContext());
            BVAuthenticatedUser bvAuthenticatedUser = new BVAuthenticatedUser(Utils.isStagingEnvironment(bazaarEnvironment), apiKeyShopperAdvertising, immediateExecutorService, profileExecutorService, okHttpClient, gson);
            String versionName = Utils.getVersionName(application.getApplicationContext());
            String versionCode = Utils.getVersionCode(application.getApplicationContext());
            String packageName = Utils.getPackageName(application.getApplicationContext());
            UUID uuid = Utils.getUuid(application.getApplicationContext());
            AnalyticsManager analyticsManager = new AnalyticsManager(versionName, versionCode, clientId, bazaarEnvironment, advertisingIdClient, okHttpClient, immediateExecutorService, scheduledExecutorService, bvAuthenticatedUser, packageName, uuid);
            BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks = new BVActivityLifecycleCallbacks(analyticsManager);

            instance = new BVSDK(application, clientId, bazaarEnvironment, apiKeyShopperAdvertising, apiKeyConversations, logLevel, scheduledExecutorService, immediateExecutorService, okHttpClient, advertisingIdClient, analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson);
            return instance;
        }
    }

    // Methods for helper classes below

    Context getApplicationContext() {
        return application.getApplicationContext();
    }

    String getClientId() {
        return clientId;
    }

    BazaarEnvironment getEnvironment() {
        return environment;
    }

    AnalyticsManager getAnalyticsManager() {
        return analyticsManager;
    }

    AdvertisingIdClient getAdvertisingIdClient() {
        return advertisingIdClient;
    }

    Gson getGson() {
        return gson;
    }

    void registerLifecycleListener(Application.ActivityLifecycleCallbacks lifecycleCallbacks) {
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    void unregisterLifecycleListener(Application.ActivityLifecycleCallbacks lifecycleCallbacks) {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    /**
     * Tell BVSDK who the user is. This is the essential step in providing targeted, personalized
     * information about the user.
     * @param userAuthString The Bazaarvoice-specific User Auth String. See the online documentation
     *                       for information on where this auth string comes from, and why it is
     *                       necessary.
     */
    public void setUserAuthString(String userAuthString) {
        if (userAuthString == null) {
            Logger.w(TAG, "userAuthString must not be null");
            return;
        }

        String savedUserAuthString = bvAuthenticatedUser.getUserAuthString();
        if (savedUserAuthString != null && savedUserAuthString.equals(userAuthString)) {
            Logger.w(TAG, "This same userAuthString is already set");
            return;
        }

        bvAuthenticatedUser.setUserAuthString(userAuthString);
        analyticsManager.sendPersonalizationEvent();

        updateUser();
    }

    /**
     * Event when a single product recommendation appears on screen
     *
     * @param product
     */
    public void sendProductImpressionEvent(BVProduct product) {
        analyticsManager.sendProductImpressionEvent(product);
    }

    /**
     * Event when a product is tapped (i.e. conversion)
     *
     * @param product
     */
    public void sendProductConversionEvent(BVProduct product) {
        analyticsManager.sendProductConversionEvent(product);
    }

    private void updateUser() {
        final Handler handler = new Handler(Looper.getMainLooper());

        for(final int time : Arrays.asList(0, 5000, 10000, 20000)){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Logger.d(TAG, "Profile update poll");
                    bvAuthenticatedUser.updateProfile(true);
                }
            }, time);
        }
    }

    BVAuthenticatedUser getAuthenticatedUser() {
        return bvAuthenticatedUser;
    }

    String getApiKeyShopperAdvertising() {
        return apiKeyShopperAdvertising;
    }

    String getApiKeyConversations() {
        return apiKeyConversations;
    }

}
