/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.bazaarvoice.bvandroidsdk.internal.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Internal SDK API for sending analytics events
 */
class AnalyticsManager {
    // region Properties

    private static final String TAG = AnalyticsManager.class.getSimpleName();
    private static final String ANALYTICS_THREAD_NAME = TAG;
    private static final int ANALYTICS_START_DELAY_SECONDS = 10;
    private static final int ANALYTICS_DELAY_SECONDS = 10;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String KEY_HASHED_IP = "HashedIP";
    private static final String KEY_USER_AGENT = "UA";
    private static final String HASHED_IP = "default";
    private static final String KEY_ADVERTISING_ID = "advertisingId";
    private static final String PATH = "event";

    static final int ENQUEUE_EVENT = 1;
    static final int ENQUEUE_APP_STATE_EVENT = 2;
    static final int ENQUEUE_CONVERSION_TRANSACTION_EVENT = 3;
    static final int ENQUEUE_NON_COMMERCE_CONVERSION_EVENT = 4;
    static final int DISPATCH_SEND_PERSONALIZATION_EVENT = 5;
    static final int DISPATCH_SEND_EVENTS = 6;

    private final AnalyticsThread analyticsThread;
    private final Handler analyticsHandler;
    private final URL url;
    private final AnalyticsBatch analyticsBatch;
    private final Context applicationContext;
    private final ExecutorService immediateExecutorService;
    private final OkHttpClient okHttpClient;
    private final String clientId;
    private final String versionName, versionCode, packageName;
    private final UUID uuid;
    private final BVAuthenticatedUser bvAuthenticatedUser;

    // endregion

    // region Constructor

    AnalyticsManager(final Context applicationContext, final String versionName, final String versionCode, final String clientId, final String baseUrl, final OkHttpClient okHttpClient, final ExecutorService immediateExecutorService, final ScheduledExecutorService scheduledExecutorService, final BVAuthenticatedUser bvAuthenticatedUser, final String packageName, final UUID uuid) {
        this.analyticsBatch = new AnalyticsBatch();
        this.analyticsThread = new AnalyticsThread();
        this.analyticsThread.start();
        this.analyticsHandler = new AnalyticsHandler(analyticsThread.getLooper(), this);
        this.url = Utils.toUrl(baseUrl + PATH);
        this.applicationContext = applicationContext;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.okHttpClient = okHttpClient;
        this.clientId = clientId;
        scheduledExecutorService.scheduleWithFixedDelay(new AnalyticsTask(this), ANALYTICS_START_DELAY_SECONDS, ANALYTICS_DELAY_SECONDS, TimeUnit.SECONDS);
        this.immediateExecutorService = immediateExecutorService;
        this.uuid = uuid;
        this.bvAuthenticatedUser = bvAuthenticatedUser;
    }

    // endregion

    MagpieMobileAppPartialSchema getMagpieMobileAppPartialSchema() {
        return new MagpieMobileAppPartialSchema.Builder().client(clientId).build();
    }

    // region Helper Classes - to form and send analytics

    static class AnalyticsTask implements Runnable {
        private final AnalyticsManager analyticsManager;

        AnalyticsTask(AnalyticsManager analyticsManager) {
            this.analyticsManager = analyticsManager;
        }

        @Override
        public void run() {
            analyticsManager.dispatchSendEvents();
        }
    }

    static class AnalyticsBatch {
        private final LinkedList<Map<String, Object>> eventQueue;

        public AnalyticsBatch() {
            eventQueue = new LinkedList<>();
        }

        public void putEvent(BvAnalyticsSchema schema) {
            eventQueue.add(schema.getDataMap());
        }

        public void clear() {
            eventQueue.clear();
        }

        public boolean isEmpty() {
            return eventQueue.isEmpty();
        }

        public int size() {
            return eventQueue.size();
        }

        public RequestBody toPostPayload() {
            Map<String, LinkedList<Map<String, Object>>> batch = new HashMap<>();
            batch.put("batch", eventQueue);

            JSONObject batchToSend = new JSONObject(batch);

            return RequestBody.create(JSON, batchToSend.toString());
        }

        public void log(boolean full) {
            String tag = "BVAnalyticsVerify";

            for (Map<String, Object> event : eventQueue) {
                if (full) {
                    Logger.d(tag, "{");
                    for (Map.Entry<String, Object> entry : event.entrySet()) {
                        Logger.d(tag, "  " + entry.getKey() + ":" + entry.getValue());
                    }
                    Logger.d(tag, "}");
                } else {
                    Logger.d(tag, event.get("type") + " - " + event.get("cl") + " - " + event.get("source") + getSmartExtraInfo(event));
                    //JSONObject log = new JSONObject(event);
                    //Logger.d(tag, log.toString());
                }
            }
        }

        private String getSmartExtraInfo(Map<String, Object> event) {
            StringBuilder stringBuilder = new StringBuilder();
            if (event.containsKey("name")) {
                stringBuilder.append(" - name:" + event.get("name"));
            }
            if (event.containsKey("transition")) {
                stringBuilder.append(" - transition:" + event.get("transition"));
            }
            if (event.containsKey("durationSecs")) {
                stringBuilder.append(" - durationSecs:" + event.get("durationSecs"));
            }
            if (event.containsKey("locationId")) {
                stringBuilder.append(" - locationId:" + event.get("locationId"));
            }
            return stringBuilder.toString();
        }
    }

    static class AnalyticsThread extends HandlerThread {
        AnalyticsThread() {
            super(Utils.THREAD_PREFIX + ANALYTICS_THREAD_NAME, THREAD_PRIORITY_BACKGROUND);
        }
    }

    static class AnalyticsHandler extends Handler {
        private final AnalyticsManager analyticsManager;

        public AnalyticsHandler(Looper looper, AnalyticsManager analyticsManager) {
            super(looper);
            this.analyticsManager = analyticsManager;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENQUEUE_EVENT: {
                    BvAnalyticsSchema schema = (BvAnalyticsSchema) msg.obj;
                    analyticsManager.addEventToBatch(schema);
                    break;
                }
                case ENQUEUE_APP_STATE_EVENT: {
                    MobileAppLifecycleSchema.AppState appState = (MobileAppLifecycleSchema.AppState) msg.obj;
                    analyticsManager.addAppStateEvent(appState);
                    break;
                }
                case ENQUEUE_CONVERSION_TRANSACTION_EVENT: {
                    Transaction transaction = (Transaction) msg.obj;
                    analyticsManager.addConversionTransactionEvent(transaction);
                    break;
                }
                case ENQUEUE_NON_COMMERCE_CONVERSION_EVENT: {
                    Conversion conversion = (Conversion) msg.obj;
                    analyticsManager.addNonCommerceConversionEvent(conversion);
                    break;
                }
                case DISPATCH_SEND_PERSONALIZATION_EVENT: {
                    analyticsManager.sendPersonalizationEvent();
                    break;
                }
                case DISPATCH_SEND_EVENTS: {
                    analyticsManager.sendAnalytics();
                    break;
                }
            }
        }
    }

    // endregion

    // region API - send analytics through these methods on the main looper

    void dispatchSendPersonalizationEvent() {
        analyticsHandler.sendMessage(analyticsHandler.obtainMessage(DISPATCH_SEND_PERSONALIZATION_EVENT));
    }

    void dispatchSendEvents() {
        analyticsHandler.sendMessage(analyticsHandler.obtainMessage(DISPATCH_SEND_EVENTS));
    }

    void enqueueEvent(BvAnalyticsSchema analyticsSchema) {
        analyticsHandler.sendMessage(analyticsHandler.obtainMessage(ENQUEUE_EVENT, analyticsSchema));
    }

    void enqueueAppStateEvent(MobileAppLifecycleSchema.AppState appState) {
        analyticsHandler.sendMessage(analyticsHandler.obtainMessage(ENQUEUE_APP_STATE_EVENT, appState));
    }

    void enqueueConversionTransactionEvent(Transaction transaction) {
        analyticsHandler.sendMessage(analyticsHandler.obtainMessage(ENQUEUE_CONVERSION_TRANSACTION_EVENT, transaction));
    }

    void enqueueNonCommerceConversionEvent(Conversion conversion) {
        analyticsHandler.sendMessage(analyticsHandler.obtainMessage(ENQUEUE_NON_COMMERCE_CONVERSION_EVENT, conversion));
    }

    // endregion

    // region Internal Methods - that manage building and sending analytics events on the AnalyticsThread

    private MobileAppLifecycleSchema getMobileAppLifecycleSchema(MobileAppLifecycleSchema.AppState appState) {
        MobileAppLifecycleSchema.Builder builder = new MobileAppLifecycleSchema.Builder(appState, getMagpieMobileAppPartialSchema(), getProfileCommonPartialSchema())
                .mobileAppIdentifier(packageName)
                .mobileAppVersion(versionName)
                .mobileDeviceName(Build.MODEL)
                .mobileOSVersion(Build.VERSION.RELEASE)
                .bvSDKVersion(BVSDK.SDK_VERSION);

        return builder.build();
    }

    ProfileCommonPartialSchema getProfileCommonPartialSchema() {
        return new ProfileCommonPartialSchema(bvAuthenticatedUser.getUserAuthString());
    }

    void addMagpieData(BvAnalyticsSchema schema) {
        schema.addKeyVal(KEY_HASHED_IP, HASHED_IP);
        schema.addKeyVal(KEY_USER_AGENT, uuid);
        if (schema.allowAdId()) {
            String adId = AdIdRequestTask.getAdId(applicationContext).getAdId();
            schema.addKeyVal(KEY_ADVERTISING_ID, adId);
        }
    }

    void addEventToBatch(BvAnalyticsSchema schema) {
        addMagpieData(schema);
        analyticsBatch.putEvent(schema);
    }

    void addAppStateEvent(MobileAppLifecycleSchema.AppState appState) {
        MobileAppLifecycleSchema schema = getMobileAppLifecycleSchema(appState);
        addEventToBatch(schema);
    }

    void sendPersonalizationEvent() {
        ProfileMobilePersonalizationSchema schema = new ProfileMobilePersonalizationSchema(getMagpieMobileAppPartialSchema(), getProfileCommonPartialSchema());
        addEventToBatch(schema);

        //flush queue, we want to send personalization events right away
        immediateExecutorService.execute(new AnalyticsTask(this));
    }

    void addConversionTransactionEvent(Transaction transaction) {
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = getMagpieMobileAppPartialSchema();
        ConversionTransactionSchema transactionSchema = new ConversionTransactionSchema(magpieMobileAppPartialSchema, transaction);

        addEventToBatch(transactionSchema);
        if (transactionSchema.getPIIEvent() != null){
            addEventToBatch(transactionSchema);
        }
    }

    void addNonCommerceConversionEvent(Conversion conversion) {
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = getMagpieMobileAppPartialSchema();
        ConversionNonCommerceSchema conversionSchema = new ConversionNonCommerceSchema(magpieMobileAppPartialSchema, conversion);

        addEventToBatch(conversionSchema);
        if (conversionSchema.getPIIEvent() != null){
            addEventToBatch(conversionSchema);
        }
    }

    void sendAnalytics() {
        Response response = null;
        try {
            if (analyticsBatch.isEmpty()) {
                return;
            }

            analyticsBatch.log(false);

            RequestBody body = analyticsBatch.toPostPayload();
            Logger.v(TAG, url.toString() + "\n" + analyticsBatch.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("User-Agent", BVSDK.getInstance().getBvsdkUserAgent())
                    .post(body)
                    .build();

            response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                Logger.d("Analytics", "Successfully posted " + analyticsBatch.size() + " events");
            } else {
                Logger.d("Analytics", "Unsuccessfully posted Events: " + response.code() + ", message: " + response.message());
            }
        } catch (IOException e) {
            Logger.e(TAG, "Failed to send analytics event", e);
        } finally {
            analyticsBatch.clear();
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
    }

    String getVersionName() {
        return versionName;
    }

    String getVersionCode() {
        return versionCode;
    }

    String getPackageName() {
        return packageName;
    }

    // endregion
}
