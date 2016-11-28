package com.bazaarvoice.bvandroidsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PinNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Forward to Service so that async work can finish
        Intent forwardIntent = new Intent(context, PinNotificationService.class);
        forwardIntent.setAction(intent.getAction());
        forwardIntent.putExtras(intent.getExtras());
        context.startService(forwardIntent);
    }

}
