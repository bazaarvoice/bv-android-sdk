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
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.curations.detail.DemoCurationsDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class DemoCurationsFeedActivity extends AppCompatActivity implements CurationsFeedCallback {

    private DemoCurationsAdapter curationsAdapter;
    private List<CurationsFeedItem> curationsFeedItems;
    private ProgressBar progressBar;
    private CurationsRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curations_feed);

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
        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        if (demoConfigUtils.isDemoClient()) {
            curationsFeedItems = DemoDataUtil.getInstance(this).getCurationsFeedItems();
            curationsAdapter.setValues(curationsFeedItems);
            dismissLoading();
            return;
        }

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(DemoConstants.CURATIONS_GROUPS)
                .limit(20)
                .hasPhoto(true)
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
        Intent intent = new Intent(this, DemoCurationsDetailActivity.class);
        Gson gson = new GsonBuilder().create();
        intent.putExtra(DemoCurationsDetailActivity.CURRATIONS_UPDATE_KEY, gson.toJson(updates));
        intent.putExtra(DemoCurationsDetailActivity.CURRATIONS_UPDATE_IDX_KEY, idxOfSelected);
        startActivity(intent);
    }

    public static void transitionTo(Activity fromActivity) {
        Intent intent = new Intent(fromActivity, DemoCurationsFeedActivity.class);
        fromActivity.startActivity(intent);
    }
}
