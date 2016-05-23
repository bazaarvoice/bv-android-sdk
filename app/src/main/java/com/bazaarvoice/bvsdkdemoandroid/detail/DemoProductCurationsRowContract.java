/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;

import java.util.List;

interface DemoProductCurationsRowContract {

    interface View {
        void showCurations(List<CurationsFeedItem> feedItems);
        void showLoadingCurations(boolean show);
        void showNoCurations();
        void showCurationsMessage(String message);
        void transitionToCurationsFeedItem(int index, List<CurationsFeedItem> curationsFeedItems);
    }

    interface UserActionsListener {
        void loadCurationsFeedItems(boolean forceRefresh);
        void onCurationsFeedItemTapped(CurationsFeedItem curationsFeedItem);
    }
}
