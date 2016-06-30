/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.recommendations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DividerItemDecoration;

import java.util.Collections;
import java.util.List;

public class DemoRecommendationsFragment extends Fragment implements DemoRecommendationsContract.View {

    private static final String TAG = DemoRecommendationsFragment.class.getSimpleName();
    private DemoRecommendationsContract.UserActionsListener userActionsListener;

    private DemoProductAdapter demoProductAdapter;
    private RecommendationsRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noRecsFoundTextView;
    private ProgressBar getRecsProgressBar;

    public static DemoRecommendationsFragment newInstance() {
        return new DemoRecommendationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recommendations_raw, container, false);

        recyclerView = (RecommendationsRecyclerView) view.findViewById(R.id.recommendations_custom_list);
        demoProductAdapter = new DemoProductAdapter();
        demoProductAdapter.setOnItemClickListener(new DemoProductAdapter.OnBvProductClickListener() {
            @Override
            public void onBvProductClickListener(BVProduct bvProduct, View rowView) {
                userActionsListener.onRecommendationProductTapped(bvProduct);
                showDetailScreen(bvProduct);
            }
        });
        recyclerView.setAdapter(demoProductAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userActionsListener.loadRecommendationProducts(true);
            }
        });
        noRecsFoundTextView = (TextView) view.findViewById(R.id.no_recs_found);
        getRecsProgressBar = (ProgressBar) view.findViewById(R.id.get_recs_progress);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        userActionsListener = new DemoRecommendationsPresenter(this, DemoConfigUtils.getInstance(getContext()), DemoDataUtil.getInstance(getContext()), recyclerView);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        userActionsListener.onResume();
        userActionsListener.loadRecommendationProducts(false);
    }

    @Override
    public void showRecommendations(List<BVProduct> recommendationProducts) {
        demoProductAdapter.setBvProducts(recommendationProducts);
        noRecsFoundTextView.setVisibility(View.GONE);
        getRecsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoRecommendationsFound() {
        demoProductAdapter.setBvProducts(Collections.<BVProduct>emptyList());
        getRecsProgressBar.setVisibility(View.GONE);
        noRecsFoundTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSwipeRefreshLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showLoading(final boolean isLoading) {
        Log.d(TAG, "showLoadingRecs: " + isLoading);
        getRecsProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotConfiguredDialog(String displayName) {
        String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_recommendations));
        String acceptButton = "Ok";
        new AlertDialog.Builder(getActivity())
            .setMessage(errorMessage)
            .setPositiveButton(acceptButton, null).create().show();
    }

    private void showDetailScreen(BVProduct bvProduct) {
        ((DemoMainActivity) getActivity()).transitionToBvProductDetail(bvProduct);
    }
}
