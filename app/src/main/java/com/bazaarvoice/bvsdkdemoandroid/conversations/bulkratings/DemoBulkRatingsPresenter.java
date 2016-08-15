/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.bulkratings;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.BulkRatingOptions;
import com.bazaarvoice.bvandroidsdk.BulkRatingsRequest;
import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.Statistics;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemoBulkRatingsPresenter implements DemoBulkRatingsContract.UserActionsListener {

    private DemoBulkRatingsContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private ArrayList<String> bulkProductIds = new ArrayList<String>();
    private final BVConversationsClient client = new BVConversationsClient();

    public DemoBulkRatingsPresenter(DemoBulkRatingsContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, ArrayList<String> bulkProductIds) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.bulkProductIds = bulkProductIds;
    }

    @Override
    public void loadRatings() {

        BulkRatingsRequest request = new BulkRatingsRequest.Builder(bulkProductIds, BulkRatingOptions.StatsType.NativeReviews)
                .build();

        client.prepareCall(request).loadAsync(new ConversationsCallback<BulkRatingsResponse>() {
            @Override
            public void onSuccess(BulkRatingsResponse response) {
                //called on Main Thread
                response.getResults(); //contains Statistics
                showRatings(response.getResults());
            }

            @Override
            public void onFailure(BazaarException exception) {
                //called on Main Thread
                exception.printStackTrace();
                showRatings(Collections.<Statistics>emptyList());
            }
        });

    }


    private void showRatings(List<Statistics> bazaarStats) {

        view.showLoadingRatings(false);

        if (bazaarStats.size() > 0) {
            view.showRatings(bazaarStats);
        } else {
            view.showNoRatings();
        }
    }
}
