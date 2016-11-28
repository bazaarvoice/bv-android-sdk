/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Internal SDK delegate for listening to Activity Lifecycle events
 */
class BVActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = BVActivityLifecycleCallbacks.class.getSimpleName();
    private AnalyticsManager analyticsManager;

    private int activeActivityCount = 0;

    public BVActivityLifecycleCallbacks(AnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override public void onActivityStarted(Activity activity) {
        if (activeActivityCount == 0) {
            // application resume
            Logger.v(TAG, "application resume");
            analyticsManager.enqueueAppStateEvent(MobileAppLifecycleSchema.AppState.RESUMED);
        }
        activeActivityCount++;
    }

    @Override public void onActivityResumed(Activity activity) {

    }

    @Override public void onActivityPaused(Activity activity) {

    }

    @Override public void onActivityStopped(Activity activity) {
        activeActivityCount--;
        if (activeActivityCount == 0) {
            // application pause
            Logger.v(TAG, "application pause");
            analyticsManager.enqueueAppStateEvent(MobileAppLifecycleSchema.AppState.PAUSED);
        }
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override public void onActivityDestroyed(Activity activity) {

    }
}
