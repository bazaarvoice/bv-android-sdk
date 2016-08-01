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
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Answer;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

public class DemoAnswersActivity extends AppCompatActivity implements DemoAnswersContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String EXTRA_QUESTION_ID = "extra_question_id";

    private BVProduct bvProduct;
    private DemoAnswersContract.UserActionsListener answerUserActionListener;

    @BindView(R.id.product_image) ImageView productImageView;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_rating) RatingBar productRating;

    private RecyclerView answersRecyclerView;
    private DemoAnswersAdapter answersAdapter;

    private String questionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_answers);
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        questionId = getIntent().getStringExtra(EXTRA_QUESTION_ID);

        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);

        if (bvProduct != null && demoConfigUtils.isDemoClient()) {
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
        setupRecyclerView();

        this.answerUserActionListener = new DemoAnswersPresenter(this, demoConfigUtils, demoDataUtil, productId, questionId, bvProduct == null);
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
    public void showHeaderView(String imageUrl, String productNameStr, float averageRating) {
        if (bvProduct != null) {
            Picasso.with(productImageView.getContext()).load(imageUrl).into(productImageView);
            productName.setText(productNameStr);
            productRating.setRating(averageRating);
        } else {
            productName.setText("Testing Question Id: " + questionId);
            productRating.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showAnswers(List<Answer> answers) {
        answersAdapter.refreshAnswers(answers);
    }

    public static void transitionTo(Activity fromActivity, String productId, String questionId) {
        Intent intent = new Intent(fromActivity, DemoAnswersActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_QUESTION_ID, questionId);
        fromActivity.startActivity(intent);
    }
}
