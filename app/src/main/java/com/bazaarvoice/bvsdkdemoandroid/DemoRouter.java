/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvsdkdemoandroid.curations.detail.DemoCurationsDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.map.DemoCurationsMapsActivity;

public class DemoRouter {

    private static void transitionToActivityWithExtras(Context fromContext, Class toActivityClass, @Nullable Bundle extras) {
        Intent intent = new Intent(fromContext, toActivityClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        fromContext.startActivity(intent);
    }

    private static void transitionToActivity(Context fromContext, Class toActivityClass) {
        transitionToActivityWithExtras(fromContext, toActivityClass, null);
    }

    public static void transitionToCurationsMapView(Context fromContext, String productId) {
        Bundle bundle = new Bundle();
        bundle.putString(DemoCurationsMapsActivity.EXTRA_PRODUCT_ID, productId);
        transitionToActivityWithExtras(fromContext, DemoCurationsMapsActivity.class, bundle);
    }

    public static void transitionToCurationsFeedItem(Context fromContext, String productId, String startFeedItemId) {
        Bundle extras = new Bundle();
        extras.putString(DemoCurationsDetailActivity.KEY_PRODUCT_ITEM, productId);
        extras.putString(DemoCurationsDetailActivity.KEY_CURATIONS_ITEM_ID, startFeedItemId);
        transitionToActivityWithExtras(fromContext, DemoCurationsDetailActivity.class, extras);
    }

}
