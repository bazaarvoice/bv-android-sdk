/**
 * A simple {@link Fragment} subclass.
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.ads;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bazaarvoice.bv_android_sdk.DemoMainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;

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

        checkForDemoInput();

        Button nativeBtn = (Button) view.findViewById(R.id.nativeAdBtn);
        nativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DemoMainActivity)getActivity()).transitionToNativeAd();
            }
        });

        Button interstitialBtn = (Button) view.findViewById(R.id.interstitialAdBtn);
        interstitialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DemoMainActivity)getActivity()).transitionToInterstitialAd();
            }
        });

        Button bannerBtn = (Button) view.findViewById(R.id.bannerAdBtn);
        bannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DemoMainActivity)getActivity()).transitionToBannerAd();
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


}
