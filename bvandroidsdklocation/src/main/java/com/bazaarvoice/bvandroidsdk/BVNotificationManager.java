/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Manages queueing notifications
 */
public final class BVNotificationManager {
    private static final String PUSH_NOTIFICATION_VIEW_NAME = "StoreReviewPushNotification";
    private static final String TAG = BVNotificationManager.class.getSimpleName();
    private static final String S3_LABS_BASE_URL = "https://s3.amazonaws.com";
    private static final String S3_CONFIG_URL_TEMPLATE = S3_LABS_BASE_URL + "/incubator-mobile-apps/conversations-stores/%s/android/geofenceConfig.json";
    private static BVNotificationManager instance;
    private Context applicationContext;

    private BVNotificationManager() {
        BVSDK bvsdk = BVSDK.getInstance();
        this.applicationContext = bvsdk.getApplicationContext();
    }

    public static BVNotificationManager getInstance() {
        if (instance == null) {
            instance = new BVNotificationManager();
        }
        return instance;
    }

    /**
     * Called upon exiting a Geofence, and will conditionally continue depending on config.
     * Queues up an AsyncTask to fetch the latest Config data, thus must be sent in a
     * service to ensure it completes.
     *
     * @param storeId Bazaarvoice generated id for a store
     * @param dwellTimeMillis Milliseconds dwelled at a store to help determine whether to continue
     */
    public void scheduleNotification(final String storeId, final long dwellTimeMillis) {
        Intent intent = new Intent(applicationContext, BVNotificationService.class);
        intent.setAction(BVNotificationBroadcastReceiver.ACTION_SCHEDULE_NOTIF);
        intent.putExtra(BVNotificationService.EXTRA_STORE_ID, storeId);
        intent.putExtra(BVNotificationService.EXTRA_DWELL_TIME_MILLIS, dwellTimeMillis);
        applicationContext.startService(intent);
    }

    /**
     * Called when user taps 'Later' option in the notification, and will use config to
     * determine when to reschedule the notification for. Queues up an AsyncTask to
     * fetch the latest Config data, thus must be sent in a service to ensure it
     * completes.
     *
     * @param storeId Bazaarvoice generated id for a store
     */
    public void rescheduleNotification(final String storeId) {
        Intent intent = new Intent(applicationContext, BVNotificationService.class);
        intent.setAction(BVNotificationBroadcastReceiver.ACTION_RESCHEDULE_NOTIF);
        intent.putExtra(BVNotificationService.EXTRA_STORE_ID, storeId);
        applicationContext.startService(intent);
    }

    /**
     *
     * For service to call after it has retreived the {@link BvNotificationData}.
     * <br><br>
     * Should be called on first attempt to show notification, checking first for whether
     * we are allowed to display it, and if so moves forward with scheduling it.
     *
     * @param bvNotificationData The configuration file for client notifications
     * @param storeId The Bazaarvoice associated id for a store
     * @param dwellTimeMillis The amount of time the user dwelled at this location which may
     *                        affect whether we decide to move forward with scheduling a notification
     */
    void initialStartNotificationProcess(BvNotificationData bvNotificationData, String storeId, long dwellTimeMillis) {
        Log.d(TAG, bvNotificationData.toString());
        if (shouldStartNotificationProcess(bvNotificationData, dwellTimeMillis)) {
            startNotificationProcess(true, storeId, bvNotificationData);
        }
    }

    private boolean shouldStartNotificationProcess(BvNotificationData bvNotificationData, long dwellTimeMillis) {
        return bvNotificationData.isNotificationsEnabled() && dwellTimeMillis >= bvNotificationData.getVisitDurationMillis();
    }

    /**
     * For service to call after it has retrieved the {@link BvNotificationData}.
     * <br><br>
     * May be called once we know that we are approved to move forward with scheduling a notification.
     *
     * @param initialSchedule Whether this is the first schedule notification event or a reschedule from
     *                        the neutral response
     * @param storeId The Bazaarvoice associated id for a store
     * @param bvNotificationData The configuration file for client notifications
     */
    void startNotificationProcess(boolean initialSchedule, String storeId, BvNotificationData bvNotificationData) {
        PendingIntent alarmPendingIntent = getAlarmPendingIntent(storeId);
        long timeForAlarmMillis = getTimeForAlarm(initialSchedule, bvNotificationData);
        setAlarm(timeForAlarmMillis, alarmPendingIntent);

        BVNotificationAnalyticsManager.sendNotificationEventForNotificationInView(PUSH_NOTIFICATION_VIEW_NAME, storeId);
    }

    private PendingIntent getAlarmPendingIntent(String storeId) {
        Intent notificationIntent = new Intent(applicationContext, BVNotificationBroadcastReceiver.class);
        notificationIntent.setAction(BVNotificationBroadcastReceiver.ACTION_EXIT_GEOFENCE);
        notificationIntent.putExtra(BVNotificationBroadcastReceiver.EXTRA_STORE_ID, storeId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    /**
     * Time returned depends on which type of schedule this is,
     * <ul>
     *     <li>Initial Schedule - Upon exiting a geofence we will check the clients
     *     {@link BvNotificationData#getNotificationDelayMillis()} to get schedule time</li>
     *     <li>Reschedule - Upon a user tapping the {@link BVNotificationService#NEUTRAL}
     *     response, this time will reflect the client
     *     {@link BvNotificationData#getReviewRemindLaterDurationMillis()} to get schedule
     *     time</li>
     * </ul>
     *
     * @return Time in millis for the alarm to send a broadcast
     */
    private long getTimeForAlarm(boolean initialSchedule, BvNotificationData bvNotificationData) {
        long timeForAlarmMillis;
        long elapsedTimeMillis = SystemClock.elapsedRealtime();
        if (initialSchedule) {
            timeForAlarmMillis = elapsedTimeMillis + bvNotificationData.getNotificationDelayMillis();
        } else {
            timeForAlarmMillis = elapsedTimeMillis + bvNotificationData.getReviewRemindLaterDurationMillis();
        }
        return timeForAlarmMillis;
    }

    private void setAlarm(long timeForAlarmMillis, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, timeForAlarmMillis, pendingIntent);
    }

    @WorkerThread
    BvNotificationData getNotificationData() {
        BVSDK bvsdk = BVSDK.getInstance();
        OkHttpClient okHttpClient = bvsdk.getOkHttpClient();
        Gson gson = bvsdk.getGson();
        String clientId = bvsdk.getClientId();
        BvNotificationData bvNotificationData = new BvNotificationData();
        String configUrl = String.format(S3_CONFIG_URL_TEMPLATE, clientId);
        Request request = new Request.Builder().url(configUrl).build();
        Log.d(TAG, "configUrl: " + configUrl);
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseString = response.body().string();
            response.body().close();
            bvNotificationData = gson.fromJson(responseString, BvNotificationData.class);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        return bvNotificationData;
    }


}