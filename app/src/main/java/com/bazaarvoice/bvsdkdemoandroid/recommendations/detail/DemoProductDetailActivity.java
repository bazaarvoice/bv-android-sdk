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
import com.bazaarvoice.bvsdkdemoandroid.cart.DemoCart;
import com.bazaarvoice.bvsdkdemoandroid.utils.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class DemoProductDetailActivity extends AppCompatActivity implements DemoRecommendationDetailContract.View {

    public static final String BV_PRODUCT = "extra_bv_product";

    private DemoRecommendationDetailContract.UserActionsListener userActionsListener;

    private RecommendationsRecyclerView recyclerView;
    private DemoCategoryRecommendationAdapter demoCategoryRecommendationAdapter;
    private BVProduct bvProduct;
    private DemoCart cart;

    public static void start(Activity activity, BVProduct bvProduct) {
        Intent intent = new Intent(activity, DemoProductDetailActivity.class);
        Gson gson = new GsonBuilder().create();
        intent.putExtra(BV_PRODUCT, gson.toJson(bvProduct));
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

        demoCategoryRecommendationAdapter = new DemoCategoryRecommendationAdapter(bvProduct);
        demoCategoryRecommendationAdapter.setOnExtraProductTappedListener(new DemoCategoryRecommendationAdapter.OnExtraProductTappedListener() {
            @Override
            public void onExtraProductTapped(BVProduct bvProduct) {
                userActionsListener.onRelatedRecommendationTapped(bvProduct);
            }
        });

        demoCategoryRecommendationAdapter.setAddProductToCartLister(new DemoCategoryRecommendationAdapter.AddProductToCartLister() {
            @Override
            public void addProductToCart(BVProduct product) {
                DemoCart.INSTANCE.addProduct(product);
            }
        });

        recyclerView.setAdapter(demoCategoryRecommendationAdapter);
    }

    private void getIntentExtras() {
        Gson gson = new GsonBuilder().create();
        bvProduct = gson.fromJson(getIntent().getStringExtra(BV_PRODUCT), BVProduct.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userActionsListener = new DemoProductDetailPresenter(this, bvProduct.getId(), recyclerView);
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
