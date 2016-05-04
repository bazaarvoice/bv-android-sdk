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

import com.example.bazaarvoice.bv_android_sdk.DemoMainActivity;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfigurationImpl;

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

        readyForDemo();

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

    private boolean readyForDemo() {
        DemoUserConfiguration demoUserConfiguration = DemoAppConfigurationImpl.getInstance().provideBvUserComponent();
        String shopperAdKey = demoUserConfiguration.provideApiKeyShopperAdvertising();
        String clientId = demoUserConfiguration.provideBvClientId();

        String errorVal = null;
        if (shopperAdKey.equals(DemoUserConfigurationImpl.REPLACE_ME)) {
            errorVal = "SHOPPER_ADVERTISING_API_KEY";
        } else if (clientId.equals(DemoUserConfigurationImpl.REPLACE_ME)) {
            errorVal = "BV_CLIENT_ID";
        }

        if (errorVal != null) {
            String errorMessage = String.format(getResources().getString(R.string.view_demo_error_message), errorVal);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNegativeButton("Ok",null).create().show();
            return false;
        }

        return true;
    }
}
