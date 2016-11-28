/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BroadcastReceiver for receiving notifications to start building
 * notification metadata
 */
public class BVNotificationReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        // Forward to Service so that async work can finish
        Intent forwardIntent = new Intent(context, BVNotificationService.class);
        forwardIntent.setAction(intent.getAction());
        forwardIntent.putExtras(intent.getExtras());
        context.startService(forwardIntent);
    }

}