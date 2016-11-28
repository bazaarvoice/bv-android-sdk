package com.bazaarvoice.bvandroidsdk;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class PinNotificationService extends IntentService {

    private static final String TAG = "PinNotifService";

    public PinNotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String inputAction = intent.getAction();
        switch (inputAction) {
            case BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED: {
                onNotificationButtonTapped(intent);
                break;
            }
        }
    }

    private void onNotificationButtonTapped(Intent intent) {
        String featureName = BVNotificationService.getFeatureName(intent);
        switch (featureName) {
            case PinNotificationManager.FEATURE_NAME: {
                int buttonInt = BVNotificationService.getButtonTapped(intent);
                String buttonTapped = "";
                String productId = BVNotificationService.getNotificationCgcId(intent);
                switch (buttonInt) {
                    case BVNotificationService.POSITIVE: {
                        buttonTapped = "positive";
                        break;
                    }
                    case BVNotificationService.NEUTRAL: {
                        buttonTapped = "neutral";
                        PinNotificationManager.getInstance().reScheduleNotification(productId);
                        break;
                    }
                    case BVNotificationService.NEGATIVE: {
                        buttonTapped = "negative";
                        break;
                    }
                }
                Logger.d(TAG, "PIN notification tapped " + buttonTapped + " for productId " + productId);
                break;
            }
        }
    }
}
