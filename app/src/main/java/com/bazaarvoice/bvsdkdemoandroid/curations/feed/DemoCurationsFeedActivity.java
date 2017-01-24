package com.bazaarvoice.bvsdkdemoandroid.curations.feed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.bazaarvoice.bvandroidsdk.CurationsFeedCallback;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsFeedRequest;
import com.bazaarvoice.bvandroidsdk.CurationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;

import java.util.List;

import javax.inject.Inject;

public class DemoCurationsFeedActivity extends AppCompatActivity implements CurationsFeedCallback {

    private DemoCurationsAdapter curationsAdapter;
    private List<CurationsFeedItem> curationsFeedItems;
    private ProgressBar progressBar;
    private CurationsRecyclerView recyclerView;

    @Inject DemoConfigUtils demoConfigUtils;
    @Inject DemoDataUtil demoDataUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_feed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DemoApp.get(this).getAppComponent().inject(this);

        progressBar = (ProgressBar) findViewById(R.id.curations_loading);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (CurationsRecyclerView) findViewById(R.id.curations_custom_list);
        recyclerView.setLayoutManager(layoutManager);

        curationsAdapter = new DemoCurationsAdapter();
        curationsAdapter.setOnItemClickListener(new DemoCurationsAdapter.OnCurationsUpdatePressedListener() {
            @Override
            public void onCurationsUpdatePressed(CurationsFeedItem update, View row) {
                transitionToCurationsDetail(curationsFeedItems, curationsFeedItems.indexOf(update));
            }
        });

        recyclerView.setAdapter(curationsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCurationsFeed(recyclerView);
    }

    private void loadCurationsFeed(CurationsRecyclerView recyclerView) {
        if (demoConfigUtils.isDemoClient()) {
            curationsFeedItems = demoDataUtil.getCurationsFeedItems();
            curationsAdapter.setValues(curationsFeedItems);
            dismissLoading();
            return;
        }

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(DemoConstants.CURATIONS_GROUPS)
                .limit(20)
                .withProductData(true)
                .build();

        recyclerView.getCurationsFeedItems(request, "MainGrid", this);
    }


    @Override
    public void onSuccess(List<CurationsFeedItem> feedItems) {
        curationsFeedItems = feedItems;
        curationsAdapter.setValues(feedItems);
        dismissLoading();
    }

    @Override
    public void onFailure(Throwable throwable) {
        dismissLoading();
    }

    private void dismissLoading() {
        progressBar.setVisibility(View.GONE);
    }

    public void transitionToCurationsDetail(List<CurationsFeedItem> updates, int idxOfSelected) {
        CurationsFeedItem curationsFeedItem = updates.get(idxOfSelected);
        String productId = curationsFeedItem != null && curationsFeedItem.getProducts() != null && !curationsFeedItem.getProducts().isEmpty() ? curationsFeedItem.getProducts().get(0).getId() : "";
        String feedItemId = String.valueOf(curationsFeedItem.getId());
        DemoRouter.transitionToCurationsFeedItem(this, productId, feedItemId);
    }

    public static void transitionTo(Activity fromActivity) {
        Intent intent = new Intent(fromActivity, DemoCurationsFeedActivity.class);
        fromActivity.startActivity(intent);
    }
}
