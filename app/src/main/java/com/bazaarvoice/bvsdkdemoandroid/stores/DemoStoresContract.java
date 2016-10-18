/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.stores;

import com.bazaarvoice.bvandroidsdk.Store;

import java.util.List;

public interface DemoStoresContract {
    interface View {
        void showStores(List<Store> stores);
        void showNoStoresFound();
        void showSwipeRefreshLoading(boolean isLoading);
        void showLoading(boolean isLoading);
        void showMessage(String message);
        void showNotConfiguredDialog(String displayName);
    }

    interface UserActionsListener {
        void onStoreTapped(Store store);
        void loadStores(boolean forceRefresh);
        void onSwipeRefresh();
    }
}