/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedRequest;
import com.bazaarvoice.bvandroidsdk.CurationsImageLoader;
import com.bazaarvoice.bvandroidsdk.CurationsInfiniteRecyclerView;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.cart.DemoCart;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.DemoProductContract;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.DemoProductPresenter;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.DemoReviewsActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoCurationsPostActivity;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bazaarvoice.bvsdkdemoandroid.utils.DemoRequiredKeyUiUtil.getNoReccosApiKeyDialog;

public class DemoFancyProductDetailActivity extends AppCompatActivity implements
        DemoProductRecContract.View,
        DemoProductDetailRecAdapter.ProductTapListener,
        DemoProductDetailCurationsAdapter.CurationFeedItemTapListener,
        DemoProductContract.View {
    // region Properties
    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String EXTRA_PRODUCT_NAME = "extra_product_name";
    private static final String EXTRA_PRODUCT_IMAGE_URL = "extra_product_image_url";

    String productId;
    String productName;
    String productImageUrl;
    float productAverageRating;

    @BindView(R.id.product_image) ImageView prodImage;
    @BindView(R.id.product_name) TextView prodName;
    @BindView(R.id.product_rating) RatingBar prodRating;
    @BindView(R.id.addToCartButton) Button addToCartButton;

    @BindView(R.id.product_row_rec_recycler_view) RecommendationsRecyclerView recommendationsRecyclerView;
    DemoProductDetailRecAdapter recAdapter;
    @BindView(R.id.no_recs_found) TextView noRecsFoundTextView;
    @BindView(R.id.get_recs_progress) ProgressBar getRecsProgressBar;
    @BindView(R.id.rowRecs) View rowRecs;

    @BindView(R.id.product_row_curations_recycler_view) CurationsInfiniteRecyclerView curationsRecyclerView;
    @BindView(R.id.no_curations_found) TextView noCurationsFoundTextView;
    @BindView(R.id.get_curations_progress) ProgressBar getCurationsProgressBar;
    @BindView(R.id.curations_submit_container) ViewGroup curationsSubmitViewGroup;
    @BindView(R.id.curations_submit_image) TextView fontAwesomeCameraIcon;
    @BindView(R.id.curations_locations) ViewGroup curationsLocationsContainer;
    @BindView(R.id.curations_locations_image) TextView fontAwesomeLocationIcon;
    @BindView(R.id.curations_seperator_2) View curationsSnippetSpacer2;
    @BindView(R.id.rowCurations) View rowCurations;

    @BindView(R.id.conv_reviews) TextView convReviews;
    @BindView(R.id.conv_reviews_summary_text) TextView convReviewsSummaryText;
    @BindView(R.id.conv_questions_image) TextView fontAwesomeQuestionIcon;
    @BindView(R.id.conv_reviews_loading) ProgressBar convReviewsProgressBar;
    @BindView(R.id.conv_reviews_summary_loading) ProgressBar convReviewsSummaryProgressBar;
    @BindView(R.id.conv_questions) TextView convQuestions;
    @BindView(R.id.conv_reviews_image) TextView fontAwesomeReviewsIcon;

    @BindView(R.id.conv_reviews_summary_image) TextView fontAwesomeReviewsSummaryIcon;
    @BindView(R.id.conv_questions_loading) ProgressBar convQuestionsProgressBar;

    private DemoProductRecContract.UserActionsListener recRowActionListener;
    private DemoProductContract.UserActionsListener productDataActionListener;

    @Inject DemoMockDataUtil demoMockDataUtil;
    @Inject CurationsImageLoader curationsImageLoader;
    @Inject DemoClient demoClient;
    @Inject BVConversationsClient bvConversationsClient;

    // endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fancy_product_detail);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_PRODUCT_ID)) {
            productId = savedInstanceState.getString(EXTRA_PRODUCT_ID);
            productName = savedInstanceState.getString(EXTRA_PRODUCT_NAME);
            productImageUrl = savedInstanceState.getString(EXTRA_PRODUCT_IMAGE_URL);
        } else {
            productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
            if (getIntent().hasExtra(EXTRA_PRODUCT_NAME)) {
                productName = getIntent().getStringExtra(EXTRA_PRODUCT_NAME);
                productImageUrl = getIntent().getStringExtra(EXTRA_PRODUCT_IMAGE_URL);
            } else {
                BVDisplayableProductContent bvProduct = DemoDisplayableProductsCache.getInstance().getDataItem(productId);
                productName = bvProduct.getDisplayName();
                productImageUrl = bvProduct.getDisplayImageUrl();
                productAverageRating = bvProduct.getAverageRating();
            }
        }

        setupToolbar();
        setupFab();
        setupHeader();
        setupConversationsRow();
        setupRecommendationsRow();
        setupCurationsRow();

        recRowActionListener = new DemoProductRecPresenter(this, demoClient, demoMockDataUtil, false, recommendationsRecyclerView);
        productDataActionListener = new DemoProductPresenter(demoClient, bvConversationsClient, demoMockDataUtil, this, productId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recRowActionListener.loadRecommendationsWithProductId(false, productId);
        productDataActionListener.loadProduct(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PRODUCT_ID, productId);
        outState.putString(EXTRA_PRODUCT_NAME, productName);
        outState.putString(EXTRA_PRODUCT_IMAGE_URL, productImageUrl);
    }

    private void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(productName);
    }

    private void setupFab() {
        final FloatingActionButton fab = findViewById(R.id.fab);
        final DemoRouter router = new DemoRouter(this);
        fab.setOnClickListener(v -> router.transitionToSubmitReviewActivity(productId));
    }

    private void setupHeader() {
        Picasso.get()
                .load(productImageUrl)
                .into(prodImage);

        prodName.setText(productName);
        prodRating.setRating((int)productAverageRating);

        final ProductContentContainer contentContainer = new ProductContentContainer(productId, productName, productImageUrl, productAverageRating);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemoCart.INSTANCE.addProduct(contentContainer);

            }
        });
    }

    private void setupConversationsRow() {
        ViewGroup convQuestionsViewGroup = findViewById(R.id.conv_questions_container);
        convQuestionsViewGroup.setOnClickListener(convQuestionsRowClickListener);

        // Add the Font-Awesome icon
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        fontAwesomeReviewsIcon.setTypeface(fontAwesomeFont);

        Typeface fontAwesomeSummaryFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        fontAwesomeReviewsSummaryIcon.setTypeface(fontAwesomeFont);

        ViewGroup convReviewsViewGroup = findViewById(R.id.conv_reviews_container);
        convReviewsViewGroup.setOnClickListener(convReviewsRowClickListener);
        // Add the Font-Awesome icon
        fontAwesomeQuestionIcon.setTypeface(fontAwesomeFont);
    }

    private View.OnClickListener convQuestionsRowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            productDataActionListener.onQandATapped();
        }
    };

    private View.OnClickListener convReviewsRowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            productDataActionListener.onReviewsTapped();
        }
    };

    private View.OnClickListener curationsSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DemoFancyProductDetailActivity.this, DemoCurationsPostActivity.class);
            startActivity(intent);
        }
    };

    @OnClick(R.id.curations_locations)
    void onCurationsLocationClicked() {
        String googleMapsKey = getResources().getString(R.string.google_maps_key);
        boolean haveGoogleMapsApiKey =  DemoConstants.isSet(googleMapsKey);
        if (haveGoogleMapsApiKey) {
            DemoRouter.transitionToCurationsMapView(this, productId);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Google API Key Missing")
                    .setMessage("To view Curations Locations you must obtain a Google Maps API key, and place it in the google_maps_api.xml file in this project")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private void setupRecommendationsRow() {
        if (!demoClient.hasShopperAds() && !demoClient.isMockClient()) {
            rowRecs.setVisibility(View.GONE);
            return;
        }
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recAdapter = new DemoProductDetailRecAdapter(this);
        recAdapter.setProductTapListener(this);
        recommendationsRecyclerView.setAdapter(recAdapter);
        recommendationsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupCurationsRow() {
        if (!demoClient.hasCurations() && !demoClient.isMockClient()) {
            rowCurations.setVisibility(View.GONE);
            return;
        }
        curationsRecyclerView.setNestedScrollingEnabled(false);
        final CurationsFeedRequest request = new CurationsFeedRequest
            .Builder(Collections.singletonList("__all__"))
            .externalId(productId)
            .build();
        curationsRecyclerView
            .setRequest(request)
            .setImageLoader(curationsImageLoader)
            .setOnPageLoadListener(new CurationsInfiniteRecyclerView.OnPageLoadListener() {
                @Override
                public void onPageLoadSuccess(int pageIndex, int pageSize) {
                    Log.d("CurationPageLoad", "success - pageIndex: " + pageIndex + ", pageSize: " + pageSize);
                }

                @Override
                public void onPageLoadFailure(int pageIndex, Throwable throwable) {
                    DemoFancyProductDetailActivity activity = DemoFancyProductDetailActivity.this;
                    if (!activity.isFinishing()) {
                        new AlertDialog.Builder(activity)
                            .setTitle("Error!")
                            .setMessage(throwable.getMessage())
                            .show();
                        throwable.printStackTrace();
                    }
                }
            })
            .setOnFeedItemClickListener(new CurationsInfiniteRecyclerView.OnFeedItemClickListener() {
                @Override
                public void onClick(CurationsFeedItem curationsFeedItem) {
                    DemoFancyProductDetailActivity activity = DemoFancyProductDetailActivity.this;
                    if (!activity.isFinishing()) {
                        String productId = curationsFeedItem.getProductId();
                        String feedItemId = String.valueOf(curationsFeedItem);
                        DemoRouter.transitionToCurationsFeedItem(activity, productId, feedItemId);
                    }
                }
            })
            .load();

        curationsSubmitViewGroup.setOnClickListener(curationsSubmitClickListener);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        fontAwesomeCameraIcon.setTypeface(fontAwesomeFont);
        fontAwesomeLocationIcon.setTypeface(fontAwesomeFont);
    }

    private void showReviewDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DemoSubmitDialogFragment.newInstance(getString(R.string.review_body_hint));
        dialog.show(getSupportFragmentManager(), "SubmitReviewDialogFragment");
    }

    // region Product Recommendations UI API

    @Override
    public void showRecommendations(List<BVProduct> bvProducts) {
        recAdapter.refreshRecs(bvProducts);
        noRecsFoundTextView.setVisibility(View.GONE);
        getRecsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingRecs(boolean show) {
        getRecsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoRecommendations() {
        noRecsFoundTextView.setVisibility(View.VISIBLE);
        getRecsProgressBar.setVisibility(View.GONE);
        recAdapter.refreshRecs(Collections.<BVProduct>emptyList());
    }

    @Override
    public void showError() {

    }

    @Override
    public void showRecMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoApiKey(String clientDisplayName) {
        getNoReccosApiKeyDialog(this, clientDisplayName).show();
    }

    // endregion

    // region Curations UI API

    public void showCurations() {
        noCurationsFoundTextView.setVisibility(View.GONE);
        getCurationsProgressBar.setVisibility(View.GONE);
    }

    public void showLoadingCurations(boolean show) {
        getCurationsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showNoCurations() {
        noCurationsFoundTextView.setVisibility(View.VISIBLE);
        getCurationsProgressBar.setVisibility(View.GONE);
    }

    public void showCurationsMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // endregion

    public static void transitionTo(Context fromContext, String productId) {
        Intent intent = new Intent(fromContext, DemoFancyProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromContext.startActivity(intent);
    }

    /**
     * Use this to start the activity when the Product is cached, and is accessible with the product id
     * @param fromActivity
     * @param productId
     */
    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoFancyProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }

    /**
     * User this to start the activity when the Product is not cached, and is not accessible with the product id
     * @param fromActivity
     * @param productId
     * @param productName
     * @param productImageUrl
     */
    public static void transitionTo(Activity fromActivity, String productId, String productName, String productImageUrl) {
        Intent intent = new Intent(fromActivity, DemoFancyProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_PRODUCT_NAME, productName);
        intent.putExtra(EXTRA_PRODUCT_IMAGE_URL, productImageUrl);
        fromActivity.startActivity(intent);
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
    public void onProductTapListener(BVProduct bvProduct) {
        DemoFancyProductDetailActivity.transitionTo(this, bvProduct.getId());
    }

    @Override
    public void onCurationFeedItemTapped(CurationsFeedItem curationsFeedItem) {
//        curationsRowActionListener.onCurationsFeedItemTapped(curationsFeedItem);
    }

    // region Product Detail UI API

    @Override
    public void transitionToReviews() {
        DemoReviewsActivity.transitionTo(DemoFancyProductDetailActivity.this, productId);
    }

    @Override
    public void showProduct(Product product) {
        int numReviews = (product.getReviewStatistics() != null) ? product.getReviewStatistics().getTotalReviewCount() : 0;
        int numQuestions = (product.getQaStatistics() != null) ? product.getQaStatistics().getTotalQuestionCount() : 0;
        int numAnswers = (product.getQaStatistics() != null) ? product.getQaStatistics().getTotalAnswerCount() : 0;

        if (numReviews > 0) {
            convReviews.setText(String.format(getString(R.string.conv_reviews_photos), numReviews));
        } else {
            convReviews.setText("0 Reviews, be the first!");
        }

        if (numQuestions > 0) {
            convQuestions.setText(String.format(getString(R.string.conv_questions), numQuestions, numAnswers));
        } else {
            convQuestions.setText("0 Questions, be the first!");
        }
    }

    @Override
    public void showNoProduct() {
        convReviews.setText("0 Reviews, be the first!");
        convQuestions.setText("0 Questions, be the first!");
    }

    @Override
    public void showLoadingProduct(boolean show) {
        convReviewsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        convQuestionsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        convReviewsSummaryProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showSubmitReviewDialog() {
        showReviewDialog();
    }

    @Override
    public void showAskQuestionDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DemoSubmitDialogFragment.newInstance(getString(R.string.question_body_hint));
        dialog.show(getSupportFragmentManager(), "SubmitQuestionDialogFragment");
    }

    @Override
    public void transitionToQandA() {
        DemoQuestionsActivity.transitionTo(this, productId);
    }

    // endregion

    private static class ProductContentContainer implements BVDisplayableProductContent {
        private String id;
        private String name;
        private String imageUrl;
        private float avgRating;

        private ProductContentContainer(String id, String name, String imageUrl, float avgRating) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
            this.avgRating = avgRating;
        }

        @NonNull
        @Override
        public String getId() {
            return id;
        }

        @Nullable
        @Override
        public String getDisplayName() {
            return name;
        }

        @Nullable
        @Override
        public String getDisplayImageUrl() {
            return imageUrl;
        }

        @Override
        public float getAverageRating() {
            return avgRating;
        }

    }
}
