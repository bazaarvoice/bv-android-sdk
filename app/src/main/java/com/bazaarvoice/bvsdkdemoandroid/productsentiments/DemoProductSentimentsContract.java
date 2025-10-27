package com.bazaarvoice.bvsdkdemoandroid.productsentiments;

import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBaseView;

// Add these imports for the new response types
import com.bazaarvoice.bvandroidsdk.SummarisedFeaturesResponse;
import com.bazaarvoice.bvandroidsdk.FeaturesSentimentResponse;

public interface DemoProductSentimentsContract {

    interface View extends DemoBaseView<Presenter> {
        // Remove old author/review methods if not needed

        // Add new methods for the new APIs
        void showSummarisedFeatures(SummarisedFeaturesResponse response);
        void showSummarisedFeaturesError();
        void showFeaturesSentiment(FeaturesSentimentResponse response);
        void showFeaturesSentimentError();
    }

    interface Presenter extends DemoBasePresenter {
        // No changes needed here
    }
}