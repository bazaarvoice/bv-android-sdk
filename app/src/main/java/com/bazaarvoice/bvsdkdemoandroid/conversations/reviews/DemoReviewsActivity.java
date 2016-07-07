/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;

import java.util.List;

public class DemoReviewsActivity extends AppCompatActivity implements DemoReviewsContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private BVProduct bvProduct;
    private DemoReviewsContract.UserActionsListener reviewsUserActionListener;

    private CardView productRowHeader;
    private ImageView productImageView;
    private TextView productName;
    private RatingBar productRating;

    private RecyclerView reviewsRecyclerView;
    private DemoReviewsAdapter reviewsAdapter;
    private ProgressBar reviewsLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_reviews);
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        bvProduct = DemoProductsCache.getInstance().getDataItem(productId);

        setupToolbarViews();
        setupHeaderViews();
        setupRecyclerView();

        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        reviewsUserActionListener = new DemoReviewsPresenter(this, demoConfigUtils, demoDataUtil, productId);
    }

    private void setupToolbarViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupHeaderViews() {
        productRowHeader = (CardView) findViewById(R.id.product_row_header);
        productImageView = (ImageView) productRowHeader.findViewById(R.id.product_image);
        productName = (TextView) productRowHeader.findViewById(R.id.product_name);
        productRating = (RatingBar) productRowHeader.findViewById(R.id.product_rating);

        DemoUtils demoUtils = DemoUtils.getInstance(productImageView.getContext());
        demoUtils.picassoThumbnailLoader()
                .load(bvProduct.getImageUrl())
                .resizeDimen(R.dimen.side_not_set, R.dimen.clip_half_image_side)
                .into(productImageView);
        productName.setText(bvProduct.getProductName());
        productRating.setRating(bvProduct.getAverageRating());
    }

    private void setupRecyclerView() {
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        reviewsAdapter = new DemoReviewsAdapter();
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        reviewsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
        reviewsLoading = (ProgressBar) findViewById(R.id.reviews_loading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reviewsUserActionListener.loadReviews(false);
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
    public void showReviews(List<Review> bazaarReviews) {
        reviewsAdapter.refreshReviews(bazaarReviews);
    }

    @Override
    public void showLoadingReviews(boolean show) {
        reviewsLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoReviews() {

    }

    @Override
    public void showReviewsMessage(String message) {
        Toast.makeText(DemoReviewsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void transitionToReviews() {
        // no-op
    }

    @Override
    public void showSubmitReviewDialog() {
        // maybe TODO
    }

    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoReviewsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }
}
