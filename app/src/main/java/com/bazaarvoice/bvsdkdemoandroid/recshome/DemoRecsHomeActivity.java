/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recshome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.ads.DemoAdContract;
import com.bazaarvoice.bvsdkdemoandroid.ads.DemoAdPresenter;
import com.bazaarvoice.bvsdkdemoandroid.cart.DemoCartActivity;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoFancyProductDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoProductRecContract;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoProductRecPresenter;
import com.bazaarvoice.bvsdkdemoandroid.settings.DemoSettingsActivity;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoLaunchIntentUtil;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeContentAd;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

import static com.bazaarvoice.bvsdkdemoandroid.utils.DemoRequiredKeyUiUtil.getNoReccosApiKeyDialog;

public class DemoRecsHomeActivity extends AppCompatActivity implements DemoRecsAdapter.RecTapListener, DemoProductRecContract.View, DemoAdContract.View {

    private static final int AUTO_CAROUSEL_DELAY = 8000;
    private static final String HOME_CLIENT_KEY = "home_screen_client";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rec_recycler_view) RecommendationsRecyclerView recyclerView;
    private DemoRecsAdapter adapter;
    private DemoProductRecContract.UserActionsListener recsUserActionListener;
    private DemoAdContract.UserActionsListener adUserActionListener;
    @BindView(R.id.no_recs_found) TextView noRecsTextView;
    @BindView(R.id.get_recs_progress) ProgressBar getRecsProgressBar;
    @BindView(R.id.header_pager) ViewPager headerViewPager;
    private DemoRecsHeaderPagerAdapter headerPagerAdapter;
    @BindView(R.id.indicator) CircleIndicator circleIndicator;
    @BindView(R.id.error_recs) TextView errorTextView;
    private String clientId;

    @Inject DemoClientConfigUtils demoClientConfigUtils;
    @Inject DemoMockDataUtil demoMockDataUtil;
    @Inject DemoClient demoClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recs_home);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        setupToolbar();
        setupHeaderViewPager();
        setupRecsViews();

        recsUserActionListener = new DemoProductRecPresenter(this, demoClient, demoMockDataUtil, true, recyclerView);
        AdLoader.Builder adLoaderBuilder = new AdLoader.Builder(this, demoMockDataUtil.getAdUnitId());
        adUserActionListener = new DemoAdPresenter(this, adLoaderBuilder, demoClient);

        swipeRefreshLayout.setOnRefreshListener(new RecRefreshListener(recsUserActionListener));
        clientId = savedInstanceState != null ? savedInstanceState.getString(HOME_CLIENT_KEY, "") : "";
    }


    @Override
    protected void onResume() {
        super.onResume();
        adUserActionListener.loadAd(false);
        boolean haveRecs = adapter.getRecommendationCount() > 0;
        String currentClient = demoClient.getClientId();
        boolean newClient = TextUtils.isEmpty(clientId);
        boolean clientChanged = !clientId.equals(currentClient);
        clientId = currentClient;
        if (!haveRecs || newClient || clientChanged) {
            recsUserActionListener.loadRecommendations(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(HOME_CLIENT_KEY, clientId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_actions, menu);

        if (!demoClientConfigUtils.otherSourcesExist()) {
            menu.findItem(R.id.settings_action).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private Toolbar.OnMenuItemClickListener onToolbarMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.settings_action:
                    DemoSettingsActivity.transitionTo(DemoRecsHomeActivity.this, DemoLaunchIntentUtil.LaunchCode.DEMO);
                    break;
                case R.id.cart_action:
                    DemoCartActivity.transitionTo(DemoRecsHomeActivity.this);
                    break;
            }
            return false;
        }
    };

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setOnMenuItemClickListener(onToolbarMenuItemClickListener);
    }

    private void setupHeaderViewPager() {
        headerPagerAdapter = new DemoRecsHeaderPagerAdapter(this);
        headerViewPager.setAdapter(headerPagerAdapter);
        circleIndicator.setViewPager(headerViewPager);

        headerViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DemoRecsHomeActivity.this.isFinishing()) {
                    return;
                }
                int currentIndex = headerViewPager.getCurrentItem();
                int nextIndex = (currentIndex+1) % headerPagerAdapter.getCount();
                headerViewPager.setCurrentItem(nextIndex);
                headerViewPager.postDelayed(this, AUTO_CAROUSEL_DELAY);
            }
        }, AUTO_CAROUSEL_DELAY);

    }

    private void setupRecsViews() {
        adapter = new DemoRecsAdapter(this);
        adapter.setRecTapListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    static class RecRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        DemoProductRecContract.UserActionsListener userActionsListener;

        RecRefreshListener(DemoProductRecContract.UserActionsListener userActionsListener) {
            this.userActionsListener = userActionsListener;
        }

        @Override
        public void onRefresh() {
            userActionsListener.loadRecommendations(true);
        }
    }

    @Override
    public void onProductTapped(BVProduct bvProduct) {
        DemoFancyProductDetailActivity.transitionTo(this, bvProduct.getId());
    }

    @Override
    public void showRecommendations(List<BVProduct> bvProducts) {
        recyclerView.setVisibility(View.VISIBLE);
        adapter.refreshProducts(bvProducts);
        getRecsProgressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        noRecsTextView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingRecs(boolean show) {
        getRecsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(show);
    }

    @Override
    public void showNoRecommendations() {
        adapter.refreshProducts(Collections.<BVProduct>emptyList());
        recyclerView.setVisibility(View.GONE);
        getRecsProgressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        noRecsTextView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        adapter.refreshProducts(Collections.<BVProduct>emptyList());
        recyclerView.setVisibility(View.GONE);
        getRecsProgressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        noRecsTextView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecMessage(String message) {
        Toast.makeText(DemoRecsHomeActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAd(NativeContentAd nativeContentAd) {
        adapter.refreshAd(nativeContentAd);
    }

    @Override
    public void transitionToAdInWebview(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent viewAdIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(viewAdIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showNoApiKey(String clientDisplayName) {
        getNoReccosApiKeyDialog(this, clientDisplayName).show();
    }

}
