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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.Review;
import com.bazaarvoice.bvandroidsdk.ReviewsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoReviewsActivity extends AppCompatActivity implements DemoReviewsContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String FORCE_LOAD_API = "extra_force_api_load";

    private BVProduct bvProduct;
    private DemoReviewsContract.UserActionsListener reviewsUserActionListener;

    @BindView(R.id.product_row_header) CardView productRowHeader;
    @BindView(R.id.product_image) ImageView productImageView;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_rating) RatingBar productRating;

    @BindView(R.id.reviews_recycler_view) ReviewsRecyclerView reviewsRecyclerView;
    @BindView(R.id.reviews_loading) ProgressBar reviewsLoading;
    private DemoReviewsAdapter reviewsAdapter;

    private boolean forceLoadFromProductId; // Meaning, a BVProduct is explicitly not provided
    private String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_reviews);
        ButterKnife.bind(this);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        this.forceLoadFromProductId = getIntent().getBooleanExtra(FORCE_LOAD_API, false);
        bvProduct = DemoProductsCache.getInstance().getDataItem(productId);

        setupToolbarViews();
        setupRecyclerView();

        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        reviewsUserActionListener = new DemoReviewsPresenter(this, demoConfigUtils, demoDataUtil, productId, this.forceLoadFromProductId, reviewsRecyclerView);
    }

    private void setupToolbarViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        reviewsAdapter = new DemoReviewsAdapter();
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        reviewsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
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
    public void showHeaderView(String imageUrl, String productNameStr, float averageRating) {
        if (!TextUtils.isEmpty(imageUrl)) {
            DemoUtils demoUtils = DemoUtils.getInstance(productImageView.getContext());
            demoUtils.picassoThumbnailLoader()
                    .load(imageUrl)
                    .resizeDimen(R.dimen.side_not_set, R.dimen.clip_half_image_side)
                    .into(productImageView);
        }
        productName.setText(productNameStr);
        if (averageRating >= 0) {
            productRating.setRating(averageRating);
            productRating.setVisibility(View.VISIBLE);
        } else {
            productRating.setVisibility(View.GONE);
        }
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
