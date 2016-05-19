/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.example.bazaarvoice.bv_android_sdk.curations;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Describe file here.
 */
public class DemoCurationsCache {

    private static long timeMillisAdded = 0l;
    private static List<CurationsFeedItem> feedItems = Collections.emptyList();

    public static void putBvProducts(List<CurationsFeedItem> feedItems) {
        timeMillisAdded = System.currentTimeMillis();
        feedItems = feedItems;
    }

    public static List<CurationsFeedItem> getFeedItems() {
        checkValid();
        return feedItems;
    }

    private static void checkValid() {
        long tenMinutesInMillis = TimeUnit.MINUTES.toMillis(10);
        long tenMinutesAgoInMillis = System.currentTimeMillis() - tenMinutesInMillis;
        boolean olderThan10Minutes = timeMillisAdded < tenMinutesAgoInMillis;
        if (olderThan10Minutes) {
            feedItems = Collections.emptyList();
        }
    }
}