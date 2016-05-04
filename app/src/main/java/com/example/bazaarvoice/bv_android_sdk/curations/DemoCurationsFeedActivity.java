package com.example.bazaarvoice.bv_android_sdk.curations;

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
import com.example.bazaarvoice.bv_android_sdk.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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
        boolean haveLocalCache = !DemoCurationsCache.getFeedItems().isEmpty();
        boolean shouldHitNetwork = !haveLocalCache;

        if (shouldHitNetwork) {
            ArrayList<String> groups = new ArrayList<>();
            groups.add("__all__");
            CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups)
                    .limit(20)
                    .hasPhoto(true)
                    .withProductData(true)
                    .build();

            recyclerView.getCurationsFeedItems(request, "MainGrid", this);
        } else {
            curationsFeedItems = DemoCurationsCache.getFeedItems();
            curationsAdapter.setValues(curationsFeedItems);
            dismissLoading();
        }
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
}
