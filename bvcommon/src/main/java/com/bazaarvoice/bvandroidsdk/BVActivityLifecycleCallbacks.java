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
    private final BVPixel bvPixel;
    private final BVLogger bvLogger;

    private int activeActivityCount = 0;

    BVActivityLifecycleCallbacks(BVPixel bvPixel, BVLogger bvLogger) {
        this.bvPixel = bvPixel;
        this.bvLogger = bvLogger;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {
        if (activeActivityCount == 0) {
            // application resume
            bvLogger.v(TAG, "application resume");
            BVMobileAppLifecycleEvent event = new BVMobileAppLifecycleEvent(BVEventValues.AppState.RESUMED);
            bvPixel.track(event);
        }
        activeActivityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {
        activeActivityCount--;
        if (activeActivityCount == 0) {
            // application pause
            bvLogger.v(TAG, "application pause");
            BVMobileAppLifecycleEvent event = new BVMobileAppLifecycleEvent(BVEventValues.AppState.PAUSED);
            bvPixel.track(event);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
