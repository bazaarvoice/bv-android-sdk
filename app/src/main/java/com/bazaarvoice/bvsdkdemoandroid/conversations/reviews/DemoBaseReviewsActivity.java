/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVProductSentimentsClient;
import com.bazaarvoice.bvandroidsdk.BVUiConversationsDisplayRecyclerView;
import com.bazaarvoice.bvandroidsdk.BaseReview;
import com.bazaarvoice.bvandroidsdk.BestFeature;
import com.bazaarvoice.bvandroidsdk.FeatureSentiment;
import com.bazaarvoice.bvandroidsdk.FeaturesSentiment;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentResponse;
import com.bazaarvoice.bvandroidsdk.Quote;
import com.bazaarvoice.bvandroidsdk.ReviewOptions;
import com.bazaarvoice.bvandroidsdk.SummarisedFeaturesResponse;
import com.bazaarvoice.bvandroidsdk.WorstFeature;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments.ChipAdapter;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments.ReviewAdapter;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.productsentiments.ReviewItem;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

abstract class DemoBaseReviewsActivity<ReviewType extends BaseReview> extends AppCompatActivity implements DemoReviewsContract.View<ReviewType> {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String EXTRA_FILTER_TYPE_ID = "extra_filter_type_id";
    private static final String EXTRA_FILTER_OPERATOR_ID = "extra_filter_operator_id";
    private static final String EXTRA_FILTER_VALUE_ID = "extra_filter_value_id";
    private static final String EXTRA_FORCE_API_LOAD = "extra_force_api_load";

    private BVDisplayableProductContent bvProduct;
    private DemoReviewsContract.UserActionsListener reviewsUserActionListener;

    @BindView(R.id.product_row_header)
    CardView productRowHeader;
    @BindView(R.id.product_image)
    ImageView productImageView;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.product_rating)
    RatingBar productRating;

    BVUiConversationsDisplayRecyclerView reviewsRecyclerView;
    @BindView(R.id.reviews_loading)
    ProgressBar reviewsLoading;
    @BindView(R.id.recyclerViewStub)
    ViewStub recyclerViewStub;
    @BindView(R.id.empty_message)
    TextView emptyMessage;

    private DemoReviewsAdapter<ReviewType> reviewsAdapter;

    private boolean forceLoadFromProductId; // Meaning, a BVProduct is explicitly not provided
    private String productId;
    private ReviewOptions.PrimaryFilter filterType;
    private String filterTypeIntent;
    private String filterValue;
    private ReviewAdapter reviewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_reviews);
        ButterKnife.bind(this);
        inflateRecyclerView();
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        this.filterTypeIntent = getIntent().getStringExtra(EXTRA_FILTER_TYPE_ID);
        this.filterValue = getIntent().getStringExtra(EXTRA_FILTER_VALUE_ID);
        this.forceLoadFromProductId = getIntent().getBooleanExtra(EXTRA_FORCE_API_LOAD, false);
        bvProduct = DemoDisplayableProductsCache.getInstance().getDataItem(productId);

        setFilterTypeFromIntent(filterTypeIntent);
        setupToolbarViews();
        setupRecyclerView();
        DemoMockDataUtil demoMockDataUtil = getDataUtil();
        reviewsUserActionListener = getReviewsUserActionListener(this, getConvClient(),getPsClient(), getDemoClient(), demoMockDataUtil, productId, forceLoadFromProductId, reviewsRecyclerView);
    }

    abstract DemoClient getDemoClient();

    abstract DemoMockDataUtil getDataUtil();

    abstract Picasso getPicasso();

    abstract BVConversationsClient getConvClient();
    abstract BVProductSentimentsClient getPsClient();

    void inflateRecyclerView() {
        recyclerViewStub.setLayoutResource(R.layout.reviews_recyclerview);
        recyclerViewStub.inflate();
        reviewsRecyclerView = (BVUiConversationsDisplayRecyclerView) findViewById(R.id.reviews_recycler_view);
    }

    protected DemoReviewsContract.UserActionsListener getReviewsUserActionListener(DemoReviewsContract.View view, BVConversationsClient client,BVProductSentimentsClient psClient, DemoClient demoClient, DemoMockDataUtil demoMockDataUtil, String productId, boolean forceLoadFromProductId, BVUiConversationsDisplayRecyclerView reviewsRecyclerView) {
        return new DemoReviewsPresenter(view, client,psClient, demoClient, demoMockDataUtil, productId, filterValue, filterType, forceLoadFromProductId, reviewsRecyclerView);
    }

    private void setupToolbarViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        reviewsAdapter = (DemoReviewsAdapter) createAdapter();
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        reviewsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setFilterTypeFromIntent(String filterType) {
        if(filterType == null) {
            return;
        }
        switch (filterType) {
            case "ProductId":
                this.filterType = ReviewOptions.PrimaryFilter.ProductId;
                break;
            case "AuthorId":
                this.filterType = ReviewOptions.PrimaryFilter.AuthorId;
                break;
            case "SubmissionId":
                this.filterType = ReviewOptions.PrimaryFilter.SubmissionId;
                break;
            case "CategoryAncestorId":
                this.filterType = ReviewOptions.PrimaryFilter.CategoryAncestorId;
                break;
            default:
                this.filterType = null;
        }
    }

    protected abstract DemoReviewsAdapter<ReviewType> createAdapter();

    @Override
    protected void onResume() {
        super.onResume();
        reviewsUserActionListener.loadReviews(false);
        reviewsUserActionListener.loadSummarisedFeatures(false);
        reviewsUserActionListener.loadFeaturesSentiment(false);
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
            getPicasso().load(imageUrl)
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
    public void showReviews(List<ReviewType> reviews) {
        reviewsAdapter.refreshReviews(reviews);
    }

    @Override
    public void showLoadingReviews(boolean show) {
        reviewsLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoReviews() {
        emptyMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialogWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
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

    public static void transitionTo(Activity fromActivity, String filterType, String filterOperator, String filterValue) {
        Intent intent = new Intent(fromActivity, DemoReviewsActivity.class);
        intent.putExtra(EXTRA_FILTER_TYPE_ID, filterType);
        intent.putExtra(EXTRA_FILTER_OPERATOR_ID, filterOperator);
        intent.putExtra(EXTRA_FILTER_VALUE_ID, filterValue);
        fromActivity.startActivity(intent);
    }


    @Override
    public void showSummarisedFeatures(SummarisedFeaturesResponse response) {
        List<ReviewItem> allItems = new ArrayList<>();

        // 1. Build the list of ALL possible items (chips and quotes)
        // Add best features section
        if (response.getBestFeatures() != null && !response.getBestFeatures().isEmpty()) {
            List<String> positiveChips = new ArrayList<>();
            for (BestFeature feature : response.getBestFeatures()) {
                positiveChips.add(feature.getFeature());
            }
            // Using R.drawable.ic_add_circle from our previous step
            allItems.add(new ReviewItem.ChipSection("Pros", positiveChips, R.drawable.ic_add_circle));

            List<String> positiveQuotes = new ArrayList<>();
            for (BestFeature feature : response.getBestFeatures()) {
                if (feature.getEmbedded() != null && feature.getEmbedded().getQuotes() != null) {
                    for (Quote quote : feature.getEmbedded().getQuotes()) {
                        positiveQuotes.add(quote.getText());
                    }
                }
            }
            if (!positiveQuotes.isEmpty()) {
                allItems.add(new ReviewItem.QuoteCard("What people like", positiveQuotes));
            }
        }

        // Add worst features section
        if (response.getWorstFeatures() != null && !response.getWorstFeatures().isEmpty()) {
            List<String> negativeChips = new ArrayList<>();
            for (WorstFeature feature : response.getWorstFeatures()) {
                negativeChips.add(feature.getFeature());
            }
            // Using R.drawable.ic_remove_circle from our previous step
            allItems.add(new ReviewItem.ChipSection("Cons", negativeChips, R.drawable.ic_remove_circle));

            List<String> negativeQuotes = new ArrayList<>();
            for (WorstFeature feature : response.getWorstFeatures()) {
                if (feature.getEmbedded() != null && feature.getEmbedded().getQuotes() != null) {
                    for (Quote quote : feature.getEmbedded().getQuotes()) {
                        negativeQuotes.add(quote.getText());
                    }
                }
            }
            if (!negativeQuotes.isEmpty()) {
                allItems.add(new ReviewItem.QuoteCard("What people dislike", negativeQuotes));
            }
        }

        // If the list has content, set up the adapter
        if (!allItems.isEmpty()) {
            RecyclerView recyclerView = findViewById(R.id.recycler_view_sum_features);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // 2. Give the full list to the stateful adapter.
            //    The adapter will handle the toggle logic internally.
            //    (Ensure you are using the ReviewAdapter from the previous answer)
            ReviewAdapter adapter = new ReviewAdapter(this, allItems);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void showFeaturesSentiment(FeaturesSentimentResponse response) {
        if (response != null && response.getFeatures() != null) {

            List<String> features = new ArrayList<>();
            for (FeatureSentiment feature : response.getFeatures()) {
                features.add(feature.getFeature());
            }
            // Set up RecyclerView for features chips
            RecyclerView chipsRecyclerView = findViewById(R.id.recyclerViewChips );
            chipsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            ChipAdapter chipAdapter = new ChipAdapter(this, features);
            chipsRecyclerView.setAdapter(chipAdapter);
        }
    }
}
