/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.bazaarvoice.bvandroidsdk.internal.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bazaarvoice.bvandroidsdk.BVNotificationBroadcastReceiver.ACTION_EXIT_GEOFENCE;
import static com.bazaarvoice.bvandroidsdk.BVNotificationBroadcastReceiver.ACTION_RESCHEDULE_NOTIF;
import static com.bazaarvoice.bvandroidsdk.BVNotificationBroadcastReceiver.ACTION_SCHEDULE_NOTIF;

/**
 * Service for building notification metadata
 */
public final class BVNotificationService extends IntentService {

    // TODO: Need this to point to the cloudfront endpoint instead
    private static final String S3_STATIC_MAPS_URL_TEMPLATE = "https://s3.amazonaws.com/incubator-mobile-apps/conversations-stores/%s/staticmaps/map_%s.png";

    private static final String SERVICE_NAME = Utils.THREAD_PREFIX + BVNotificationService.class.getSimpleName();

    public static final String ACTION_NOTIFICATION_BUTTON_TAPPED = "com.bazaarvoice.bvandroidsdk.action.NOTIFICATION_BUTTON_TAPPED";
    public static final String EXTRA_BUTTON_TAPPED = "extra_button_tapped";
    public static final int POSITIVE = 0;
    public static final int NEUTRAL = 1;
    public static final int NEGATIVE = 2;

    public static final String EXTRA_STORE_ID = "extra_place_id";
    public static final String EXTRA_DWELL_TIME_MILLIS = "extra_dwell_time_millis";

    private OkHttpClient okHttpClient;
    private String clientId;

    public BVNotificationService() {
        super(SERVICE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BVSDK bvsdk = BVSDK.getInstance();
        okHttpClient = bvsdk.getOkHttpClient();
        clientId = bvsdk.getClientId();
    }

    @Override
    protected void onHandleIntent(Intent inputIntent) {
        String inputAction = inputIntent.getAction();
        switch (inputAction) {
            case ACTION_EXIT_GEOFENCE: {
                onExitGeofence(inputIntent);
                break;
            }
            case ACTION_SCHEDULE_NOTIF: {
                onScheduleNotif(inputIntent);
                break;
            }
            case ACTION_RESCHEDULE_NOTIF: {
                onRescheduleNotif(inputIntent);
                break;
            }
        }
    }

    private void onExitGeofence(Intent inputIntent) {
        int notificationId = getNotificationId(inputIntent);
        String storeId = inputIntent.getStringExtra(EXTRA_STORE_ID);
        BvNotificationData bvNotificationData = BVNotificationManager.getInstance().getNotificationData();
        Notification notification = createNotification(storeId, bvNotificationData);
        showNotification(notificationId, notification);
    }

    private void onScheduleNotif(Intent inputIntent) {
        String storeId = inputIntent.getStringExtra(EXTRA_STORE_ID);
        long dwellTimeMillis = inputIntent.getLongExtra(EXTRA_DWELL_TIME_MILLIS, 0);
        BVNotificationManager bvNotificationManager = BVNotificationManager.getInstance();
        BvNotificationData bvNotificationData = bvNotificationManager.getNotificationData();
        bvNotificationManager.initialStartNotificationProcess(bvNotificationData, storeId, dwellTimeMillis);
    }

    private void onRescheduleNotif(Intent inputIntent) {
        String storeId = inputIntent.getStringExtra(EXTRA_STORE_ID);
        BVNotificationManager bvNotificationManager = BVNotificationManager.getInstance();
        BvNotificationData bvNotificationData = bvNotificationManager.getNotificationData();
        bvNotificationManager.startNotificationProcess(false, storeId, bvNotificationData);
    }

    private int getNotificationId(Intent inputIntent) {
        String storeId = inputIntent.getStringExtra(EXTRA_STORE_ID);
        int storeIdInt = 0;
        try {
            storeIdInt = Integer.parseInt(storeId);
        } catch (NumberFormatException e) {
            storeIdInt = 0;
        }
        return storeIdInt;
    }

    private Notification createNotification(String storeId, BvNotificationData bvNotificationData) {
        PendingIntent positiveButtonPendingIntent = createButtonIntent(POSITIVE, storeId);
        PendingIntent neutralPendingIntent = createButtonIntent(NEUTRAL, storeId);
        PendingIntent negativePendingIntent = createButtonIntent(NEGATIVE, storeId);

        Bitmap bigPictureBitmap = getGoogleMapScreenShot(storeId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        final NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(builder)
                .bigPicture(bigPictureBitmap)
                .setBigContentTitle(bvNotificationData.getContentTitleText())
                .setSummaryText(bvNotificationData.getSummaryText());
        builder.setContentTitle(bvNotificationData.getContentTitleText())
                .setStyle(style)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_rate_store)
                .setContentIntent(positiveButtonPendingIntent)
                .addAction(0, bvNotificationData.getPositiveText(), positiveButtonPendingIntent)
                .addAction(0, bvNotificationData.getNeutralText(), neutralPendingIntent)
                .addAction(0, bvNotificationData.getNegativeText(), negativePendingIntent);

        if (bvNotificationData.isHeadsUpEnabled()) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH).setVibrate(new long[]{0, 100});
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        return builder.build();
    }

    private PendingIntent createButtonIntent(int extraButtonTapped, String storeId) {
        Intent intent = new Intent(getApplicationContext(), BVNotificationBroadcastReceiver.class);
        intent.setAction(ACTION_NOTIFICATION_BUTTON_TAPPED);
        intent.putExtra(EXTRA_BUTTON_TAPPED, extraButtonTapped);
        intent.putExtra(EXTRA_STORE_ID, storeId);
        int pendingIntentId = extraButtonTapped;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), pendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    private void showNotification(int id, Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(id, notification);
    }

    /**
     * @param storeId id of store to get image for
     * @return bitmap displaying google map with a pin at the input location
     */
    private Bitmap getGoogleMapScreenShot(String storeId) {
        String staticMapUrl = String.format(S3_STATIC_MAPS_URL_TEMPLATE, clientId, storeId);
        Request staticMapRequest = new Request.Builder()
                .url(staticMapUrl)
                .build();

        Bitmap bitmap = null;

        try {
            Response response = okHttpClient.newCall(staticMapRequest).execute();
            bitmap = Utils.decodeBitmapFromBytes(response.body().bytes(), 400, 260);
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
