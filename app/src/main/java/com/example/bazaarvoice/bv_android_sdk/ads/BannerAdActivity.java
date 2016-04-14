/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.ads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVAdListener;
import com.bazaarvoice.bvandroidsdk.BVAds;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

/**
 * TODO: Description Here
 */
public class BannerAdActivity extends AppCompatActivity {
    private PublisherAdView publisherAdView;
    private PublisherAdRequest.Builder targetedAdRequest;

    private TextView bannerAdLoading, bannerAdSuccess, bannerAdFailure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bannerAdLoading = (TextView) findViewById(R.id.banner_ad_loading);
        bannerAdSuccess = (TextView) findViewById(R.id.banner_ad_success);
        bannerAdFailure = (TextView) findViewById(R.id.banner_ad_failure);

        DemoAppConfiguration demoAppConfiguration = DemoAppConfigurationImpl.getInstance();
        BVAds bvAds = demoAppConfiguration.provideBvAds();
        DemoUserConfiguration demoUserConfiguration = demoAppConfiguration.provideBvUserComponent();
        String bvAdUnitId = demoUserConfiguration.provideBannerAdUnitId();

        // get a PublisherAdView object from the BVAdsSDK
        publisherAdView = bvAds.getTargetedAdView(getApplicationContext());

        // setup the PublisherAdView as you normally would - setting the adUnitId and desired ad size.
        // in this case, a 320x100 Large Banner ad is requested from DFP
        publisherAdView.setAdUnitId(bvAdUnitId);
        publisherAdView.setAdSizes(AdSize.SMART_BANNER);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        publisherAdView.setLayoutParams(layoutParams);

        // optional: set a BVAdListener to track the lifecycle of the ad
        publisherAdView.setAdListener(new BVAdListener(bvAds, publisherAdView) {
            @Override
            public void bvOnAdLoaded() {
                showSuccess();
            }

            @Override
            public void bvOnAdFailedToLoad(int errorCode) {
                showFailure();
            }
        });

        // get the PublisherAdRequest that BVAdsSDK has set targeting information on.
        // provided as the Builder, to allow interacting with it further to set other information
        // like gender, birthday, etc.
        targetedAdRequest = bvAds.getTargetedAdRequest();

        // Add deviceId for emulator
        // You can also add your own for a specific hardware device
        targetedAdRequest.addTestDevice(PublisherAdRequest.DEVICE_ID_EMULATOR);
        String testDeviceId = demoUserConfiguration.provideTestDeviceId();
        if (testDeviceId != null && !testDeviceId.equals("REPLACE_ME")) {
            targetedAdRequest.addTestDevice(testDeviceId);
        }

        // Ad Load View to View Hierarchy
        FrameLayout view = (FrameLayout) findViewById(R.id.banner_ad_view);
        view.addView(publisherAdView);

        showLoading();

        // load the ad!
        publisherAdView.loadAd(targetedAdRequest.build());

    }

    @Override
    public void onResume() {
        super.onResume();

        // Resume the PublisherAdView.
        publisherAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the PublisherAdView.
        publisherAdView.pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the PublisherAdView.
        publisherAdView.destroy();

        super.onDestroy();
    }

    private void showLoading() {
        bannerAdLoading.setVisibility(View.VISIBLE);
        bannerAdSuccess.setVisibility(View.GONE);
        bannerAdFailure.setVisibility(View.GONE);
    }

    private void showSuccess() {
        bannerAdLoading.setVisibility(View.GONE);
        bannerAdSuccess.setVisibility(View.VISIBLE);
        bannerAdFailure.setVisibility(View.GONE);    }

    private void showFailure() {
        bannerAdLoading.setVisibility(View.GONE);
        bannerAdSuccess.setVisibility(View.GONE);
        bannerAdFailure.setVisibility(View.VISIBLE);
    }
}
