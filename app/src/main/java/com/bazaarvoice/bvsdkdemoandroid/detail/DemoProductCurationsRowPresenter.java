/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import com.bazaarvoice.bvandroidsdk.BVCurations;
import com.bazaarvoice.bvandroidsdk.CurationsFeedCallback;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedRequest;
import com.bazaarvoice.bvandroidsdk.CurationsMedia;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfig;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;

import java.util.Collections;
import java.util.List;

class DemoProductCurationsRowPresenter implements DemoProductCurationsRowContract.UserActionsListener, CurationsFeedCallback {

    private DemoProductCurationsRowContract.View view;
    private DemoConfigUtils demoConfigUtils;
    private DemoDataUtil demoDataUtil;
    private boolean fetched = false;
    private List<CurationsFeedItem> curationsFeedItems = Collections.emptyList();
    private String productId;

    public DemoProductCurationsRowPresenter(DemoProductCurationsRowContract.View view, DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil, String productId) {
        this.view = view;
        this.demoConfigUtils = demoConfigUtils;
        this.demoDataUtil = demoDataUtil;
        this.productId = productId;
    }

    @Override
    public void loadCurationsFeedItems(boolean forceRefresh) {
        fetched = false;
        DemoConfig currentConfig = demoConfigUtils.getCurrentConfig();
        List<CurationsFeedItem> demoFeedItems = demoDataUtil.getCurationsFeedItems();
        if (currentConfig.isDemoClient()) {
            showCurationsFeedItems(demoFeedItems);
            return;
        }

        if (!curationsFeedItems.isEmpty()) {
            showCurationsFeedItems(curationsFeedItems);
        }

        view.showLoadingCurations(true);
        BVCurations bvCurations = new BVCurations();
        CurationsFeedRequest request = new CurationsFeedRequest.Builder(DemoConstants.CURATIONS_GROUPS)
                .limit(20)
                .media(new CurationsMedia("photo", DemoUtils.MAX_IMAGE_WIDTH/2, DemoUtils.MAX_IMAGE_HEIGHT/2))
                .externalId(productId)
                .withProductData(true)
                .build();
        bvCurations.getCurationsFeedItems(request, this);
    }

    @Override
    public void onCurationsFeedItemTapped(CurationsFeedItem curationsFeedItem) {
        if (fetched) {
            for (int i=0; i<curationsFeedItems.size(); i++) {
                CurationsFeedItem currItem = curationsFeedItems.get(i);
                if (curationsFeedItem.getId().equals(currItem.getId())) {
                    view.transitionToCurationsFeedItem(i, curationsFeedItems);
                    return;
                }
            }
        }
    }

    @Override
    public void onSuccess(List<CurationsFeedItem> feedItems) {
        showCurationsFeedItems(feedItems);
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        view.showCurationsMessage("Failed to get curations feed");
        showCurationsFeedItems(Collections.<CurationsFeedItem>emptyList());
    }

    private void showCurationsFeedItems(List<CurationsFeedItem> feedItems) {
        curationsFeedItems = feedItems;
        fetched = true;
        view.showLoadingCurations(false);

        if (feedItems.size() > 0) {
            view.showCurations(feedItems);
        } else {
            view.showNoCurations();
        }
    }
}
