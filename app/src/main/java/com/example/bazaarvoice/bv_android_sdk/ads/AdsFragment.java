/**
 * A simple {@link Fragment} subclass.
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.ads;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bazaarvoice.bv_android_sdk.DemoConstants;
import com.example.bazaarvoice.bv_android_sdk.DemoMainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.utils.DemoConfig;
import com.example.bazaarvoice.bv_android_sdk.utils.DemoConfigUtils;

/**
 * TODO: Description Here
 */
public class AdsFragment extends Fragment {


    public AdsFragment() {
        // Required empty public constructor
    }

    public static AdsFragment newInstance() {
        AdsFragment fragment = new AdsFragment();
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readyForDemo();
    }

    private boolean readyForDemo() {
        DemoConfig currentConfig = DemoConfigUtils.getInstance(getContext()).getCurrentConfig();
        String shopperAdKey = currentConfig.apiKeyShopperAdvertising;
        String displayName = currentConfig.displayName;

        if (!DemoConstants.isSet(shopperAdKey)) {
            String errorMessage = String.format(getString(R.string.view_demo_error_message), displayName, getString(R.string.demo_recommendations));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok", null).create().show();
            return false;
        }

        return true;
    }

}
