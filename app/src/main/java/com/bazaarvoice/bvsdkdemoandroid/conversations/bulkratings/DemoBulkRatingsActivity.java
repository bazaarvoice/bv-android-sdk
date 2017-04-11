/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.bulkratings;

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
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.ConversationsDisplayRecyclerView;
import com.bazaarvoice.bvandroidsdk.Statistics;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoBulkRatingsActivity extends AppCompatActivity implements DemoBulkRatingsContract.View {

    private static final String EXTRA_PRODUCT_IDS = "extra_bulk_product_ids";

    private DemoBulkRatingsContract.UserActionsListener bulkRatingsUserActionListener;

    private CardView productRowHeader;
    private TextView productName;
    private RatingBar productRating;

    private RecyclerView reviewsRecyclerView;
    private DemoBulkRatingsAdapter bulkReviewAdapter;
    private ProgressBar reviewsLoading;
    @BindView(R.id.recyclerViewStub)
    ViewStub recyclerViewStub;

    @Inject
    DemoClientConfigUtils demoClientConfigUtils;
    @Inject
    DemoMockDataUtil demoMockDataUtil;

    private ArrayList<String> bulkProductIds = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_reviews);
        ButterKnife.bind(this);
        inflateRecyclerView();
        Bundle extra = getIntent().getBundleExtra("extra");
        this.bulkProductIds = (ArrayList<String>) extra.getSerializable(EXTRA_PRODUCT_IDS);

        setupToolbarViews();
        setupHeaderViews();
        setupRecyclerView();

        DemoApp.get(this).getAppComponent().inject(this);
        bulkRatingsUserActionListener = new DemoBulkRatingsPresenter(this, demoClientConfigUtils, demoMockDataUtil, bulkProductIds);
    }

    void inflateRecyclerView() {
        recyclerViewStub.setLayoutResource(R.layout.reviews_recyclerview);
        recyclerViewStub.inflate();
        reviewsRecyclerView = (ConversationsDisplayRecyclerView) findViewById(R.id.store_reviews_recycler_view);
    }

    private void setupToolbarViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupHeaderViews() {
        productRowHeader = (CardView) findViewById(R.id.product_row_header);
        (productRowHeader.findViewById(R.id.product_image)).setVisibility(View.INVISIBLE);

        productName = (TextView) productRowHeader.findViewById(R.id.product_name);
        productRating = (RatingBar) productRowHeader.findViewById(R.id.product_rating);

        productName.setText("Testing product ids: " + bulkProductIds.toString());
        productRating.setVisibility(View.INVISIBLE);

    }

    private void setupRecyclerView() {
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        bulkReviewAdapter = new DemoBulkRatingsAdapter();
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        reviewsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        reviewsRecyclerView.setAdapter(bulkReviewAdapter);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
        reviewsLoading = (ProgressBar) findViewById(R.id.reviews_loading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bulkRatingsUserActionListener.loadRatings();
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
    public void showRatings(List<Statistics> bazaarStatistics) {
        bulkReviewAdapter.refreshReviews(bazaarStatistics);
    }

    @Override
    public void showLoadingRatings(boolean show) {
        reviewsLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoRatings() {

    }

    @Override
    public void showRatingsMessage(String message) {
        Toast.makeText(DemoBulkRatingsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void transitionToRatings() {
        // no-op
    }

    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoBulkRatingsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_IDS, productId);
        fromActivity.startActivity(intent);
    }
}
