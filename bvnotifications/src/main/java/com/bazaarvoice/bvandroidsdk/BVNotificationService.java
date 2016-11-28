/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.internal.Utils;
import com.google.gson.Gson;

/**
 * Service for building notification metadata
 */
public class BVNotificationService extends IntentService {

    // region Properties

    public static final int POSITIVE = 0;
    public static final int NEUTRAL = 1;
    public static final int NEGATIVE = 2;
    private static final String SERVICE_NAME = Utils.THREAD_PREFIX + "BVNotificationService";
    static final String ACTION_SHOW_NOTIF = "com.bazaarvoice.bvandroidsdk.action.SHOW_NOTIF";
    public static final String ACTION_NOTIFICATION_BUTTON_TAPPED = "com.bazaarvoice.bvandroidsdk.action.NOTIFICATION_BUTTON_TAPPED";
    static final String EXTRA_BUTTON_TAPPED = "extra_button_tapped";
    static final String EXTRA_CGC_ID = "extra_cgc_id";
    static final String EXTRA_FEATURE_NAME = "extra_feature_name";
    static final String EXTRA_DISPLAY_DATA = "extra_bv_notification_data_str";
    static final String EXTRA_ANALYTICS_CGC_TYPE = "extra_remote_cgc_type";

    // endregion

    // region Constructor

    public BVNotificationService() {
        super(SERVICE_NAME);
    }

    // endregion

    // region Implementation

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String inputAction = intent.getAction();
        switch (inputAction) {
            case ACTION_SHOW_NOTIF: {
                BVNotificationUtil.onShowNotif(getApplicationContext(), intent);
                break;
            }
            case ACTION_NOTIFICATION_BUTTON_TAPPED: {
                BVNotificationUtil.onNotifButtonTapBroadcast(getApplicationContext(), intent);
                break;
            }
        }
    }

    // endregion

    // region Intent Parsing Helpers

    static int getNotificationId(Intent intent) {
        String cgcId = getNotificationCgcId(intent);
        int cgcIdInt = 0;
        try {
            cgcIdInt = Integer.parseInt(cgcId);
        } catch (NumberFormatException e) {
            cgcIdInt = 0;
        }
        return cgcIdInt;
    }

    public static String getNotificationCgcId(Intent intent) {
        return intent.getStringExtra(EXTRA_CGC_ID);
    }

    public static int getButtonTapped(Intent intent) {
        return intent.getIntExtra(BVNotificationService.EXTRA_BUTTON_TAPPED, -1);
    }

    public static String getFeatureName(Intent intent) {
        return intent.getStringExtra(EXTRA_FEATURE_NAME);
    }

    static BVNotificationDisplayData getDisplayData(Intent intent) {
        String displayDataStr = intent.getStringExtra(EXTRA_DISPLAY_DATA);
        Gson gson = BVSDK.getInstance().getGson();
        return gson.fromJson(displayDataStr, BVNotificationDisplayData.class);
    }

    static String getRemoteCgcType(Intent intent) {
        return intent.getStringExtra(EXTRA_ANALYTICS_CGC_TYPE);
    }

    // endregion

}
