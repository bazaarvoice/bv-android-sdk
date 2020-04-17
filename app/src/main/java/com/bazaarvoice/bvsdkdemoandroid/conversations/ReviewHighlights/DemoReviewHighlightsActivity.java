package com.bazaarvoice.bvsdkdemoandroid.conversations.ReviewHighlights;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVReviewHighlightsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.bazaarvoice.bvsdkdemoandroid.curations.map.DemoCurationsMapsActivity.EXTRA_PRODUCT_ID;

public class DemoReviewHighlightsActivity extends AppCompatActivity {
    private boolean forceLoadFromProductId; // Meaning, a BVProduct is explicitly not provided
    private String productId;
    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String FORCE_LOAD_API = "extra_force_api_load";
    BVReviewHighlightsRecyclerView reviewHighlightsRecyclerView;
    @Inject BVConversationsClient bvConversationsClient;
    @Inject DemoClient demoClient;
    @Inject DemoMockDataUtil demoMockDataUtil;
   @Inject DemoConvResponseHandler demoConvResponseHandler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_review_highlights);
        ButterKnife.bind(this);
        DemoApp.getAppComponent(this).inject(this);
        this.productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        this.forceLoadFromProductId = getIntent().getBooleanExtra(FORCE_LOAD_API, false);
        new DemoReviewHighlightsPresenter(productId, forceLoadFromProductId,bvConversationsClient,demoClient,demoMockDataUtil,reviewHighlightsRecyclerView).loadQuestions(false);
    }
}

