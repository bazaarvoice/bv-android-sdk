/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.stores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.Store;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoStoresActivity extends AppCompatActivity implements DemoStoresContract.View {
    private static final String EXTRA_PRODUCT_IDS = "extra_product_ids";
    private static final String EXTRA_LIMIT = "extra_limit";
    private static final String EXTRA_OFFSET = "extra_offset";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stores_recyclerview)
    RecyclerView storesRecyclerView;
    @BindView(R.id.get_stores_progress) ProgressBar storesProgress;
    @BindView(R.id.no_stores_found) TextView noStoresFoundTv;

    @Inject BVConversationsClient client;

    private DemoStoresContract.UserActionsListener userActionsListener;
    private DemoStoreAdapter demoStoreAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DemoApp.getAppComponent(this).inject(this);
        LayoutInflater.from(this).inflate(R.layout.activity_stores_feed, (FrameLayout) findViewById(R.id.main_content), true);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_PRODUCT_IDS)) {
            List<String> productIds = getIntent().getExtras().getStringArrayList(EXTRA_PRODUCT_IDS);
            userActionsListener = new DemoStoresPresenter(this, client, productIds);
        } else if (extras.containsKey(EXTRA_LIMIT) && extras.containsKey(EXTRA_OFFSET)) {
            int limit = extras.getInt(EXTRA_LIMIT);
            int offset = extras.getInt(EXTRA_OFFSET);
            userActionsListener = new DemoStoresPresenter(this, client, limit, offset);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userActionsListener.onSwipeRefresh();
            }
        });
        demoStoreAdapter = new DemoStoreAdapter();
        demoStoreAdapter.setOnItemClickListener(new DemoStoreAdapter.OnBvProductClickListener() {
            @Override
            public void onStoreClickListener(Store store, View row) {
                userActionsListener.onStoreTapped(store);
            }
        });
        storesRecyclerView.setAdapter(demoStoreAdapter);
        storesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        storesRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userActionsListener.loadStores(false);
    }

    @Override
    public void showStores(List<Store> stores) {
        demoStoreAdapter.setStores(stores);
        noStoresFoundTv.setVisibility(View.GONE);
        storesProgress.setVisibility(View.GONE);
    }

    @Override
    public void showNoStoresFound() {
        demoStoreAdapter.setStores(Collections.<Store>emptyList());
        noStoresFoundTv.setVisibility(View.VISIBLE);
        storesProgress.setVisibility(View.GONE);
    }

    @Override
    public void showSwipeRefreshLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showLoading(boolean isLoading) {
        storesProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotConfiguredDialog(String displayName) {
        Toast.makeText(this, "not configured " + displayName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void transitionToWithProductIds(Activity fromActivity, ArrayList<String> productIds) {
        Intent intent = new Intent(fromActivity, DemoStoresActivity.class);
        intent.putStringArrayListExtra(EXTRA_PRODUCT_IDS, productIds);
        fromActivity.startActivity(intent);
    }

    public static void transitionToWithLimitAndOffset(Activity fromActivity, int limit, int offset) {
        Intent intent = new Intent(fromActivity, DemoStoresActivity.class);
        intent.putExtra(EXTRA_LIMIT, limit);
        intent.putExtra(EXTRA_OFFSET, offset);
        fromActivity.startActivity(intent);
    }
}
