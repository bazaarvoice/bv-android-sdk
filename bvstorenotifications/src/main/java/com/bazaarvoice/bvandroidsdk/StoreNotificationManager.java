package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.WorkerThread;

public class StoreNotificationManager {

    // region Properties
    public static final String FEATURE_NAME = "com.bazaarvoice.bvandroidsdk_storenotifications";

    private static final String ANALYTICS_VIEW_NAME = "StoreReviewPushNotification";
    private static final String ANALYTICS_CGC_TYPE = "store";
    private static final String REMOTE_CONFIG_FEATURE_NAME = "conversations-stores";
    private static final String REMOTE_CONFIG_FILE_NAME = "geofenceConfig.json";
    private static final int MSG_DISPATCH_SCHEDULE_NOTIFICATION = 1;
    private static final int MSG_DISPATCH_RESCHEDULE_NOTIFICATION = 2;
    private static final String S3_STATIC_MAPS_URL_TEMPLATE = "https://s3.amazonaws.com/incubator-mobile-apps/conversations-stores/%s/staticmaps/map_%s.png";
    private static final String TAG = "StoreNotifManager";
    private static StoreNotificationManager instance;

    private StoreBgHandler storeBgHandler;

    // endregion

    // region Constructor

    private StoreNotificationManager() {
        super();
        storeBgHandler = new StoreBgHandler(BVSDK.getInstance().getBvWorkerData().getBackgroundLooper(), this);
    }

    // endregion

    // region Public API

    public static StoreNotificationManager getInstance() {
        if (instance == null) {
            instance = new StoreNotificationManager();
        }
        return instance;
    }

    /**
     * @param storeId Bazaarvoice generated id for a store
     * @param dwellTimeMillis Milliseconds dwelled at a store to help determine whether to continue
     */
    public void scheduleNotification(final String storeId, final long dwellTimeMillis) {
        ScheduleStoreNotificationPayload payload = new ScheduleStoreNotificationPayload(storeId, dwellTimeMillis);
        storeBgHandler.sendMessage(storeBgHandler.obtainMessage(MSG_DISPATCH_SCHEDULE_NOTIFICATION, payload));
    }

    // endregion

    // region Internal API

    void reScheduleNotification(final String storeId) {
        // Don't know dwellTime for reschedule, but it's not relevant
        ScheduleStoreNotificationPayload payload = new ScheduleStoreNotificationPayload(storeId, 0);
        storeBgHandler.sendMessage(storeBgHandler.obtainMessage(MSG_DISPATCH_RESCHEDULE_NOTIFICATION, payload));
    }

    /**
     * @param storeId Bazaarvoice id for the store
     * @param clientId Bazaarvoice id for the client
     * @return url of image displaying google map with a pin at the input location
     */
    private static String getGoogleMapScreenShot(String storeId, String clientId) {
        return String.format(S3_STATIC_MAPS_URL_TEMPLATE, clientId, storeId);
    }

    private static class ScheduleStoreNotificationPayload {
        private String storeId;
        private long dwellMillis;

        public ScheduleStoreNotificationPayload(String storeId, long dwellMillis) {
            this.storeId = storeId;
            this.dwellMillis = dwellMillis;
        }

        public String getStoreId() {
            return storeId;
        }

        public long getDwellMillis() {
            return dwellMillis;
        }
    }

    private static class StoreBgHandler extends Handler {
        private StoreNotificationManager storeNotificationManager;
        StoreBgHandler(Looper looper, StoreNotificationManager storeNotificationManager) {
            super(looper);
            this.storeNotificationManager = storeNotificationManager;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DISPATCH_SCHEDULE_NOTIFICATION: {
                    ScheduleStoreNotificationPayload payload = (ScheduleStoreNotificationPayload) msg.obj;
                    storeNotificationManager.dispatchScheduleNotification(payload.getStoreId(), payload.getDwellMillis(), true);
                    break;
                }
                case MSG_DISPATCH_RESCHEDULE_NOTIFICATION: {
                    ScheduleStoreNotificationPayload payload = (ScheduleStoreNotificationPayload) msg.obj;
                    storeNotificationManager.dispatchScheduleNotification(payload.getStoreId(), payload.getDwellMillis(), false);
                    break;
                }
            }
        }
    }

    @WorkerThread
    private void dispatchScheduleNotification(String storeId, long dwellTimeMillis, boolean initialSchedule) {
        BVUserProvidedData bvUserProvidedData = BVSDK.getInstance().getBvUserProvidedData();
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        String clientId = bvUserProvidedData.getBvConfig().getClientId();
        StoreNotificationData storeNotificationData = BVNotificationUtil.getNotificationData(
                REMOTE_CONFIG_FEATURE_NAME, REMOTE_CONFIG_FILE_NAME, StoreNotificationData.class);
        if (storeNotificationData == null) {
            bvLogger.e(TAG, "Failed to get PIN config data. Cancelling schedule call");
            return;
        }
        if (!storeNotificationData.isNotificationsEnabled()) {
            bvLogger.d(TAG, "PIN notifications are not enabled. Cancelling schedule call");
            return;
        }
        boolean dwelledLongEnough = dwellTimeMillis >= storeNotificationData.getVisitDurationMillis();
        if (!dwelledLongEnough && initialSchedule) {
            // Only valid for the initial exit geofence event. Not valid for reschedule button tap
            bvLogger.d(TAG, "Was at geofence, but not long enough to trigger PIN. Cancelling schedule call");
            return;
        }
        String productImageUrl = StoreNotificationManager.getGoogleMapScreenShot(storeId, clientId);
        Context appContext = bvUserProvidedData.getAppContext();
        BVNotificationUtil.ScheduleNotificationData<StoreNotificationData> scheduleNotificationData =
                new BVNotificationUtil.ScheduleNotificationData<>(
                        appContext,
                        true,
                        storeId,
                        storeNotificationData,
                        ANALYTICS_VIEW_NAME,
                        ANALYTICS_CGC_TYPE,
                        FEATURE_NAME);
        BVNotificationDisplayData displayData = new BVNotificationDisplayData(
                storeId,
                storeNotificationData.getPositiveText(),
                storeNotificationData.getNeutralText(),
                storeNotificationData.getNegativeText(),
                productImageUrl,
                storeNotificationData.getContentTitleText(),
                storeNotificationData.getSummaryText(),
                storeNotificationData.isHeadsUpEnabled());
        BVNotificationUtil.scheduleNotification(scheduleNotificationData, displayData);
    }

    // endregion

}
