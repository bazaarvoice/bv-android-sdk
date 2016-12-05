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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvandroidsdk.QuestionsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoQuestionsActivity extends AppCompatActivity implements DemoQuestionsContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String FORCE_LOAD_API = "extra_force_api_load";

    private BVProduct bvProduct;
    private DemoQuestionsContract.UserActionsListener questionsActionsListener;

    @BindView(R.id.product_image) ImageView productImageView;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_rating) RatingBar productRating;

    @BindView(R.id.questions_recycler_view) QuestionsRecyclerView questionsRecyclerView;
    private DemoQuestionsAdapter questionsAdapter;
    @BindView(R.id.questions_loading) ProgressBar questionLoading;

    private boolean forceLoadFromProductId; // Meaning, a BVProduct is explicitly not provided
    private String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_qanda);
        ButterKnife.bind(this);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        this.forceLoadFromProductId = getIntent().getBooleanExtra(FORCE_LOAD_API, false);

        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);

        if (!forceLoadFromProductId && demoConfigUtils.isDemoClient()) {
            List<BVProduct> recommendedProducts = demoDataUtil.getRecommendedProducts();
            for (BVProduct currRecProd : recommendedProducts) {
                if (currRecProd.getId().equals(productId)) {
                    bvProduct = currRecProd;
                    break;
                }
            }
        } else {
            bvProduct = DemoProductsCache.getInstance().getDataItem(productId);
        }

        setupToolbarViews();
        setupRecyclerView();

        questionsActionsListener = new DemoQuestionsPresenter(this, demoConfigUtils, demoDataUtil, productId, forceLoadFromProductId, questionsRecyclerView);
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

    private void setupRecyclerView() {
        String productId = bvProduct == null ? this.productId : bvProduct.getId();

        questionsAdapter = new DemoQuestionsAdapter(productId);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        questionsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        questionsRecyclerView.setAdapter(questionsAdapter);
        questionsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void showHeaderView(String imageUrl, String productNameStr, float averageRating) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(productImageView.getContext()).load(imageUrl).into(productImageView);
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
