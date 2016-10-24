/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

/**
 * BroadcastReceiver for receiving notifications to start building
 * notification metadata
 */
public final class BVNotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String EXTRA_STORE_ID = "extra_place_id";
    public static final String ACTION_EXIT_GEOFENCE = "com.bazaarvoice.bvandroidsdk.action.EXIT_GEOFENCE";
    public static final String ACTION_SCHEDULE_NOTIF = "com.bazaarvoice.bvandroidsdk.action.SCHEDULE_NOTIF";
    public static final String ACTION_RESCHEDULE_NOTIF = "com.bazaarvoice.bvandroidsdk.action.RESCHEDULE_NOTIF";
    private static final String TAG = "BvNoteBroadcastReceiver";

    public void onReceive(Context context, Intent inputIntent) {
        String inputAction = inputIntent.getAction();
        switch (inputAction) {
            case ACTION_EXIT_GEOFENCE: {
                onExitGeofenceBroadcast(context, inputIntent);
                break;
            }
            case BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED: {
                onNotifButtonTapBroadcast(context, inputIntent);
                break;
            }
        }
    }

    /**
     * When an {@link #ACTION_EXIT_GEOFENCE} broadcast is received, send info through
     * to the {@link BVNotificationService}
     *
     * @param context Application context
     * @param inputIntent Intent that was sent with the broadcast
     */
    private void onExitGeofenceBroadcast(Context context, Intent inputIntent) {
        String storeId = inputIntent.getStringExtra(EXTRA_STORE_ID);
        Intent outputIntent = new Intent(context, BVNotificationService.class);
        outputIntent.setAction(ACTION_EXIT_GEOFENCE);
        outputIntent.putExtra(BVNotificationService.EXTRA_STORE_ID, storeId);
        context.startService(outputIntent);
    }

    /**
     * When an {@link BVNotificationService#ACTION_NOTIFICATION_BUTTON_TAPPED} broadcast
     * is received, process it depending on which button is tapped.
     * <ul>
     *     <li>{@link BVNotificationService#POSITIVE} - Notify all listeners in
     *     {@link BVLocationManager#rateStoreListeners}, then cancel the Notification</li>
     *     <li>{@link BVNotificationService#NEUTRAL} - Reschedule the notification by starting
     *     the {@link BVNotificationService}, and cancel the Notification</li>
     *     <li>{@link BVNotificationService#NEGATIVE} - Cancel the Notification</li>
     * </ul>
     *
     * @param context Application context
     * @param inputIntent Intent that was sent with the broadcast
     */
    private void onNotifButtonTapBroadcast(Context context, Intent inputIntent) {
        String storeId = inputIntent.getStringExtra(BVNotificationService.EXTRA_STORE_ID);
        int storeIdInt = Integer.parseInt(storeId);
        int whichButton = inputIntent.getIntExtra(BVNotificationService.EXTRA_BUTTON_TAPPED, -1);
        switch (whichButton) {
            case BVNotificationService.POSITIVE: {
                BVLocationManager bvLocationManager = BVLocationManager.getInstance();
                for (BVLocationManager.BVRateStoreListener rateStoreListener : bvLocationManager.getRateStoreListeners().getListeners()) {
                    rateStoreListener.onRateStoreClicked(storeId);
                }
                NotificationManagerCompat.from(context).cancel(storeIdInt);
                BVNotificationAnalyticsManager.sendNotificationEventForStoreReviewFeatureUsed(BVNotificationAnalyticsManager.NotificationAction.Positive.getKey(), storeId);
                break;
            }
            case BVNotificationService.NEUTRAL: {
                BVNotificationManager.getInstance().rescheduleNotification(storeId);
                BVNotificationAnalyticsManager.sendNotificationEventForStoreReviewFeatureUsed(BVNotificationAnalyticsManager.NotificationAction.Neutral.getKey(), storeId);
                NotificationManagerCompat.from(context).cancel(storeIdInt);
                break;
            }
            case BVNotificationService.NEGATIVE: {
                NotificationManagerCompat.from(context).cancel(storeIdInt);
                BVNotificationAnalyticsManager.sendNotificationEventForStoreReviewFeatureUsed(BVNotificationAnalyticsManager.NotificationAction.Negative.getKey(), storeId);
                break;
            }
        }
    }
}