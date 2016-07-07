/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DemoQuestionsActivity extends AppCompatActivity implements DemoQuestionsContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private BVProduct bvProduct;
    private DemoQuestionsContract.UserActionsListener questionsActionsListener;

    private ImageView productImageView;
    private TextView productName;
    private RatingBar productRating;

    private RecyclerView questionsRecyclerView;
    private DemoQuestionsAdapter questionsAdapter;
    private ProgressBar questionLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_qanda);
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);

        if (demoConfigUtils.isDemoClient()) {
            List<BVProduct> recommendedProducts = demoDataUtil.getRecommendedProducts();
            for (BVProduct currRecProd : recommendedProducts) {
                if (currRecProd.getProductId().equals(productId)) {
                    bvProduct = currRecProd;
                    break;
                }
            }
        } else {
            bvProduct = DemoProductsCache.getInstance().getDataItem(productId);
        }

        setupToolbarViews();
        setupHeaderViews();
        setupRecyclerView();

        questionsActionsListener = new DemoQuestionsPresenter(this, demoConfigUtils, demoDataUtil, productId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionsActionsListener.loadQuestions(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbarViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupHeaderViews() {
        productImageView = (ImageView) findViewById(R.id.product_image);
        productName = (TextView) findViewById(R.id.product_name);
        productRating = (RatingBar) findViewById(R.id.product_rating);

        Picasso.with(productImageView.getContext()).load(bvProduct.getImageUrl()).into(productImageView);
        productName.setText(bvProduct.getProductName());
        productRating.setRating(bvProduct.getAverageRating());
    }

    private void setupRecyclerView() {
        questionsRecyclerView = (RecyclerView) findViewById(R.id.questions_recycler_view);
        questionsAdapter = new DemoQuestionsAdapter(bvProduct.getProductId());
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        questionsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        questionsRecyclerView.setAdapter(questionsAdapter);
        questionsRecyclerView.setNestedScrollingEnabled(false);
        questionLoading = (ProgressBar) findViewById(R.id.questions_loading);
    }

    @Override
    public void showQuestions(List<Question> questions) {
        questionsAdapter.refreshQuestions(questions);
    }

    @Override
    public void showLoadingQuestions(boolean show) {
        questionLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoQuestions() {

    }

    @Override
    public void transitionToQandA() {
        // no-op
    }

    @Override
    public void showAskQuestionDialog() {
        // TODO maybe
    }

    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoQuestionsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }

}
