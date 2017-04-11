/**
 * A simple {@link Fragment} subclass.
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.ads;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoMainActivity;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;

import javax.inject.Inject;

public class DemoAdFragment extends Fragment {

    @Inject DemoClient demoClient;

    public static DemoAdFragment newInstance() {
        DemoAdFragment fragment = new DemoAdFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_ads, container, false);


        Button nativeBtn = (Button) view.findViewById(R.id.nativeAdBtn);
        nativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToNativeAd();
                }
            }
        });

        Button interstitialBtn = (Button) view.findViewById(R.id.interstitialAdBtn);
        interstitialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToInterstitialAd();
                }
            }
        });

        Button bannerBtn = (Button) view.findViewById(R.id.bannerAdBtn);
        bannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyForDemo()) {
                    ((DemoMainActivity) getActivity()).transitionToBannerAd();
                }
            }
        });

        DemoApp.get(getContext()).getAppComponent().inject(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {
        String shopperAdKey = demoClient.getApiKeyShopperAdvertising();
        String displayName = demoClient.getDisplayName();

        if (!DemoConstants.isSet(shopperAdKey)) {
            String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_ads));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok", null).create().show();
            return false;
        }

        return true;
    }

}
