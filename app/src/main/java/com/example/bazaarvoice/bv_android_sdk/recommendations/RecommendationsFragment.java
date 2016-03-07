/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.recommendations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.example.bazaarvoice.bv_android_sdk.MainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.recommendations.data.BvRepository;
import com.example.bazaarvoice.bv_android_sdk.di.AppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.UserConfiguration;

import java.util.List;

/**
 * TODO: Description Here
 */
public class RecommendationsFragment extends Fragment implements RecommendationsContract.View {

    private RecommendationsContract.UserActionsListener userActionsListener;

    private BvProductAdapter bvProductAdapter;
    private RecyclerView recyclerView;
    private TextView failMessage;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static RecommendationsFragment newInstance() {
        return new RecommendationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recommendations_raw, container, false);

        checkForDemoInput();

        recyclerView = (RecyclerView) view.findViewById(R.id.recommendations_custom_list);
        bvProductAdapter = new BvProductAdapter();
        bvProductAdapter.setOnItemClickListener(new BvProductAdapter.OnBvProductClickListener() {
            @Override
            public void onBvProductClickListener(BVProduct bvProduct, View rowView) {
                userActionsListener.onRecommendationProductTapped(bvProduct);
                showDetailScreen(bvProduct.getProductId());
            }
        });
        recyclerView.setAdapter(bvProductAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        failMessage = (TextView) view.findViewById(R.id.recommendations_fail_message);

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
        UserConfiguration userConfiguration = AppConfigurationImpl.getInstance().provideBvUserComponent();
        String shopperAdKey = userConfiguration.provideApiKeyShopperAdvertising();
        String clientId = userConfiguration.provideBvClientId();

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
        BvRepository bvRepository = AppConfigurationImpl.getInstance().provideBvRepository();
        userActionsListener = new RecommendationsPresenter(this, bvRepository);
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
        bvProductAdapter.setBvProducts(recommendationProducts);
        recyclerView.setVisibility(View.VISIBLE);
        failMessage.setVisibility(View.GONE);
    }

    @Override
    public void showLoading(final boolean isLoading) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isLoading);
            }
        });
    }

    @Override
    public void showNoRecommendations(String message) {
        failMessage.setText(message);
        recyclerView.setVisibility(View.GONE);
        failMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showDetailScreen(String bvProductId) {
        ((MainActivity) getActivity()).transitionToBvProductDetail(bvProductId);
    }
}
