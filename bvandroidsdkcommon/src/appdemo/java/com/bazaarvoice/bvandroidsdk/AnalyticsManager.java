/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.os.Build;

import java.net.MalformedURLException;
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

import static com.bazaarvoice.bvandroidsdk.Utils.mapPutSafe;

/**
 * Internal SDK API for sending analytics events
 */
class AnalyticsManager {

    private static final String TAG = AnalyticsManager.class.getSimpleName();
    private static final String BASEURL = "https://network.bazaarvoice.com/event";
    private static final String BASEURLSTAGING = "https://network-stg.bazaarvoice.com/event";
    private static final int ANALYTICS_START_DELAY_SECONDS = 10;
    private static final int ANALYTICS_DELAY_SECONDS = 10;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String KEY_HASHED_IP = "HashedIP";
    private static final String KEY_USER_AGENT = "UA";
    private static final String HASHED_IP = "default";

    private URL url;
    private LinkedList<Map<String, Object>> eventQueue = new LinkedList<>();

    private final ExecutorService immediateExecutorService;
    private OkHttpClient okHttpClient;
    private String clientId;
    private BVAuthenticatedUser bvAuthenticatedUser;
    private BazaarEnvironment bazaarEnvironment;

    private String versionName, versionCode, packageName;
    private UUID uuid;

    AnalyticsManager(final String versionName, String versionCode, String clientId, BazaarEnvironment environment, AdvertisingIdClient advertisingIdClient, OkHttpClient okHttpClient, ExecutorService immediateExecutorService, ScheduledExecutorService scheduledExecutorService, final BVAuthenticatedUser bvAuthenticatedUser, final String packageName, final UUID uuid) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.okHttpClient = okHttpClient;
        this.clientId = clientId;
        this.bazaarEnvironment = environment;
        this.url = getAnalyticsUrlAppDemo(environment);
        scheduledExecutorService.scheduleWithFixedDelay(new BvAnalyticsTask(), ANALYTICS_START_DELAY_SECONDS, ANALYTICS_DELAY_SECONDS, TimeUnit.SECONDS);
        this.immediateExecutorService = immediateExecutorService;
        this.bvAuthenticatedUser = bvAuthenticatedUser;
        this.uuid = uuid;

        advertisingIdClient.getAdInfo(new BVSDK.GetAdInfoCompleteAction() {
            @Override
            public void completionAction(AdInfo adInfo) {
                bvAuthenticatedUser.setUserAdvertisingId(adInfo.getId());
                bvAuthenticatedUser.setIsLimitAdTrackingEnabled(adInfo.isLimitAdTrackingEnabled());

                if (bvAuthenticatedUser.getUserAuthString() != null) {
                    sendPersonalizationEvent();
                }
            }
        });
    }

    private URL getAnalyticsUrlAppDemo(BazaarEnvironment environment) {
        URL url = null;
        try {
            url = new URL(environment == BazaarEnvironment.STAGING ? BASEURLSTAGING : BASEURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private final class BvAnalyticsTask implements Runnable {
        @Override
        public void run() {
            synchronized (AnalyticsManager.this) {
                if (eventQueue.isEmpty()) {
                    return;
                }

                Map<String, LinkedList<Map<String, Object>>> batch = new HashMap<>();
                batch.put("batch", eventQueue);

                int numEvents = eventQueue.size();

                Utils.printAnalytics(eventQueue);

                Logger.d("Analytics", "Successfully saw " + numEvents + " events. Not sending for demo app.");
                if (!eventQueue.isEmpty()) {
                    eventQueue.clear();
                }
            }
        }
    }

    MagpieMobileAppPartialSchema getMagpieMobileAppPartialSchema() {
        return new MagpieMobileAppPartialSchema.Builder().advertisingId(bvAuthenticatedUser.getUserAdvertisingId()).client(clientId).build();
    }

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

    /**
     * Queues up data to be added to batch analytics
     * @param eventData
     */
    public synchronized void enqueueEvent(Map<String, Object> eventData) {
        addMagpieData(eventData);
        eventQueue.add(eventData);
    }

    private void addMagpieData(Map<String, Object> eventData) {
        mapPutSafe(eventData, KEY_HASHED_IP, HASHED_IP);
        mapPutSafe(eventData, KEY_USER_AGENT, uuid);
    }

    public void sendAppStateEvent(MobileAppLifecycleSchema.AppState appState) {
        MobileAppLifecycleSchema schema = getMobileAppLifecycleSchema(appState);
        enqueueEvent(schema.getDataMap());
    }

    public void sendPersonalizationEvent() {
        ProfileMobilePersonalizationSchema schema = new ProfileMobilePersonalizationSchema(getMagpieMobileAppPartialSchema(), getProfileCommonPartialSchema());
        enqueueEvent(schema.getDataMap());

        //flush queue, we want to send personalization events right away
        immediateExecutorService.execute(new BvAnalyticsTask());
    }

    /**
     * Event when a transaction occurs
     *
     * @param transaction
     */
    public void sendConversionTransactionEvent(Transaction transaction) {
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = getMagpieMobileAppPartialSchema();
        ConversionTransactionSchema transactionSchema = new ConversionTransactionSchema(magpieMobileAppPartialSchema, transaction);

        enqueueEvent(transactionSchema.getDataMap());
        if (transactionSchema.getPIIEvent() != null){
            enqueueEvent(transactionSchema.getPIIEvent());
        }
    }

    /**
     * Event when a transaction occurs
     *
     * @param conversion
     */
    public void sendNonCommerceConversionEvent(Conversion conversion) {
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = getMagpieMobileAppPartialSchema();
        ConversionNonCommerceSchema conversionSchema = new ConversionNonCommerceSchema(magpieMobileAppPartialSchema, conversion);

        enqueueEvent(conversionSchema.getDataMap());
        if (conversionSchema.getPIIEvent() != null){
            enqueueEvent(conversionSchema.getPIIEvent());
        }
    }

    /**
     * Adds personalization data to an event
     * @param eventData
     */
    void addPersonalizationData(Map<String, Object> eventData) {
        eventData.put("type","ProfileMobile");
        eventData.put("cl","Personalization");
        eventData.put("bvProduct", "PersonalizationProfile");
    }
}
