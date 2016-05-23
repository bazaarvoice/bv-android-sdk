/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.recshome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.ads.DemoAdContract;
import com.bazaarvoice.bvsdkdemoandroid.ads.DemoAdPresenter;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoFancyProductDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoProductRecContract;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoProductRecPresenter;
import com.bazaarvoice.bvsdkdemoandroid.settings.DemoSettingsActivity;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeContentAd;

import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class DemoRecsHomeActivity extends AppCompatActivity implements DemoRecsAdapter.RecTapListener, DemoProductRecContract.View, DemoAdContract.View {

    private static final int AUTO_CAROUSEL_DELAY = 8000;

    private RecommendationsRecyclerView recyclerView;
    private DemoRecsAdapter adapter;
    private DemoProductRecContract.UserActionsListener recsUserActionListener;
    private DemoAdContract.UserActionsListener adUserActionListener;
    private TextView noRecsTextView;
    private ProgressBar getRecsProgressBar;
    private ViewPager headerViewPager;
    private DemoRecsHeaderPagerAdapter headerPagerAdapter;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        recsUserActionListener = new DemoProductRecPresenter(this, demoConfigUtils, demoDataUtil, true);
        AdLoader.Builder adLoaderBuilder = new AdLoader.Builder(this, "/5705/bv-incubator/IncubatorEnduranceCycles");
        adUserActionListener = new DemoAdPresenter(this, demoConfigUtils, demoDataUtil, adLoaderBuilder);

        setContentView(R.layout.activity_recs_home);
        setupToolbar();
        setupHeaderViewPager();
        setupRecsViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        adUserActionListener.loadAd(false);
        recsUserActionListener.loadRecommendations(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (DemoConfigUtils.getInstance(this).configFileExists()) {
            getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private Toolbar.OnMenuItemClickListener onToolbarMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.settings_action:
                    DemoSettingsActivity.transitionTo(DemoRecsHomeActivity.this);
                    break;
            }
            return false;
        }
    };

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setOnMenuItemClickListener(onToolbarMenuItemClickListener);
    }

    private void setupHeaderViewPager() {
        headerViewPager = (ViewPager) findViewById(R.id.header_pager);
        headerPagerAdapter = new DemoRecsHeaderPagerAdapter(getSupportFragmentManager());
        headerViewPager.setAdapter(headerPagerAdapter);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
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
        recyclerView = (RecommendationsRecyclerView) findViewById(R.id.rec_recycler_view);
        adapter = new DemoRecsAdapter();
        adapter.setRecTapListener(this);
        noRecsTextView = (TextView) findViewById(R.id.no_recs_found);
        getRecsProgressBar = (ProgressBar) findViewById(R.id.get_recs_progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onProductTapped(BVProduct bvProduct) {
        DemoFancyProductDetailActivity.transitionTo(this, bvProduct.getProductId());
    }

    @Override
    public void showRecommendations(List<BVProduct> bvProducts) {
        adapter.refreshProducts(bvProducts);
        getRecsProgressBar.setVisibility(View.GONE);
        noRecsTextView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingRecs(boolean show) {
        getRecsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoRecommendations() {
        adapter.refreshProducts(Collections.<BVProduct>emptyList());
        getRecsProgressBar.setVisibility(View.GONE);
        noRecsTextView.setVisibility(View.VISIBLE);
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

}
