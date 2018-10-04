/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVDisplayableProductContent;
import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvandroidsdk.BVUiQuestionsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoDisplayableProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoQuestionsActivity extends AppCompatActivity implements DemoQuestionsContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String FORCE_LOAD_API = "extra_force_api_load";

    private BVDisplayableProductContent bvProduct;
    private DemoQuestionsContract.UserActionsListener questionsActionsListener;

    @BindView(R.id.product_image) ImageView productImageView;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_rating) RatingBar productRating;

    @BindView(R.id.questions_recycler_view)
    BVUiQuestionsRecyclerView questionsRecyclerView;
    private DemoQuestionsAdapter questionsAdapter;
    @BindView(R.id.questions_loading) ProgressBar questionLoading;
    @BindView(R.id.empty_message) TextView emptyMessage;

    private boolean forceLoadFromProductId; // Meaning, a BVProduct is explicitly not provided
    private String productId;

    @Inject DemoClient demoClient;
    @Inject DemoMockDataUtil demoMockDataUtil;
    @Inject DemoConvResponseHandler demoConvResponseHandler;
    @Inject BVConversationsClient bvConversationsClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations_qanda);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        this.forceLoadFromProductId = getIntent().getBooleanExtra(FORCE_LOAD_API, false);

        if (!forceLoadFromProductId && demoClient.isMockClient()) {
            List<BVProduct> recommendedProducts = demoMockDataUtil.getRecommendationsProfile().getProfile().getRecommendedProducts();
            for (BVProduct currRecProd : recommendedProducts) {
                if (currRecProd.getId().equals(productId)) {
                    bvProduct = currRecProd;
                    break;
                }
            }
        } else {
            bvProduct = DemoDisplayableProductsCache.getInstance().getDataItem(productId);
        }

        setupToolbarViews();
        setupRecyclerView();

        questionsActionsListener = new DemoQuestionsPresenter(this, bvConversationsClient, demoClient, demoMockDataUtil, productId, forceLoadFromProductId, questionsRecyclerView);
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
            Picasso.get().load(imageUrl).into(productImageView);
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
        emptyMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void transitionToQandA() {
        // no-op
    }

    @Override
    public void showAskQuestionDialog() {
        // TODO maybe
    }

    @Override
    public void showDialogWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("OK", null).create().show();
    }

    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoQuestionsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }

}
