/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvsdkdemoandroid.ads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVAds;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.List;
import java.util.Map;

public class NativeAdActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView loadFailed;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = (ProgressBar) findViewById(R.id.native_ad_loading);
        loadFailed = (TextView) findViewById(R.id.native_ad_load_failed);
        cardView = (CardView) findViewById(R.id.native_ad_card);

        showLoading();

        final NativeContentAdView nativeContentAdView = (NativeContentAdView) findViewById(R.id.native_content_ad_view);
        final ImageView imageView = (ImageView) findViewById(R.id.nativeAdImage);
        final TextView title = (TextView) findViewById(R.id.nativeAdTitle);
        final TextView description = (TextView) findViewById(R.id.nativeAdDescription);
        final TextView link = (TextView) findViewById(R.id.nativeAdLink);

        String adUnitId = DemoConstants.NATIVE_CONTENT_AD_UNIT_ID;

        AdLoader.Builder targetedAdBuilder = new AdLoader.Builder(this, adUnitId);

        targetedAdBuilder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd nativeContentAd) {
                List<NativeAd.Image> images = nativeContentAd.getImages();

                if(images != null && images.size() > 0) {
                    imageView.setImageDrawable(images.get(0).getDrawable());
                }

                nativeContentAdView.setHeadlineView(title);
                title.setText(nativeContentAd.getHeadline());

                nativeContentAdView.setBodyView(description);
                description.setText(nativeContentAd.getBody());

                nativeContentAdView.setCallToActionView(link);
                link.setText(nativeContentAd.getCallToAction());

                nativeContentAdView.setNativeAd(nativeContentAd);

                showLoadSucceeded();
            }
        });

        AdLoader adLoader = targetedAdBuilder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.e("error", "ad failed to prepareCall");
                showToast("Ad failed to prepareCall");
                showLoadFailed();
            }
        }).build();

        // Add Bazaarvoice targeting keywords
        PublisherAdRequest.Builder publisherAdRequest = new PublisherAdRequest.Builder();
        Map<String, String> targetingKeywords = BVAds.getCustomTargeting();
        for (Map.Entry<String, String> entry : targetingKeywords.entrySet()) {
            publisherAdRequest.addCustomTargeting(entry.getKey(), entry.getValue());
        }

        // Add deviceId for emulator
        // You can also add your own for a specific hardware device
        publisherAdRequest.addTestDevice(PublisherAdRequest.DEVICE_ID_EMULATOR);
        String testDeviceId = DemoConstants.DFP_TEST_DEVICE_ID;
        if (DemoConstants.isSet(testDeviceId)) {
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

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}