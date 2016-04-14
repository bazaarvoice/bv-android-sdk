/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.example.bazaarvoice.bv_android_sdk.DemoMainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.example.bazaarvoice.bv_android_sdk.utils.DividerItemDecoration;

import java.util.List;

/**
 * TODO: Description Here
 */
public class DemoRecommendationsFragment extends Fragment implements DemoRecommendationsContract.View {

    private DemoRecommendationsContract.UserActionsListener userActionsListener;

    private DemoProductAdapter demoProductAdapter;
    private RecommendationsRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static DemoRecommendationsFragment newInstance() {
        return new DemoRecommendationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recommendations_raw, container, false);

        checkForDemoInput();

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

        return view;
    }

    private void checkForDemoInput() {
        DemoUserConfiguration demoUserConfiguration = DemoAppConfigurationImpl.getInstance().provideBvUserComponent();
        String shopperAdKey = demoUserConfiguration.provideApiKeyShopperAdvertising();
        String clientId = demoUserConfiguration.provideBvClientId();

        String errorVal = null;
        if (shopperAdKey.equals("REPLACE_ME")) {
            errorVal = "shopperAdKey";
        } else if (clientId.equals("REPLACE_ME")) {
            errorVal = "clientId";
        }

        if (errorVal != null) {
            String errorMessage = String.format(getResources().getString(R.string.view_demo_error_message), errorVal);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        userActionsListener = new DemoRecommendationsPresenter(this);
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
    }

    @Override
    public void showLoading(final boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showDetailScreen(BVProduct bvProduct) {
        ((DemoMainActivity) getActivity()).transitionToBvProductDetail(bvProduct);
    }
}
