/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.CurationsPostCallback;
import com.bazaarvoice.bvandroidsdk.CurationsPostResponse;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.ads.AdsFragment;
import com.bazaarvoice.bvsdkdemoandroid.ads.BannerAdActivity;
import com.bazaarvoice.bvsdkdemoandroid.ads.InterstitialAdActivity;
import com.bazaarvoice.bvsdkdemoandroid.ads.NativeAdActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.BrowseProductsFragment;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.ProductsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.kitchensink.DemoConversationDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.kitchensink.DemoConversationsFragment;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoCurationsFragment;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoCurationsPostActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.feed.DemoCurationsFeedActivity;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoRecommendationsFragment;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.detail.DemoProductDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.settings.DemoSettingsActivity;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;

public class DemoMainActivity extends AppCompatActivity implements CurationsPostCallback {
    @IdRes
    private int fragContainerId;

    private DrawerLayout mDrawerLayout;

    private MenuItem prevItem;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onToolbarMenuItemClickListener);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        fragContainerId = R.id.main_content;

        if (savedInstanceState == null) {
            transitionTo(DemoHomePageFragment.newInstance());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
                    DemoSettingsActivity.transitionTo(DemoMainActivity.this);
                    break;
            }
            return false;
        }
    };

    private void setupDrawerContent(final NavigationView navigationView) {

        MenuItem item = navigationView.getMenu().getItem(0);
        item.setChecked(true);
        prevItem = item;

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                prevItem.setChecked(false);
                prevItem = item;

                switch (item.getItemId()) {
                    case R.id.home_page:
                        toolbar.setTitle("Welcome");
                        transitionTo(DemoHomePageFragment.newInstance());
                        break;
                    case R.id.recommendations:
                        toolbar.setTitle(getString(R.string.demo_recommendations));
                        transitionTo(DemoRecommendationsFragment.newInstance());
                        break;
                    case R.id.advertising:
                        toolbar.setTitle("Advertising");
                        transitionTo(AdsFragment.newInstance());
                        break;
                    case R.id.conversations:
                        toolbar.setTitle(getString(R.string.demo_conversations) + ": Kitchen Sink");
                        transitionTo(DemoConversationsFragment.newInstance());
                        break;
                    case R.id.browse_products:
                        toolbar.setTitle(getString(R.string.demo_conversations) + ": Browse");
                        transitionTo(BrowseProductsFragment.getInstance());
                        break;
                    case R.id.curations:
                        toolbar.setTitle(getString(R.string.demo_curations));
                        transitionTo(DemoCurationsFragment.newInstance());
                        break;
                }

                // Close the navigation drawer when an item is selected.
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void transitionTo(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragContainerId, fragment);
        transaction.commit();
    }

    public void transitionToBvProductDetail(BVProduct bvProduct) {
        DemoProductDetailActivity.start(this, bvProduct);
    }

    public void transitionToBVConversationDetail(String url, String response) {
        Intent intent = new Intent(this, DemoConversationDetailActivity.class);
        intent.putExtra(DemoConversationDetailActivity.URL_EXTRA_KEY, url);
        intent.putExtra(DemoConversationDetailActivity.RESPONSE_EXTRA_KEY, response);
        startActivity(intent);
    }

    public void transitionToNativeAd() {
        Intent intent = new Intent(this, NativeAdActivity.class);
        startActivity(intent);
    }

    public void transitionToInterstitialAd(){
        Intent intent = new Intent(this, InterstitialAdActivity.class);
        startActivity(intent);
    }

    public void transitionToBannerAd(){
        Intent intent = new Intent(this, BannerAdActivity.class);
        startActivity(intent);
    }

    public void transitionToProductSearch(String query){
        Intent intent = new Intent(this, ProductsActivity.class);
        intent.putExtra("searchTerm", query);
        startActivity(intent);
    }

    public void transitionToCurationsFeed(){
        Intent intent = new Intent(this, DemoCurationsFeedActivity.class);
        startActivity(intent);
    }

    public void transitionToCurationsPost(){
        Intent intent = new Intent(this, DemoCurationsPostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(CurationsPostResponse response) {
        Log.d("success", response.getRemoteUrl());
    }

    @Override
    public void onFailure(Throwable throwable) {
        Log.d("asd", throwable.getMessage());
    }
}

