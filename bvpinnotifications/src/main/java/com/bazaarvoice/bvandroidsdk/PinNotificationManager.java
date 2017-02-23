package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.List;

public class PinNotificationManager {

    // region Properties

    public static final String FEATURE_NAME = "com.bazaarvoice.bvandroidsdk_pinnotifications";

    private static final String ANALYTICS_VIEW_NAME = "PINPushNotification";
    private static final String ANALYTICS_CGC_TYPE = "product";
    private static final String REMOTE_CONFIG_FEATURE_NAME = "pin";
    private static final String REMOTE_CONFIG_FILE_NAME = "pinConfig.json";
    private static final int MSG_DISPATCH_SCHEDULE_NOTIFICATION = 1;
    private static final int MSG_DISPATCH_RESCHEDULE_NOTIFICATION = 2;

    private static final String TAG = "PinNotifManager";
    private static PinNotificationManager instance;

    private PinBgHandler pinBgHandler;

    // endregion

    // region Constructor

    private PinNotificationManager() {
        super();
        pinBgHandler = new PinBgHandler(BVSDK.getInstance().getBackgroundLooper(), this);
    }

    // endregion

    // region Public API

    public static PinNotificationManager getInstance() {
        if (instance == null) {
            instance = new PinNotificationManager();
        }
        return instance;
    }

    /**
     * @param productId id of the product to be reviewed
     */
    public void scheduleNotification(final String productId) {
        pinBgHandler.sendMessage(pinBgHandler.obtainMessage(MSG_DISPATCH_SCHEDULE_NOTIFICATION, productId));
    }

    // endregion

    // region Internal API

    void reScheduleNotification(final String productId) {
        pinBgHandler.sendMessage(pinBgHandler.obtainMessage(MSG_DISPATCH_RESCHEDULE_NOTIFICATION, productId));
    }

    private static class PinBgHandler extends Handler {
        private PinNotificationManager pinNotificationManager;

        PinBgHandler(Looper looper, PinNotificationManager pinNotificationManager) {
            super(looper);
            this.pinNotificationManager = pinNotificationManager;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DISPATCH_SCHEDULE_NOTIFICATION: {
                    String productId = (String) msg.obj;
                    pinNotificationManager.dispatchScheduleNotification(productId, true);
                    break;
                }
                case MSG_DISPATCH_RESCHEDULE_NOTIFICATION: {
                    String productId = (String) msg.obj;
                    pinNotificationManager.dispatchScheduleNotification(productId, false);
                    break;
                }
            }
        }
    }

    @WorkerThread
    private void dispatchScheduleNotification(String productId, boolean initialSchedule) {
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        PinNotificationData pinNotificationData = BVNotificationUtil.getNotificationData(
                REMOTE_CONFIG_FEATURE_NAME, REMOTE_CONFIG_FILE_NAME, PinNotificationData.class);
        if (pinNotificationData == null) {
            bvLogger.e(TAG, "Failed to get PIN config data. Cancelling schedule call");
            return;
        }
        if (!pinNotificationData.isNotificationsEnabled()) {
            bvLogger.d(TAG, "PIN notifications are not enabled. Cancelling schedule call");
            return;
        }
        String productImageUrl = PinNotificationManager.getProductImageUrl(productId);
        if (productImageUrl == null) {
            bvLogger.d(TAG, "Failed to get product info. Cancelling schedule call");
            return;
        }
        Context appContext = BVSDK.getInstance().getBvUserProvidedData().getAppContext();

        BVNotificationUtil.ScheduleNotificationData<PinNotificationData> scheduleNotificationData =
                new BVNotificationUtil.ScheduleNotificationData<>(
                        appContext,
                        initialSchedule,
                        productId,
                        pinNotificationData,
                        ANALYTICS_VIEW_NAME,
                        ANALYTICS_CGC_TYPE,
                        FEATURE_NAME);
        BVNotificationDisplayData displayData = new BVNotificationDisplayData(
                productId,
                pinNotificationData.getPositiveText(),
                pinNotificationData.getNeutralText(),
                pinNotificationData.getNegativeText(),
                productImageUrl,
                pinNotificationData.getContentTitleText(),
                pinNotificationData.getSummaryText(),
                pinNotificationData.isHeadsUpEnabled());
        BVNotificationUtil.scheduleNotification(scheduleNotificationData, displayData);
    }

    /**
     * @param productId Conversations id for a Product
     * @return the bitmap of the image from the conversations api
     */
    @WorkerThread @Nullable
    private static String getProductImageUrl(String productId) {
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        String productImageUrl = null;

        ProductDisplayPageRequest pdpRequest = new ProductDisplayPageRequest.Builder(productId).build();
        BVConversationsClient client = new BVConversationsClient();
        try {
            ProductDisplayPageResponse pdpResponse = client.prepareCall(pdpRequest).loadSync();
            List<Product> products = pdpResponse.getResults();
            if (products != null && products.size() == 1) {
                Product product = products.get(0);
                productImageUrl = product.getDisplayImageUrl();
            } else {
                bvLogger.e(TAG, "No products found for " + productId);
            }
        } catch (BazaarException e) {
            bvLogger.e(TAG, "Failed to get PDPResponse for " + productId, e);
        }

        return productImageUrl;
    }

    // endregion

}
