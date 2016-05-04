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

import com.bazaarvoice.bvandroidsdk.BVAds;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfigurationImpl;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.Map;

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

        DemoAppConfiguration demoAppConfiguration = DemoAppConfigurationImpl.getInstance();
        DemoUserConfiguration demoUserConfiguration = demoAppConfiguration.provideBvUserComponent();
        String demoAdUnitId = demoUserConfiguration.provideInterstitialAdUnitId();

        // get a PublisherAdView object from the BVAdsSDK
        publisherInterstitialAd = new PublisherInterstitialAd(this);
        publisherInterstitialAd.setAdUnitId(demoAdUnitId);

        // optional: set a BVAdListener to track the lifecycle of the ad
        publisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Toast.makeText(getApplication(), "BV Ad Opened", Toast.LENGTH_SHORT).show();
            }
        });

        // Add Bazaarvoice targeting keywords
        targetedAdRequest = new PublisherAdRequest.Builder();
        Map<String, String> targetingKeywords = BVAds.getCustomTargeting();
        for (Map.Entry<String, String> entry : targetingKeywords.entrySet()) {
            targetedAdRequest.addCustomTargeting(entry.getKey(), entry.getValue());
        }
        String testDeviceId = demoUserConfiguration.provideTestDeviceId();
        if (testDeviceId != null && !testDeviceId.equals(DemoUserConfigurationImpl.REPLACE_ME)) {
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