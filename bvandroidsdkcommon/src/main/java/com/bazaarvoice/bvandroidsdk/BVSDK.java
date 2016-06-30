/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.bazaarvoice.bvandroidsdk_common.BuildConfig;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

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
    private static final String SHOPPER_MARKETING_API_ROOT_URL_STAGING = "https://my.network-stg.bazaarvoice.com";
    private static final String SHOPPER_MARKETING_API_ROOT_URL_PRODUCTION = "https://my.network.bazaarvoice.com";
    private static final String CURATIONS_DISPLAY_API_ROOT_URL_PRODUCTION = "https://api.bazaarvoice.com/curations/content/get?";
    private static final String CURATIONS_DISPLAY_API_ROOT_URL_STAGING = "https://stg.api.bazaarvoice.com/curations/content/get?";
    private static final String CURATIONS_POST_API_ROOT_URL_PRODUCTION = "https://api.bazaarvoice.com/curations/content/add/";
    private static final String CURATIONS_POST_API_ROOT_URL_STAGING = "https://stg.api.bazaarvoice.com/curations/content/add/";
    private static final String ANALYTICS_ROOT_URL_PRODUCTION = "https://network.bazaarvoice.com/event";
    private static final String ANALYTICS_ROOT_URL_STAGING = "https://network-stg.bazaarvoice.com/event";
    static BVSDK instance;

    static final String SDK_VERSION = BuildConfig.BVSDK_VERSION_NAME;

    final Application application;
    final OkHttpClient okHttpClient;
    final AnalyticsManager analyticsManager;
    final BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks;
    final String clientId;
    final BazaarEnvironment environment;
    final String apiKeyShopperAdvertising;
    final String apiKeyConversations;
    final String apiKeyCurations;
    final BVAuthenticatedUser bvAuthenticatedUser;
    final Gson gson;
    final String shopperMarketingApiRootUrl;
    final String curationsDisplayApiRootUrl;
    final String curationsPostApiRootUrl;

    BVSDK(Application application, String clientId, BazaarEnvironment environment, String apiKeyShopperAdvertising, String apiKeyConversations, String apiKeyCurations, BVLogLevel logLevel, OkHttpClient okHttpClient, final AnalyticsManager analyticsManager, BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks, final BVAuthenticatedUser bvAuthenticatedUser, Gson gson, String shopperMarketingApiRootUrl, String curationsDisplayApiRootUrl, String curationsPostApiRootUrl) {
        this.application = application;
        this.okHttpClient = okHttpClient;
        this.analyticsManager = analyticsManager;
        this.bvActivityLifecycleCallbacks = bvActivityLifecycleCallbacks;
        this.clientId = clientId;
        this.environment = environment;
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.apiKeyConversations = apiKeyConversations;
        this.apiKeyCurations = apiKeyCurations;
        this.bvAuthenticatedUser = bvAuthenticatedUser;
        this.gson = gson;
        this.shopperMarketingApiRootUrl = shopperMarketingApiRootUrl;
        this.curationsDisplayApiRootUrl = curationsDisplayApiRootUrl;
        this.curationsPostApiRootUrl = curationsPostApiRootUrl;

        // Also set here for when the constructor is used during tests
        Logger.setLogLevel(logLevel);

        Logger.d(TAG, "Updating user upon BVSDK initialization");
        updateUser();

        /**
         * Register with ActivityLifeCycleCallbacks for App lifecycle analaytics, and
         * for triggering user profile updates
         */
        application.registerActivityLifecycleCallbacks(bvActivityLifecycleCallbacks);

        /**
         * Send Magpie lifecycle analytics signalling app launch
         */
        analyticsManager.enqueueAppStateEvent(MobileAppLifecycleSchema.AppState.LAUNCHED);
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
        private String apiKeyCurations;
        private BVLogLevel logLevel;
        private OkHttpClient okHttpClient;

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

        /**
         * @param apiKeyCurations API Key required to access Curations SDK
         * @return
         */
        public Builder apiKeyCurations(String apiKeyCurations) {
            this.apiKeyCurations = apiKeyCurations;
            return this;
        }

        public Builder logLevel(BVLogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            if (okHttpClient == null) {
                throw new IllegalArgumentException("OkHttpClient must not be null");
            }
            if (this.okHttpClient != null) {
                throw new IllegalStateException("OkHttpClient already set");
            }
            this.okHttpClient = okHttpClient;
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

            if (okHttpClient == null) {
                this.okHttpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
            }

            Logger.setLogLevel(logLevel);

            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(SCHEDULED_BV_THREAD_NAME));
            ScheduledExecutorService profileExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(SCHEDULED_BV_PROFILE_THREAD_NAME));
            ExecutorService immediateExecutorService = Executors.newFixedThreadPool(1, new NamedThreadFactory(IMMEDIATE_BV_THREAD_NAME));
            Gson gson = new Gson();
            String versionName = Utils.getVersionName(application.getApplicationContext());
            String versionCode = Utils.getVersionCode(application.getApplicationContext());
            String packageName = Utils.getPackageName(application.getApplicationContext());
            UUID uuid = Utils.getUuid(application.getApplicationContext());
            String shopperMarketingApiRootUrl = bazaarEnvironment == BazaarEnvironment.STAGING ? SHOPPER_MARKETING_API_ROOT_URL_STAGING : SHOPPER_MARKETING_API_ROOT_URL_PRODUCTION;
            String curationsDisplayApiRootUrl = bazaarEnvironment == BazaarEnvironment.STAGING ? CURATIONS_DISPLAY_API_ROOT_URL_STAGING : CURATIONS_DISPLAY_API_ROOT_URL_PRODUCTION;
            String curationsPostApiRootUrl = bazaarEnvironment == BazaarEnvironment.STAGING ? CURATIONS_POST_API_ROOT_URL_STAGING : CURATIONS_POST_API_ROOT_URL_PRODUCTION;
            String analyticsRootUrl = bazaarEnvironment == BazaarEnvironment.STAGING ? ANALYTICS_ROOT_URL_STAGING : ANALYTICS_ROOT_URL_PRODUCTION;
            BVAuthenticatedUser bvAuthenticatedUser = new BVAuthenticatedUser(application.getApplicationContext(), shopperMarketingApiRootUrl, apiKeyShopperAdvertising, immediateExecutorService, profileExecutorService, okHttpClient, gson);
            AnalyticsManager analyticsManager = new AnalyticsManager(application.getApplicationContext(), versionName, versionCode, clientId, analyticsRootUrl, okHttpClient, immediateExecutorService, scheduledExecutorService, bvAuthenticatedUser, packageName, uuid);
            BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks = new BVActivityLifecycleCallbacks(analyticsManager);

            instance = new BVSDK(application, clientId, bazaarEnvironment, apiKeyShopperAdvertising, apiKeyConversations, apiKeyCurations, logLevel, okHttpClient, analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiRootUrl, curationsDisplayApiRootUrl, curationsPostApiRootUrl);
            return instance;
        }
    }

    // Methods for helper classes below

    Context getApplicationContext() {
        return application.getApplicationContext();
    }

    AdIdRequestTask getAdIdRequestTask(AdIdRequestTask.AdIdCallback callback) {
        return new AdIdRequestTask(getApplicationContext(), callback);
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

    Gson getGson() {
        return gson;
    }

    String getShopperMarketingApiRootUrl() {
        return shopperMarketingApiRootUrl;
    }

    String getCurationsDisplayApiRootUrl(){
        return curationsDisplayApiRootUrl;
    }

    String getCurationsPostApiRootUrl(){
        return curationsPostApiRootUrl;
    }

    OkHttpClient getOkHttpClient() {
        return okHttpClient;
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
        analyticsManager.dispatchSendPersonalizationEvent();

        Logger.d(TAG, "Updating user upon user auth string update");
        updateUser();
    }

    /**
     * Event when a transaction occurs
     *
     * @param transaction
     */
    public void sendConversionTransactionEvent(Transaction transaction) {
        if (transaction.getItems() == null || transaction.getItems().size() == 0){
            Logger.w("BVSDK", "Could not track Transaction. Transaction items are required");
            return;
        }
        analyticsManager.enqueueConversionTransactionEvent(transaction);
    }

    /**
     * Event when a transaction occurs
     *
     * @param conversion
     */
    public void sendNonCommerceConversionEvent(Conversion conversion) {
        analyticsManager.enqueueNonCommerceConversionEvent(conversion);
    }

    private void updateUser() {
        final Handler handler = new Handler(Looper.getMainLooper());

        for(final int time : Arrays.asList(0, 5000, 10000, 20000)){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Logger.d(TAG, "Profile update poll");
                    bvAuthenticatedUser.updateProfile();
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

    String getApiKeyCurations() {
        return apiKeyCurations;
    }

}
