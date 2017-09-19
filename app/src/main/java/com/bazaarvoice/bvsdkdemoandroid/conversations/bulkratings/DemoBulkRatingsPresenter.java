/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.bulkratings;

import android.support.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BulkRatingOptions;
import com.bazaarvoice.bvandroidsdk.BulkRatingsRequest;
import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.Statistics;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemoBulkRatingsPresenter implements DemoBulkRatingsContract.UserActionsListener {

    private DemoBulkRatingsContract.View view;
    private DemoClientConfigUtils demoClientConfigUtils;
    private DemoMockDataUtil demoMockDataUtil;
    private ArrayList<String> bulkProductIds = new ArrayList<String>();
    private final BVConversationsClient client;

    public DemoBulkRatingsPresenter(DemoBulkRatingsContract.View view, BVConversationsClient bvConversationsClient, DemoClientConfigUtils demoClientConfigUtils, DemoMockDataUtil demoMockDataUtil, ArrayList<String> bulkProductIds) {
        this.view = view;
        this.client = bvConversationsClient;
        this.demoClientConfigUtils = demoClientConfigUtils;
        this.demoMockDataUtil = demoMockDataUtil;
        this.bulkProductIds = bulkProductIds;
    }

    @Override
    public void loadRatings() {
        final BulkRatingsRequest request = new BulkRatingsRequest.Builder(bulkProductIds, BulkRatingOptions.StatsType.All)
                .build();
        client.prepareCall(request).loadAsync(new ConversationsDisplayCallback<BulkRatingsResponse>() {
            @Override
            public void onSuccess(@NonNull BulkRatingsResponse response) {
                showRatings(response.getResults());
            }

            @Override
            public void onFailure(@NonNull ConversationsException exception) {
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
