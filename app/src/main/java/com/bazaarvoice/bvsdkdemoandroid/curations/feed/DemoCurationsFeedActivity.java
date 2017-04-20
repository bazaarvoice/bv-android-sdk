package com.bazaarvoice.bvsdkdemoandroid.curations.feed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedRequest;
import com.bazaarvoice.bvandroidsdk.CurationsImageLoader;
import com.bazaarvoice.bvandroidsdk.CurationsInfiniteRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;

import javax.inject.Inject;

public class DemoCurationsFeedActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CurationsInfiniteRecyclerView curationsInfiniteRecyclerView;

    @Inject
    DemoClientConfigUtils demoClientConfigUtils;
    @Inject
    DemoMockDataUtil demoMockDataUtil;
    @Inject CurationsImageLoader curationsImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_feed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DemoApp.getAppComponent(this).inject(this);

        progressBar = (ProgressBar) findViewById(R.id.curations_loading);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        curationsInfiniteRecyclerView = (CurationsInfiniteRecyclerView) findViewById(R.id.product_row_curations_recycler_view);
        curationsInfiniteRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCurationsFeed(curationsInfiniteRecyclerView);
    }

    private void loadCurationsFeed(CurationsInfiniteRecyclerView recyclerView) {

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(DemoConstants.CURATIONS_GROUPS)
            .limit(20)
            .withProductData(true)
            .build();

        recyclerView.setRequest(request)
            .setImageLoader(curationsImageLoader)
            .setOnPageLoadListener(new CurationsInfiniteRecyclerView.OnPageLoadListener() {
                @Override
                public void onPageLoadSuccess(int pageIndex, int pageSize) {
                    dismissLoading();
                }

                @Override
                public void onPageLoadFailure(int pageIndex, Throwable throwable) {
                    throwable.printStackTrace();
                    dismissLoading();
                }
            })
            .setOnFeedItemClickListener(new CurationsInfiniteRecyclerView.OnFeedItemClickListener() {
                @Override
                public void onClick(CurationsFeedItem curationsFeedItem) {
                    DemoCurationsFeedActivity activity = DemoCurationsFeedActivity.this;
                    if (!activity.isFinishing()) {
                        String productId = "";
                        String feedItemId = "";
                        if (curationsFeedItem != null) {
                            if (curationsFeedItem.getProducts() != null && !curationsFeedItem.getProducts().isEmpty()) {
                                productId = curationsFeedItem.getProducts().get(0).getId();
                            }
                            feedItemId = String.valueOf(curationsFeedItem.getId());
                        }
                        DemoRouter.transitionToCurationsFeedItem(activity, productId, feedItemId);
                    }
                }
            })
            .load();
    }


    private void dismissLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
