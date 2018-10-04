/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.recommendations;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.cart.DemoCart;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.utils.DividerItemDecoration;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.bazaarvoice.bvsdkdemoandroid.utils.DemoRequiredKeyUiUtil.getNoReccosApiKeyDialog;

public class DemoRecommendationsFragment extends Fragment implements DemoRecommendationsContract.View {
    private static final String TAG = DemoRecommendationsFragment.class.getSimpleName();
    private DemoRecommendationsContract.UserActionsListener userActionsListener;
    private DemoProductAdapter demoProductAdapter;
    private RecommendationsRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noRecsFoundTextView;
    private ProgressBar getRecsProgressBar;

    @Inject DemoClient demoClient;
    @Inject DemoMockDataUtil demoMockDataUtil;

    public static DemoRecommendationsFragment newInstance() {
        return new DemoRecommendationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApp.getAppComponent(getContext()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recommendations_raw, container, false);
        recyclerView = view.findViewById(R.id.recommendations_custom_list);
        demoProductAdapter = new DemoProductAdapter();
        demoProductAdapter.setOnItemClickListener((bvProduct, rowView) -> {
            userActionsListener.onRecommendationProductTapped(bvProduct);
            showDetailScreen(bvProduct);
        });
        demoProductAdapter.setAddProductToCartLister(product -> DemoCart.INSTANCE.addProduct(product));

        recyclerView.setAdapter(demoProductAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> userActionsListener.loadRecommendationProducts(true));
        noRecsFoundTextView = view.findViewById(R.id.no_recs_found);
        getRecsProgressBar = view.findViewById(R.id.get_recs_progress);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        userActionsListener = new DemoRecommendationsPresenter(this, demoClient, demoMockDataUtil, recyclerView);
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
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
        if (getActivity() == null || !isAdded()) { return; }
        getNoReccosApiKeyDialog(getContext(), displayName).show();
    }
    private void showDetailScreen(BVProduct bvProduct) {
        ((DemoMainActivity) getActivity()).transitionToBvProductDetail(bvProduct);
    }
}