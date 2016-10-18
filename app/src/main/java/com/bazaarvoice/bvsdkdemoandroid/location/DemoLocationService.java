/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.BVLocationManager;
import com.bazaarvoice.bvandroidsdk.BVVisit;

public class DemoLocationService extends Service {
    public static final String ACTION_VISIT_START = "com.bazaarvoice.bvsdkdemoandroid.action.VISIT_START";
    public static final String ACTION_VISIT_END = "com.bazaarvoice.bvsdkdemoandroid.action.VISIT_END";
    public static final String ACTION_RATE_STORE = "com.bazaarvoice.bvsdkdemoandroid.action.RATE_STORE";

    public static final String EXTRA_STORE_ID = "extra_store_id";

    private BVLocationManager.BVLocationListener bvLocationListener = new BVLocationManager.BVLocationListener() {
        @Override
        public void didBeginVisit(BVVisit visit) {
            Intent intent = new Intent(ACTION_VISIT_START);
            intent.putExtra(EXTRA_STORE_ID, visit.getStoreId());
            sendBroadcast(intent);
        }

        @Override
        public void didEndVisit(BVVisit visit) {
            Intent intent = new Intent(ACTION_VISIT_END);
            intent.putExtra(EXTRA_STORE_ID, visit.getStoreId());
            sendBroadcast(intent);
        }
    };

    private BVLocationManager.BVRateStoreListener rateStoreListener = new BVLocationManager.BVRateStoreListener() {
        @Override
        public void onRateStoreClicked(String storeId) {
            Intent intent = new Intent(ACTION_RATE_STORE);
            intent.putExtra(EXTRA_STORE_ID, storeId);
            sendBroadcast(intent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        BVLocationManager.getInstance().addLocationVisitListener(bvLocationListener);
        BVLocationManager.getInstance().addRateStoreListener(rateStoreListener);
        BVLocationManager.getInstance().startMonitoringLocation();
    }

    @Override
    public void onDestroy() {
        BVLocationManager.getInstance().stopMonitoringLocation();
        BVLocationManager.getInstance().removeRateStoreListener(rateStoreListener);
        BVLocationManager.getInstance().removeLocationVisitListener(bvLocationListener);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
