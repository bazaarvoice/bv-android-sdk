/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarAnswer;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DemoAnswersActivity extends AppCompatActivity implements DemoAnswersContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String EXTRA_QUESTION_ID = "extra_question_id";

    private BVProduct bvProduct;
    private DemoAnswersContract.UserActionsListener answerUserActionListener;

    private ImageView productImageView;
    private TextView productName;
    private RatingBar productRating;

    private RecyclerView answersRecyclerView;
    private DemoAnswersAdapter answersAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_answers);
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        String questionId = getIntent().getStringExtra(EXTRA_QUESTION_ID);

        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);

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

        this.answerUserActionListener = new DemoAnswersPresenter(this, demoConfigUtils, demoDataUtil, productId, questionId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        answerUserActionListener.loadAnswers(false);
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
        answersRecyclerView = (RecyclerView) findViewById(R.id.answers_recycler_view);
        answersAdapter = new DemoAnswersAdapter();
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
        answersRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
        answersRecyclerView.setAdapter(answersAdapter);
        answersRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void showAnswers(List<BazaarAnswer> answers) {
        answersAdapter.refreshAnswers(answers);
    }

    public static void transitionTo(Activity fromActivity, String productId, String questionId) {
        Intent intent = new Intent(fromActivity, DemoAnswersActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_QUESTION_ID, questionId);
        fromActivity.startActivity(intent);
    }
}
