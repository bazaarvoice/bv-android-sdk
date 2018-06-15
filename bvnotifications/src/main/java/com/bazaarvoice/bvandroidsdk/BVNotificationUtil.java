package com.bazaarvoice.bvandroidsdk;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.bazaarvoice.bvandroidsdk_notifications.R;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.bazaarvoice.bvandroidsdk.BVNotificationService.ACTION_NOTIFICATION_BUTTON_TAPPED;
import static com.bazaarvoice.bvandroidsdk.BVNotificationService.EXTRA_BUTTON_TAPPED;
import static com.bazaarvoice.bvandroidsdk.BVNotificationService.EXTRA_CGC_ID;
import static com.bazaarvoice.bvandroidsdk.BVNotificationService.EXTRA_DISPLAY_DATA;
import static com.bazaarvoice.bvandroidsdk.BVNotificationService.EXTRA_ANALYTICS_CGC_TYPE;
import static com.bazaarvoice.bvandroidsdk.BVNotificationService.EXTRA_FEATURE_NAME;
import static com.bazaarvoice.bvandroidsdk.BVNotificationService.getDisplayData;

class BVNotificationUtil {

    // region Properties

    private static final long[] DEFAULT_VIBRATE_PATTERN = new long[]{0, 100};
    private static final int DEFAULT_NOTIF_VISIBILITY = NotificationCompat.VISIBILITY_PUBLIC;

    // endregion

    // region Set Alarm to Schedule

    static final class ScheduleNotificationData<NotificationDataType extends BVNotificationData> {
        private Context appContext;
        private boolean initialSchedule;
        private String cgcId;
        private NotificationDataType notificationData;
        private String analyticsViewName;
        private String analyticsCgcType;
        private String featureName;

        Context getAppContext() {
            return appContext;
        }

        boolean isInitialSchedule() {
            return initialSchedule;
        }

        String getCgcId() {
            return cgcId;
        }

        NotificationDataType getNotificationData() {
            return notificationData;
        }

        public String getAnalyticsViewName() {
            return analyticsViewName;
        }

        public String getAnalyticsCgcType() {
            return analyticsCgcType;
        }

        public String getFeatureName() {
            return featureName;
        }

        ScheduleNotificationData(
                Context appContext,
                boolean initialSchedule,
                String cgcId,
                NotificationDataType notificationData,
                String analyticsViewName,
                String analyticsCgcType,
                String featureName) {
            this.appContext = appContext;
            this.initialSchedule = initialSchedule;
            this.cgcId = cgcId;
            this.notificationData = notificationData;
            this.analyticsViewName = analyticsViewName;
            this.analyticsCgcType = analyticsCgcType;
            this.featureName = featureName;
        }
    }

    @WorkerThread
    static <NotificationDataType extends BVNotificationData> void scheduleNotification(ScheduleNotificationData<NotificationDataType> data, BVNotificationDisplayData displayData) {
        Context appContext = data.getAppContext();

        PendingIntent alarmPendingIntent = getAlarmPendingIntent(appContext, data.getCgcId(), data.getAnalyticsCgcType(), data.getFeatureName(), displayData);
        long timeForAlarmMillis = getTimeForAlarm(data.isInitialSchedule(), data.getNotificationData());
        setAlarm(data.getAppContext(), timeForAlarmMillis, alarmPendingIntent);

        BVNotificationAnalyticsManager.sendNotificationEventForNotificationInView(data.getCgcId(), data.getAnalyticsViewName(), data.getAnalyticsCgcType());
    }

    /**
     * @param appContext
     * @param displayData
     * @return A {@link PendingIntent} that will queue up an Intent to trigger the
     * actual display of a Notification
     */
    private static PendingIntent getAlarmPendingIntent(Context appContext, String cgcId, String analyticsCgcType, String featureName, BVNotificationDisplayData displayData) {
        Intent notificationIntent = new Intent(appContext, BVNotificationService.class);
        notificationIntent.setAction(BVNotificationService.ACTION_SHOW_NOTIF);

        Gson gson = BVSDK.getInstance().getBvWorkerData().getGson();
        String displayDataStr = gson.toJson(displayData);
        notificationIntent.putExtra(EXTRA_DISPLAY_DATA, displayDataStr);
        notificationIntent.putExtra(EXTRA_CGC_ID, cgcId);
        notificationIntent.putExtra(EXTRA_ANALYTICS_CGC_TYPE, analyticsCgcType);
        notificationIntent.putExtra(EXTRA_FEATURE_NAME, featureName);

        // String contentTitleText
        // String summaryText
        // String positiveText
        // String neutralText
        // boolean isHeadsUpEnabled

        return PendingIntent.getService(appContext, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /**
     * Time returned depends on which type of schedule this is,
     * <ul>
     *     <li>Initial Schedule - During the first attempt to display a notification we will
     *     check the clients {@link BVNotificationData#getNotificationDelayMillis()} to
     *     get schedule time</li>
     *     <li>Reschedule - Upon a user tapping the {@link BVNotificationService#NEUTRAL}
     *     response, this time will reflect the client
     *     {@link BVNotificationData#getReviewRemindLaterDurationMillis()} to get schedule
     *     time</li>
     * </ul>
     *
     * @return Time in millis for the alarm to send a broadcast
     */
    private static long getTimeForAlarm(boolean initialSchedule, BVNotificationData notificationData) {
        long timeForAlarmMillis;
        long elapsedTimeMillis = SystemClock.elapsedRealtime();
        if (initialSchedule) {
            timeForAlarmMillis = elapsedTimeMillis + notificationData.getNotificationDelayMillis();
        } else {
            timeForAlarmMillis = elapsedTimeMillis + notificationData.getReviewRemindLaterDurationMillis();
        }
        return timeForAlarmMillis;
    }

    /**
     * @param timeForAlarmMillis {@link AlarmManager#ELAPSED_REALTIME} to display notification at
     * @param pendingIntent {@link PendingIntent} that contains metadata necessary to build the
     *                                           Notification
     */
    private static void setAlarm(Context appContext, long timeForAlarmMillis, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, timeForAlarmMillis, pendingIntent);
    }

    // endregion

    // region Actually Show Notification

    /**
     * Parses out the intent that tells us we need to show a notification.
     *
     * @param appContext Application context
     * @param intent Intent from the scheduled AlarmManager event
     */
    @WorkerThread
    static void onShowNotif(Context appContext, Intent intent) {
        int notificationId = BVNotificationService.getNotificationId(intent);
        BVNotificationDisplayData displayData = getDisplayData(intent);
        Bitmap bigPictureBitmap = getBigPictureBitmap(displayData.getImageUrl());
        String remoteCgcType = BVNotificationService.getRemoteCgcType(intent);
        String featureName = BVNotificationService.getFeatureName(intent);

        showNotification(appContext, remoteCgcType, featureName, displayData, bigPictureBitmap, notificationId, DEFAULT_NOTIF_VISIBILITY, R.drawable.ic_rate, DEFAULT_VIBRATE_PATTERN);
    }

    /**
     * @param bitmapUrl URL passed from feature describing the url to fetch the image from.
     * @return Small bitmap of the image, to display in the {@link Notification} object.
     */
    @Nullable @WorkerThread
    private static Bitmap getBigPictureBitmap(String bitmapUrl) {
        OkHttpClient okHttpClient = BVSDK.getInstance().getBvWorkerData().getOkHttpClient();
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        ImageLoader imageLoader = new BVImageLoader(okHttpClient, bvLogger);
        if (bitmapUrl == null) {
            return null;
        }
        return imageLoader.loadImage(bitmapUrl);
    }

    /**
     * Tell Android to immediately display a notification.
     *
     * @param appContext
     * @param displayData
     * @param bitmap
     * @param notificationId
     * @param visibility
     * @param smallIcon
     * @param vibratePattern
     */
    @WorkerThread
    private static void showNotification(Context appContext, String remoteCgcType, String featureName, BVNotificationDisplayData displayData, Bitmap bitmap, int notificationId, int visibility, @DrawableRes int smallIcon, long[] vibratePattern) {
        Notification notification = createNotification(appContext, remoteCgcType, featureName, displayData, bitmap, visibility, smallIcon, vibratePattern);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);
        notificationManager.notify(notificationId, notification);
    }

    /**
     * @param appContext
     * @param displayData
     * @param bitmap
     * @param visibility
     * @param smallIcon
     * @param vibratePattern
     * @return An Android Notification object
     */
    @WorkerThread
    private static Notification createNotification(Context appContext, String remoteCgcType, String featureName, BVNotificationDisplayData displayData, Bitmap bitmap, int visibility, @DrawableRes int smallIcon, long[] vibratePattern) {
        PendingIntent positiveButtonPendingIntent = createButtonIntent(appContext, BVNotificationService.POSITIVE, displayData.getCgcId(), remoteCgcType, featureName);
        PendingIntent neutralPendingIntent = createButtonIntent(appContext, BVNotificationService.NEUTRAL, displayData.getCgcId(), remoteCgcType, featureName);
        PendingIntent negativePendingIntent = createButtonIntent(appContext, BVNotificationService.NEGATIVE, displayData.getCgcId(), remoteCgcType, featureName);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext);
        if (bitmap != null) {
            final NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(builder)
                    .bigPicture(bitmap)
                    .setBigContentTitle(displayData.getTitleText())
                    .setSummaryText(displayData.getSummaryText());
            builder.setContentTitle(displayData.getTitleText())
                    .setStyle(style);
        }
        builder.setAutoCancel(true)
                .setVisibility(visibility)
                .setSmallIcon(smallIcon)
                .setContentIntent(positiveButtonPendingIntent)
                .addAction(0, displayData.getPositiveText(), positiveButtonPendingIntent)
                .addAction(0, displayData.getNeutralText(), neutralPendingIntent)
                .addAction(0, displayData.getNegativeText(), negativePendingIntent);

        if (displayData.isHeadsUpEnabled()) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH).setVibrate(vibratePattern);
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        return builder.build();
    }

    /**
     * @param appContext
     * @param extraButtonTapped
     * @param cgcId
     * @return An implicit intent that fires when a button is tapped on a notification
     */
    private static PendingIntent createButtonIntent(Context appContext, int extraButtonTapped, String cgcId, String analyticsCgcType, String featureName) {
        Intent intent = new Intent(ACTION_NOTIFICATION_BUTTON_TAPPED);
        intent.putExtra(EXTRA_BUTTON_TAPPED, extraButtonTapped);
        intent.putExtra(EXTRA_CGC_ID, cgcId);
        intent.putExtra(EXTRA_ANALYTICS_CGC_TYPE, analyticsCgcType);
        intent.putExtra(EXTRA_FEATURE_NAME, featureName);
        int pendingIntentId = extraButtonTapped;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, pendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    // endregion

    // region Get Remote Config

    @WorkerThread @Nullable
    static <NotificationDataType extends BVNotificationData> NotificationDataType getNotificationData(String remoteFeatureName, String remoteFileName, Class<NotificationDataType> notificationDataTypeClass) {
        BVLogger bvLogger = BVSDK.getInstance().getBvLogger();
        NotificationDataType notificationData = null;
        String pinConfigDataUrl = BVRemoteConfig.getFeatureConfigUrl(remoteFeatureName, remoteFileName);
        bvLogger.d(TAG, pinConfigDataUrl);
        Request request = new Request.Builder()
                .url(pinConfigDataUrl)
                .addHeader("User-Agent", BVSDK.getInstance().getBvWorkerData().getBvSdkUserAgent())
                .build();
        OkHttpClient okHttpClient = BVSDK.getInstance().getBvWorkerData().getOkHttpClient();
        Gson gson = BVSDK.getInstance().getBvWorkerData().getGson();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                bvLogger.e(TAG, "Unexpected code: " + response);
                return null;
            }
            notificationData = gson.fromJson(response.body().charStream(), notificationDataTypeClass);
        } catch (IOException e) {
            bvLogger.e(TAG, "Failed to get pin notification config", e);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
        return notificationData;
    }

    // endregion

    // region Manage Button Taps

    /**
     * When an {@link BVNotificationService#ACTION_NOTIFICATION_BUTTON_TAPPED} broadcast
     * is received, process it depending on which button is tapped.
     * <ul>
     *     <li>{@link BVNotificationService#POSITIVE} - Send response to user, then cancel the Notification</li>
     *     <li>{@link BVNotificationService#NEUTRAL} - Reschedule the notification by starting
     *     the {@link BVNotificationService}, and cancel the Notification</li>
     *     <li>{@link BVNotificationService#NEGATIVE} - Cancel the Notification</li>
     * </ul>
     *
     * @param context Application context
     * @param intent Intent that was sent with the broadcast
     */
    static void onNotifButtonTapBroadcast(Context context, Intent intent) {
        String cgcId = BVNotificationService.getNotificationCgcId(intent);
        int whichButton = BVNotificationService.getButtonTapped(intent);
        int notificationId = BVNotificationService.getNotificationId(intent);
        String remoteCgcType = BVNotificationService.getRemoteCgcType(intent);
        switch (whichButton) {
            case BVNotificationService.POSITIVE: {
                NotificationManagerCompat.from(context).cancel(notificationId);
                BVNotificationAnalyticsManager.sendNotificationEventForStoreReviewFeatureUsed(BVNotificationAnalyticsManager.NotificationAction.Positive.getKey(), cgcId, remoteCgcType);
                break;
            }
            case BVNotificationService.NEUTRAL: {
                NotificationManagerCompat.from(context).cancel(notificationId);
                BVNotificationAnalyticsManager.sendNotificationEventForStoreReviewFeatureUsed(BVNotificationAnalyticsManager.NotificationAction.Neutral.getKey(), cgcId, remoteCgcType);
                break;
            }
            case BVNotificationService.NEGATIVE: {
                NotificationManagerCompat.from(context).cancel(notificationId);
                BVNotificationAnalyticsManager.sendNotificationEventForStoreReviewFeatureUsed(BVNotificationAnalyticsManager.NotificationAction.Negative.getKey(), cgcId, remoteCgcType);
                break;
            }
        }
    }

    // endregion
}
