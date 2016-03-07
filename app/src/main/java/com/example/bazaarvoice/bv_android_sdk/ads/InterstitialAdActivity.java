/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.ads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVAdListener;
import com.bazaarvoice.bvandroidsdk.BVAds;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.AppConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.AppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.UserConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

/**
 * TODO: Description Here
 */
public class InterstitialAdActivity extends AppCompatActivity {
    private Button interstitialButton;

    private PublisherInterstitialAd publisherInterstitialAd;
    private PublisherAdRequest.Builder targetedAdRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        interstitialButton = (Button) findViewById(R.id.interstitial_button);

        AppConfiguration appConfiguration = AppConfigurationImpl.getInstance();
        BVAds bvAds = appConfiguration.provideBvAds();
        UserConfiguration userConfiguration = appConfiguration.provideBvUserComponent();
        String bvAdUnitId = userConfiguration.provideInterstitialAdUnitId();

        // get a PublisherAdView object from the BVAdsSDK
        publisherInterstitialAd = bvAds.getTargetedInterstitialAd(getApplicationContext(), bvAdUnitId);

        // optional: set a BVAdListener to track the lifecycle of the ad
        publisherInterstitialAd.setAdListener(new BVAdListener(bvAds, publisherInterstitialAd) {
            @Override
            public void bvOnAdOpened() {
                Toast.makeText(getApplication(), "BV Ad Opened", Toast.LENGTH_SHORT).show();
            }
        });

        // get the PublisherAdRequest that BVAdsSDK has set targeting information on.
        // provided as the Builder, to allow interacting with it further to set other information
        // like gender, birthday, etc.
        targetedAdRequest = bvAds.getTargetedAdRequest();
        String testDeviceId = userConfiguration.provideTestDeviceId();
        if (testDeviceId != null && !testDeviceId.equals("REPLACE_ME")) {
            targetedAdRequest.addTestDevice(testDeviceId);
        }

        // Add deviceId for emulator
        // You can also add your own for a specific hardware device
        targetedAdRequest.addTestDevice(PublisherAdRequest.DEVICE_ID_EMULATOR);
    }

    @Override
    public void onResume() {
        super.onResume();

        // load the ad!
        publisherInterstitialAd.loadAd(targetedAdRequest.build());

        // Show the ad when it is loaded, and we click the button
        interstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publisherInterstitialAd.isLoaded()) {
                    publisherInterstitialAd.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Still loading Google Ad", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}