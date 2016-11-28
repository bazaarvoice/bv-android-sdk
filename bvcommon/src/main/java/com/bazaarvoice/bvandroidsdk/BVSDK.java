/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.internal.Utils;
import com.bazaarvoice.bvandroidsdk_common.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

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
    // region Properties

    private static final String TAG = BVSDK.class.getSimpleName();
    private static final String SCHEDULED_BV_THREAD_NAME = "BV-ScheduledThread";
    private static final String IMMEDIATE_BV_THREAD_NAME = "BV-ImmediateThread";
    private static final String SHOPPER_MARKETING_API_ROOT_URL_STAGING = "https://my.network-stg.bazaarvoice.com/";
    private static final String SHOPPER_MARKETING_API_ROOT_URL_PRODUCTION = "https://my.network.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_PRODUCTION = "https://network.bazaarvoice.com/";
    private static final String ANALYTICS_ROOT_URL_STAGING = "https://network-stg.bazaarvoice.com/";
    private static final String BAZAARVOICE_ROOT_URL_STAGING = "https://stg.api.bazaarvoice.com/";
    private static final String BAZAARVOICE_ROOT_URL_PRODUCTION = "https://api.bazaarvoice.com/";
    private static final String NOTIFICATION_CONFIG_URL = "https://s3.amazonaws.com/";
    private static final String BVSDK_USER_AGENT = "bvsdk-android/v"+ BuildConfig.BVSDK_VERSION_NAME;
    private static final String BACKGROUND_THREAD_NAME = "BackgroundThread";
    static final int BVHandlePayload = 123;
    static volatile BVSDK singleton;

    static final String SDK_VERSION = BuildConfig.BVSDK_VERSION_NAME;

    final Application application;
    final OkHttpClient okHttpClient;
    final AnalyticsManager analyticsManager;
    final BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks;
    final String clientId;
    final BazaarEnvironment environment;
    final BVAuthenticatedUser bvAuthenticatedUser;
    final Gson gson;
    final BVRootApiUrls rootApiUrls;
    final BVApiKeys apiKeys;
    final Handler handler;
    final HandlerThread backgroundThread;

    // endregion

    // region Constructor

    BVSDK(Application application, String clientId, BazaarEnvironment environment, BVApiKeys apiKeys, BVLogLevel logLevel, OkHttpClient okHttpClient, final AnalyticsManager analyticsManager, BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks, final BVAuthenticatedUser bvAuthenticatedUser, Gson gson, BVRootApiUrls rootApiUrls, Handler handler, HandlerThread backgroundThread) {
        this.application = application;
        this.okHttpClient = okHttpClient;
        this.analyticsManager = analyticsManager;
        this.bvActivityLifecycleCallbacks = bvActivityLifecycleCallbacks;
        this.clientId = clientId;
        this.environment = environment;
        this.bvAuthenticatedUser = bvAuthenticatedUser;
        this.gson = gson;
        this.rootApiUrls = rootApiUrls;
        this.apiKeys = apiKeys;
        this.handler = handler;
        this.backgroundThread = backgroundThread;

        // Also set here for when the constructor is used during tests
        Logger.setLogLevel(logLevel);

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

    // endregion

    // region Public API

    public static BVSDK getInstance() {
        confirmBVSDKCreated();
        return singleton;
    }

    /**
     * Tell BVSDK who the user is. This is the essential step in providing targeted, personalized
     * information about the user.
     * @param userAuthString The Bazaarvoice-specific User Auth String. See the online documentation
     *                       for information on where this auth string comes from, and why it is
     *                       necessary.
     */
    @MainThread
    public void setUserAuthString(@NonNull String userAuthString) {
        if (userAuthString == null || userAuthString.isEmpty()) {
            Logger.w(TAG, "userAuthString must not be empty");
            return;
        }

        bvAuthenticatedUser.setUserAuthString(userAuthString);
        analyticsManager.dispatchSendPersonalizationEvent();

        bvAuthenticatedUser.updateUser("user auth string update");
    }

    void sendConversionTransactionEvent(Transaction transaction) {
        analyticsManager.enqueueConversionTransactionEvent(transaction);
    }

    void sendNonCommerceConversionEvent(Conversion conversion) {
        analyticsManager.enqueueNonCommerceConversionEvent(conversion);
    }

    public static Builder builder(Application application, String clientId) {
        return new Builder(application, clientId);
    }

    // endregion

    // region Internal API

    private static void confirmBVSDKCreated() {
        if (singleton == null) {
            synchronized (BVSDK.class) {
                if (singleton == null) {
                    throw new IllegalStateException("Must initialize BVSDK first.");
                }
            }
        }
    }

    private static void confirmBVSDKNotCreated() {
        if (singleton != null) {
            synchronized (BVSDK.class) {
                if (singleton != null) {
                    throw new IllegalStateException("BVSDK singleton already exists");
                }
            }
        }
    }

    /**
     * Used for testing to emulate app restart when this singleton would be destroyed
     */
    static void destroy() {
        synchronized (BVSDK.class) {
            singleton = null;
        }
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
     * {@link HandlerThread} to be shared in the SDK to facilitate background
     * work. The {@link Looper} is accessible to libraries inheriting from common.
     */
    static class BackgroundThread extends HandlerThread {
        BackgroundThread() {
            super(Utils.THREAD_PREFIX + BACKGROUND_THREAD_NAME, THREAD_PRIORITY_BACKGROUND);
        }
    }

    void postPayloadToMainThread(BVHandlerCallbackPayload payload) {
        Message message = handler.obtainMessage();
        message.what = BVSDK.BVHandlePayload;
        message.obj = payload;
        handler.sendMessage(message);
    }

    // endregion

    // region Builder

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
        private String apiKeyConversationsStores;
        private String apiKeyCurations;
        private String apiKeyLocation;
        private String apiKeyPin;
        private BVLogLevel logLevel;
        private OkHttpClient okHttpClient;

        /**
         * @param application Required Application Object
         * @param clientId Client id used to get custom results
         */
        public Builder(Application application, String clientId) {
            if (application == null) {
                throw new IllegalArgumentException("Application must not be null");
            }
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalArgumentException("clientId must be valid");
            }
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
            if (apiKeyShopperAdvertising == null || apiKeyShopperAdvertising.isEmpty()) {
                throw new IllegalArgumentException("apiKeyShopperAdvertising must be valid");
            }
            this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
            return this;
        }

        /**
         * @param apiKeyConversations API Key required to access Conversations SDK
         * @return
         */
        public Builder apiKeyConversations(String apiKeyConversations) {
            if (apiKeyConversations == null || apiKeyConversations.isEmpty()) {
                throw new IllegalArgumentException("apiKeyConversations must be valid");
            }
            this.apiKeyConversations = apiKeyConversations;
            return this;
        }

        /**
         * @param apiKeyConversationsStores API Key required to access Conversations SDK
         * @return
         */
        public Builder apiKeyConversationsStores(String apiKeyConversationsStores) {
            if (apiKeyConversationsStores == null || apiKeyConversationsStores.isEmpty()) {
                throw new IllegalArgumentException("apiKeyConversationsStores must be valid");
            }
            this.apiKeyConversationsStores = apiKeyConversationsStores;
            return this;
        }

        /**
         * @param apiKeyCurations API Key required to access Curations SDK
         * @return
         */
        public Builder apiKeyCurations(String apiKeyCurations) {
            if (apiKeyCurations == null || apiKeyCurations.isEmpty()) {
                throw new IllegalArgumentException("apiKeyCurations must be valid");
            }
            this.apiKeyCurations = apiKeyCurations;
            return this;
        }

        /**
         * @param apiKeyLocation API Key required to access Location SDK
         * @return
         */
        public Builder apiKeyLocation(String apiKeyLocation) {
            if (apiKeyLocation == null || apiKeyLocation.isEmpty()) {
                throw new IllegalArgumentException("apiKeyCurations must be valid");
            }
            this.apiKeyLocation = apiKeyLocation;
            return this;
        }

        /**
         * @param apiKeyPin API Key required to access Post Interaction Notifications API
         * @return
         */
        public Builder apiKeyPin(String apiKeyPin) {
            if (apiKeyPin == null || apiKeyPin.isEmpty()) {
                throw new IllegalArgumentException("apiKeyPin must be valid");
            }
            this.apiKeyPin = apiKeyPin;
            return this;
        }

        public Builder logLevel(BVLogLevel logLevel) {
            if (logLevel == null) {
                throw new IllegalArgumentException("logLevel must not be null");
            }
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
            confirmBVSDKNotCreated();

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
                this.okHttpClient = new OkHttpClient();
            }

            // Use their OkHttp instance or ours, and add the options we want
            File httpCacheFile = new File(application.getCacheDir(), "bvsdk_http_cache");
            long maxCacheSize = 1024 * 1024 * 10; // 10MiB
            Cache httpCache = new Cache(httpCacheFile, maxCacheSize);
            this.okHttpClient = okHttpClient
                    .newBuilder()
                    .cache(httpCache)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();

            Logger.setLogLevel(logLevel);

            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(SCHEDULED_BV_THREAD_NAME));
            ExecutorService immediateExecutorService = Executors.newFixedThreadPool(1, new NamedThreadFactory(IMMEDIATE_BV_THREAD_NAME));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String versionName = Utils.getVersionName(application.getApplicationContext());
            String versionCode = Utils.getVersionCode(application.getApplicationContext());
            String packageName = Utils.getPackageName(application.getApplicationContext());
            UUID uuid = Utils.getUuid(application.getApplicationContext());
            String shopperMarketingApiRootUrl = bazaarEnvironment == BazaarEnvironment.STAGING ? SHOPPER_MARKETING_API_ROOT_URL_STAGING : SHOPPER_MARKETING_API_ROOT_URL_PRODUCTION;
            String analyticsRootUrl = bazaarEnvironment == BazaarEnvironment.STAGING ? ANALYTICS_ROOT_URL_STAGING : ANALYTICS_ROOT_URL_PRODUCTION;
            List<Integer> profilePollTimes = Arrays.asList(0, 5000, 12000, 24000);
            String bazaarvoiceApiRootUrl = bazaarEnvironment == BazaarEnvironment.STAGING ? BAZAARVOICE_ROOT_URL_STAGING : BAZAARVOICE_ROOT_URL_PRODUCTION;
            BVRootApiUrls endPoints = new BVRootApiUrls(shopperMarketingApiRootUrl, bazaarvoiceApiRootUrl, NOTIFICATION_CONFIG_URL);
            BVAuthenticatedUser bvAuthenticatedUser = new BVAuthenticatedUser(application.getApplicationContext(), shopperMarketingApiRootUrl, apiKeyShopperAdvertising, okHttpClient, gson, profilePollTimes);
            AnalyticsManager analyticsManager = new AnalyticsManager(application.getApplicationContext(), versionName, versionCode, clientId, analyticsRootUrl, okHttpClient, immediateExecutorService, scheduledExecutorService, bvAuthenticatedUser, packageName, uuid);
            BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks = new BVActivityLifecycleCallbacks(analyticsManager);
            BVApiKeys apiKeys = new BVApiKeys(apiKeyShopperAdvertising, apiKeyConversations, apiKeyConversationsStores, apiKeyCurations, apiKeyLocation, apiKeyPin);
            BackgroundThread backgroundThread = new BackgroundThread();
            backgroundThread.start();

            Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    if (msg.what == BVHandlePayload){
                        BVHandlerCallbackPayload payload = (BVHandlerCallbackPayload) msg.obj;
                        payload.getInternalCB().performOnMainThread(payload);
                        return true;
                    }

                    return false;
                }
            });

            singleton = new BVSDK(
                    application,
                    clientId,
                    bazaarEnvironment,
                    apiKeys,
                    logLevel,
                    okHttpClient,
                    analyticsManager,
                    bvActivityLifecycleCallbacks,
                    bvAuthenticatedUser,
                    gson,
                    endPoints,
                    handler,
                    backgroundThread);
            return singleton;
        }
    }

    // endregion

    // region BVSDK Helper Objects

    Context getApplicationContext() {
        return application.getApplicationContext();
    }

    String getClientId() {
        return clientId;
    }

    AnalyticsManager getAnalyticsManager() {
        return analyticsManager;
    }

    Gson getGson() {
        return gson;
    }

    BVRootApiUrls getRootApiUrls() {
        return rootApiUrls;
    }

    OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    BVAuthenticatedUser getAuthenticatedUser() {
        return bvAuthenticatedUser;
    }

    BVApiKeys getApiKeys() {
        return apiKeys;
    }

    String getBvsdkUserAgent() {
        return BVSDK_USER_AGENT;
    }

    Application getApplication() {
        return application;
    }

    Looper getBackgroundLooper() {
        return backgroundThread.getLooper();
    }

    // endregion
}
