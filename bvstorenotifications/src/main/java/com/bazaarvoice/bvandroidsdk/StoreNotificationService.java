package com.bazaarvoice.bvandroidsdk;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public final class StoreNotificationService extends IntentService {

    private static final String TAG = "StoreNotifService";

    public StoreNotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String inputAction = intent.getAction();
        switch (inputAction) {
            case BVLocationManager.ACTION_GEOFENCE_VISIT: {
                onGeofenceEvent(intent);
                break;
            }
            case BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED: {
                onNotificationButtonTappedEvent(intent);
                break;
            }
        }
    }

    private void onGeofenceEvent(Intent intent) {
        BVVisit bvVisit = BVLocationManager.getBvVisit(intent);
        StoreNotificationManager.getInstance()
                .scheduleNotification(bvVisit.getStoreId(), bvVisit.getDwellTimeMillis());
    }

    private void onNotificationButtonTappedEvent(Intent intent) {
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        String featureName = BVNotificationService.getFeatureName(intent);
        switch (featureName) {
            case StoreNotificationManager.FEATURE_NAME: {
                int buttonInt = BVNotificationService.getButtonTapped(intent);
                String buttonTapped = "";
                String storeId = BVNotificationService.getNotificationCgcId(intent);
                switch (buttonInt) {
                    case BVNotificationService.POSITIVE: {
                        buttonTapped = "positive";
                        break;
                    }
                    case BVNotificationService.NEUTRAL: {
                        buttonTapped = "neutral";
                        StoreNotificationManager.getInstance().reScheduleNotification(storeId);
                        break;
                    }
                    case BVNotificationService.NEGATIVE: {
                        buttonTapped = "negative";
                        break;
                    }
                }
                bvLogger.d(TAG, "Store notification tapped " + buttonTapped + " for storeId " + storeId);
                break;
            }
        }

    }
}
