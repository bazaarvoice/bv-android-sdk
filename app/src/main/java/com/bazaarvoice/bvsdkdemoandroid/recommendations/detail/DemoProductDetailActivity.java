/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.recommendations.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DividerItemDecoration;

import java.util.List;

public class DemoProductDetailActivity extends AppCompatActivity implements DemoRecommendationDetailContract.View {

    public static final String BV_PRODUCT_ID = "extra_bv_product_id";
    public static final String BV_PRODUCT_NAME = "extra_bv_product_name";
    public static final String BV_PRODUCT_RATING = "extra_bv_product_rating";
    public static final String BV_PRODUCT_IMAGE_URL = "extra_bv_product_image_url";
    public static final String BV_PRODUCT_NUM_REVIEWS = "extra_bv_product_num_reviews";

    private DemoRecommendationDetailContract.UserActionsListener userActionsListener;

    private RecommendationsRecyclerView recyclerView;
    private DemoCategoryRecommendationAdapter demoCategoryRecommendationAdapter;
    private String bvProductId;
    private String bvProductName;
    private float bvProductRating;
    private String bvProductImageUrl;
    private int bvProductNumReviews;

    public static void start(Activity activity, BVProduct bvProduct) {
        Intent intent = new Intent(activity, DemoProductDetailActivity.class);
        intent.putExtra(BV_PRODUCT_ID, bvProduct.getProductId());
        intent.putExtra(BV_PRODUCT_NAME, bvProduct.getProductName());
        intent.putExtra(BV_PRODUCT_RATING, bvProduct.getAverageRating());
        intent.putExtra(BV_PRODUCT_IMAGE_URL, bvProduct.getImageUrl());
        intent.putExtra(BV_PRODUCT_NUM_REVIEWS, bvProduct.getNumReviews());
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentExtras();

        setContentView(R.layout.activity_product_detail);
        recyclerView = (RecommendationsRecyclerView) findViewById(R.id.bvRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        demoCategoryRecommendationAdapter = new DemoCategoryRecommendationAdapter(bvProductImageUrl, bvProductName, bvProductNumReviews, bvProductRating);
        demoCategoryRecommendationAdapter.setOnExtraProductTappedListener(new DemoCategoryRecommendationAdapter.OnExtraProductTappedListener() {
            @Override
            public void onExtraProductTapped(BVProduct bvProduct) {
                userActionsListener.onRelatedRecommendationTapped(bvProduct);
            }
        });
        recyclerView.setAdapter(demoCategoryRecommendationAdapter);
    }

    private void getIntentExtras() {
        bvProductId = getIntent().getStringExtra(BV_PRODUCT_ID);
        bvProductName = getIntent().getStringExtra(BV_PRODUCT_NAME);
        bvProductRating = getIntent().getFloatExtra(BV_PRODUCT_RATING, 0f);
        bvProductImageUrl = getIntent().getStringExtra(BV_PRODUCT_IMAGE_URL);
        bvProductNumReviews = getIntent().getIntExtra(BV_PRODUCT_NUM_REVIEWS, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userActionsListener = new DemoProductDetailPresenter(this, bvProductId);
        userActionsListener.loadRelatedRecommendations(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRelatedRecommendations(List<BVProduct> relatedRecommendations) {
        demoCategoryRecommendationAdapter.setRecommendedProducts(relatedRecommendations);
    }

    @Override
    public void transitionToRelatedRecommendation(BVProduct bvProduct) {
        start(this, bvProduct);
    }
}
