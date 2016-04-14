/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.example.bazaarvoice.bv_android_sdk.ads;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.AdTracker;
import com.bazaarvoice.bvandroidsdk.BVAdListener;
import com.bazaarvoice.bvandroidsdk.BVAds;
import com.bazaarvoice.bvandroidsdk.BVContentAdLoadedListener;
import com.example.bazaarvoice.bv_android_sdk.R;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfiguration;
import com.example.bazaarvoice.bv_android_sdk.di.DemoAppConfigurationImpl;
import com.example.bazaarvoice.bv_android_sdk.di.DemoUserConfiguration;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;

import java.util.List;

/**
 * TODO: Description Here
 */
public class NativeAdActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView loadFailed;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.native_ad_loading);
        loadFailed = (TextView) findViewById(R.id.native_ad_load_failed);
        cardView = (CardView) findViewById(R.id.native_ad_card);

        showLoading();

        final ImageView imageView = (ImageView) findViewById(R.id.nativeAdImage);
        final TextView title = (TextView) findViewById(R.id.nativeAdTitle);
        final TextView description = (TextView) findViewById(R.id.nativeAdDescription);
        final TextView link = (TextView) findViewById(R.id.nativeAdLink);

        DemoAppConfiguration demoAppConfiguration = DemoAppConfigurationImpl.getInstance();
        BVAds bvAds = demoAppConfiguration.provideBvAds();
        DemoUserConfiguration demoUserConfiguration = demoAppConfiguration.provideBvUserComponent();
        String adUnitId = demoUserConfiguration.provideNativeContentAdUnitId();

        AdLoader.Builder targetedAdBuilder = bvAds.getTargetedAdLoader(getApplicationContext(), adUnitId);

        targetedAdBuilder.forContentAd(new BVContentAdLoadedListener(bvAds, adUnitId) {
            @Override
            public void bvOnContentAdLoaded(NativeContentAd nativeContentAd) {
                super.bvOnContentAdLoaded(nativeContentAd);

                List<NativeAd.Image> images = nativeContentAd.getImages();

                if(images != null && images.size() > 0) {
                    imageView.setImageDrawable(images.get(0).getDrawable());
                }

                title.setText(nativeContentAd.getHeadline());
                description.setText(nativeContentAd.getBody());
                link.setText(nativeContentAd.getCallToAction());

                final String advertUrl = "http://" + String.valueOf(nativeContentAd.getAdvertiser());

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        conversionEvent(advertUrl);
                    }
                });

                showLoadSucceeded();
            }
        });

        AdLoader adLoader = targetedAdBuilder.withAdListener(new BVAdListener(bvAds, adUnitId, AdTracker.AdTrackerType.NATIVE) {
            @Override
            public void bvOnAdFailedToLoad(int errorCode) {
                Log.e("error", "ad failed to load");
                showToast("Ad failed to load");
                showLoadFailed();
            }
        }).build();

        PublisherAdRequest.Builder publisherAdRequest = bvAds.getTargetedAdRequest();

        // Add deviceId for emulator
        // You can also add your own for a specific hardware device
        publisherAdRequest.addTestDevice(PublisherAdRequest.DEVICE_ID_EMULATOR);
        String testDeviceId = demoUserConfiguration.provideTestDeviceId();
        if (testDeviceId != null && !testDeviceId.equals("REPLACE_ME")) {
            publisherAdRequest.addTestDevice(testDeviceId);
        }

        adLoader.loadAd(publisherAdRequest.build());
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        loadFailed.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
    }

    private void showLoadFailed() {
        progressBar.setVisibility(View.GONE);
        loadFailed.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
    }

    private void showLoadSucceeded() {
        progressBar.setVisibility(View.GONE);
        loadFailed.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }

    private void conversionEvent(String conversionUriStr) {
        Uri conversionUri;

        try {
            conversionUri = Uri.parse(conversionUriStr);
        } catch (Exception e) {
            showToast("Invalid conversion uri");
            return;
        }

        Intent conversionIntent = new Intent(Intent.ACTION_VIEW, conversionUri);
        if (conversionIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(conversionIntent);
        }   else  {
            showToast("No Intent available to handle action");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}