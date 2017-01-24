/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.productstats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.ConversationsDisplayRecyclerView;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoProductStatsActivity extends AppCompatActivity implements DemoProductStatsContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private DemoProductStatsContract.UserActionsListener bulkRatingsUserActionListener;

    @BindView(R.id.product_image) ImageView productImage;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_rating) RatingBar productRating;
    private RecyclerView productStatsRecyclerView;
    private DemoProductStatsAdapter productStatsAdapter;
    @BindView(R.id.reviews_loading) ProgressBar reviewsLoading;
    @BindView(R.id.recyclerViewStub)
    ViewStub recyclerViewStub;

    @Inject DemoConfigUtils demoConfigUtils;
    @Inject DemoDataUtil demoDataUtil;

    private String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_reviews);
        ButterKnife.bind(this);
        DemoApp.get(this).getAppComponent().inject(this);
        inflateRecyclerView();
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        setupToolbarViews();
        setupHeaderViews();
        setupRecyclerView();

        bulkRatingsUserActionListener = new DemoProductStatsPresenter(this, demoConfigUtils, demoDataUtil, productId);
    }

    void inflateRecyclerView() {
        recyclerViewStub.setLayoutResource(R.layout.store_reviews_recyclerview);
        recyclerViewStub.inflate();
        productStatsRecyclerView = (ConversationsDisplayRecyclerView) findViewById(R.id.store_reviews_recycler_view);
    }

    private void setupToolbarViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupHeaderViews() {
        productImage.setVisibility(View.INVISIBLE);
        productName.setText("Testing product id: " + productId);
        productRating.setVisibility(View.INVISIBLE);
    }

    private void setupRecyclerView() {
        productStatsAdapter = new DemoProductStatsAdapter();
        productStatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        productStatsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        productStatsRecyclerView.setAdapter(productStatsAdapter);
        productStatsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bulkRatingsUserActionListener.loadProductStats();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showProductStats(List<Product> bazaarStatistics) {
        productStatsAdapter.refreshReviews(bazaarStatistics);
    }

    @Override
    public void showLoadingProductStats(boolean show) {
        reviewsLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoProductStats() {

    }

    @Override
    public void showProductStatsMessage(String message) {
        Toast.makeText(DemoProductStatsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void transitionToProductStats() {
        // no-op
    }

    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoProductStatsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }
}
