/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.curations.detail;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;

import java.util.List;

interface DemoCurationsDetailContract {

    interface View {
        void showCurationsFeed(List<CurationsFeedItem> curationsFeedItems);
        void showCurationsFeedItemInPager(int index);
        void showNoCurations();
        void showLoadingCurations(boolean isLoading);
        void showCurationsMessage(String message);
    }

    interface UserActionsListener {
        void loadCurationsFeed();
    }

}
