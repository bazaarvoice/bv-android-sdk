package com.bazaarvoice.bvsdkdemoandroid.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bazaarvoice.bvandroidsdk.BVNotificationService;
import com.bazaarvoice.bvsdkdemoandroid.pin.DemoRateActivity;

public class DemoLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED: {
                onNotificationButtonTappedEvent(context, intent);
            }
        }
    }

    private void onNotificationButtonTappedEvent(Context context, Intent intent) {
        String cgcId = BVNotificationService.getNotificationCgcId(intent);
        int buttonTapped = BVNotificationService.getButtonTapped(intent);
        String featureName = BVNotificationService.getFeatureName(intent);
        switch (buttonTapped) {
            case BVNotificationService.POSITIVE: {
                Intent launchIntent = new Intent(context, DemoRateActivity.class);
                launchIntent.putExtra("extra_activity", "activity_rate");
                launchIntent.putExtra("extra_cgc_id", cgcId);
                launchIntent.putExtra("extra_feature_name", featureName);
                context.startActivity(launchIntent);
                break;
            }
            case BVNotificationService.NEUTRAL: {
                break;
            }
            case BVNotificationService.NEGATIVE: {
                break;
            }
        }
    }
}
