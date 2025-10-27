package com.bazaarvoice.bvsdkdemoandroid.productsentiments;

import android.util.Log;
import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVProductSentimentsClient;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentRequest;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentResponse;
import com.bazaarvoice.bvandroidsdk.ProductSentimentsCallback;
import com.bazaarvoice.bvandroidsdk.ProductSentimentsException;
import com.bazaarvoice.bvandroidsdk.SummarisedFeaturesRequest;
import com.bazaarvoice.bvandroidsdk.SummarisedFeaturesResponse;

import javax.inject.Inject;
import javax.inject.Named;

public class DemoProductSentimentsPresenter implements DemoProductSentimentsContract.Presenter {
    private static final String TAG = "DemoPSPresenter";

    private final DemoProductSentimentsContract.View view;
    private final String productId;
    private final BVProductSentimentsClient bvProductSentimentsClient;

    @Inject
    DemoProductSentimentsPresenter(
            DemoProductSentimentsContract.View view,
            @Named("ProductId") String productId,
            BVProductSentimentsClient bvProductSentimentsClient
    ) {
        this.view = view;
        this.productId = productId;
        this.bvProductSentimentsClient = bvProductSentimentsClient;
    }

    @Override
    public void start() {
        loadSummarisedFeatures();
        loadFeaturesSentiment();
    }

    private void loadSummarisedFeatures() {
        SummarisedFeaturesRequest request = new SummarisedFeaturesRequest.Builder(productId)
                .addLanguage("en")
                .addEmbed("quotes")
                .build();
        bvProductSentimentsClient.prepareCall(request).loadAsync(new ProductSentimentsCallback<SummarisedFeaturesResponse>() {
            @Override
            public void onSuccess(@NonNull SummarisedFeaturesResponse response) {
                view.showSummarisedFeatures(response);
            }

            @Override
            public void onFailure(@NonNull ProductSentimentsException e) {
                //Log.e(TAG, "Failed to load summarised features", e);
                view.showSummarisedFeaturesError();
            }
        });
    }

    private void loadFeaturesSentiment() {
        FeaturesSentimentRequest request = new FeaturesSentimentRequest.Builder(productId)
                .addLanguage("en")
                .addLimit("10")
                .build();
        bvProductSentimentsClient.prepareCall(request).loadAsync(new ProductSentimentsCallback<FeaturesSentimentResponse>() {
            @Override
            public void onSuccess(@NonNull FeaturesSentimentResponse response) {
                view.showFeaturesSentiment(response);
            }

            @Override
            public void onFailure(@NonNull ProductSentimentsException e) {
                Log.e(TAG, "Failed to load features sentiment", e);
                view.showFeaturesSentimentError();
            }
        });
    }

    @Inject
    void setupListeners() {
        view.setPresenter(this);
    }
}