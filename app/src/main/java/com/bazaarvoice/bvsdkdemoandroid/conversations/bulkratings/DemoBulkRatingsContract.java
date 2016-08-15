/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.bulkratings;

import com.bazaarvoice.bvandroidsdk.Statistics;

import java.util.List;

public interface DemoBulkRatingsContract {

    interface View {
        void showRatings(List<Statistics> bazaarStatistics);
        void showLoadingRatings(boolean show);
        void showNoRatings();
        void showRatingsMessage(String message);
        void transitionToRatings();
    }

    interface UserActionsListener {
        void loadRatings();
    }

}
