/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.stores;

import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.BulkStoreRequest;
import com.bazaarvoice.bvandroidsdk.BulkStoreResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DemoStoresPresenter implements DemoStoresContract.UserActionsListener {
    private static final String TAG = DemoStoresPresenter.class.getSimpleName();

    private boolean fetchByStoreIds;
    private List<String> storeIds = new ArrayList<>();
    private int limit, offset;

    private DemoStoresContract.View view;

    public DemoStoresPresenter(DemoStoresContract.View view, List<String> storeIds) {
        this.view = view;
        this.storeIds = storeIds;
        fetchByStoreIds = true;
    }

    public DemoStoresPresenter(DemoStoresContract.View view, int limit, int offset) {
        this.view = view;
        this.limit = limit;
        this.offset = offset;
        fetchByStoreIds = false;
    }

    @Override
    public void onStoreTapped(Store store) {
        view.showMessage("store " + store.getDisplayName() + " tapped");
    }

    @Override
    public void loadStores(boolean forceRefresh) {
        List<Store> cachedStores = getCachedStores();
        boolean haveLocalCache = cachedStores!=null && !cachedStores.isEmpty();
        boolean shouldHitNetwork = forceRefresh || !haveLocalCache;

        if (!shouldHitNetwork) {
            showStores(cachedStores);
            return;
        }

        view.showLoading(true);
        BulkStoreRequest storesRequest = getBulkStoreRequest();
        loadStores(storesRequest);
    }

    private List<Store> getCachedStores() {
        if (fetchByStoreIds) {
            return DemoStoreCache.getInstance().getDataItem(DemoStoreCache.generateKeyForStoresList(storeIds));
        } else {
            return new ArrayList<>();
        }
    }

    private BulkStoreRequest getBulkStoreRequest() {
        if (fetchByStoreIds) {
            return new BulkStoreRequest.Builder(storeIds).build();
        } else {
            return new BulkStoreRequest.Builder(limit, offset).build();
        }
    }

    private void loadStores(BulkStoreRequest bulkStoreRequest) {
        BVConversationsClient client = new BVConversationsClient();
        client.prepareCall(bulkStoreRequest).loadAsync(new ConversationsCallback<BulkStoreResponse>() {
            @Override
            public void onSuccess(BulkStoreResponse response) {
                List<Store> stores = response.getResults(); //contains stores
                showStores(stores);
            }

            @Override
            public void onFailure(BazaarException exception) {
                //called on Main Thread
                Log.e(TAG, "Failed to get stores", exception);
                view.showMessage("Failed to get stores");
                showStores(Collections.<Store>emptyList());
            }
        });
    }

    @Override
    public void onSwipeRefresh() {
        view.showSwipeRefreshLoading(true);
        loadStores(true);
    }

    private void showStores(List<Store> stores) {
        view.showSwipeRefreshLoading(false);
        view.showLoading(false);
        if (fetchByStoreIds) {
            DemoStoreCache.getInstance().putDataItem(DemoStoreCache.generateKeyForStoresList(storeIds), stores);
        }

        if (stores.size() > 0) {
            view.showStores(stores);
        } else {
            view.showNoStoresFound();
        }
    }
}
