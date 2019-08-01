/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.bazaarvoice.bvsdkdemoandroid.author.DemoAuthorActivity;
import com.bazaarvoice.bvsdkdemoandroid.cart.DemoCartActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.bulkratings.DemoBulkRatingsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.comments.DemoCommentsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.productstats.DemoProductStatsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.DemoReviewsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.submit.DemoSubmitReviewActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.detail.DemoCurationsDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.map.DemoCurationsMapsActivity;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoFancyProductDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.DemoProgressiveSubmissionActivity;
import com.bazaarvoice.bvsdkdemoandroid.settings.DemoSettingsActivity;

import java.util.ArrayList;

public class DemoRouter {
    private final Context currentActivityContext;

    public DemoRouter(Context currentActivityContext) {
        this.currentActivityContext = currentActivityContext;
    }

    private static void transitionToActivityWithExtras(Context fromContext, Class toActivityClass, @Nullable Bundle extras) {
        Intent intent = new Intent(fromContext, toActivityClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        fromContext.startActivity(intent);
    }

    /**
     * @deprecated TODO: Refactor to be part of instance rather than static
     *
     * @param fromContext
     * @param productId
     */
    public static void transitionToCurationsMapView(Context fromContext, String productId) {
        Bundle bundle = new Bundle();
        bundle.putString(DemoCurationsMapsActivity.EXTRA_PRODUCT_ID, productId);
        transitionToActivityWithExtras(fromContext, DemoCurationsMapsActivity.class, bundle);
    }

    /**
     * @deprecated TODO: Refactor to be part of instance rather than static
     * @param fromContext
     * @param productId
     * @param startFeedItemId
     */
    public static void transitionToCurationsFeedItem(Context fromContext, String productId, String startFeedItemId) {
        Bundle extras = new Bundle();
        extras.putString(DemoCurationsDetailActivity.KEY_PRODUCT_ITEM, productId);
        extras.putString(DemoCurationsDetailActivity.KEY_CURATIONS_ITEM_ID, startFeedItemId);
        transitionToActivityWithExtras(fromContext, DemoCurationsDetailActivity.class, extras);
    }

    public void transitionToProductDetail(String productId) {
        DemoFancyProductDetailActivity.transitionTo(currentActivityContext, productId);
    }

    public void transitionToSettings(int launchCode) {
        DemoSettingsActivity.transitionTo(currentActivityContext, launchCode);
    }

    private void transitionToActivityWithExtras(Class toActivityClass, Bundle extras) {
        transitionToActivityWithExtras(currentActivityContext, toActivityClass, extras);
    }

    public void transitionToCart() {
        DemoCartActivity.transitionTo(currentActivityContext);
    }

    public void transitionToReviewsActivity(String productId){
        Bundle bundle = new Bundle();
        bundle.putString("extra_product_id", productId);
        bundle.putBoolean("extra_force_api_load", true);
        transitionToActivityWithExtras(DemoReviewsActivity.class, bundle);
    }

    public void transitionToReviewsActivity(String filterType, String filterValue){
        Bundle bundle = new Bundle();
        bundle.putString("extra_filter_type_id", filterType);
        bundle.putString("extra_filter_value_id", filterValue);
        bundle.putBoolean("extra_force_api_load", true);
        transitionToActivityWithExtras(DemoReviewsActivity.class, bundle);
    }

    public void transitionToQuestionsActivity(String productId) {
        Bundle bundle = new Bundle();
        bundle.putString("extra_product_id", productId);
        bundle.putBoolean("extra_force_api_load", true);
        transitionToActivityWithExtras(DemoQuestionsActivity.class, bundle);
    }

    public void transitionToProductStatsActivity(String productId) {
        DemoProductStatsActivity.transitionTo(currentActivityContext, productId);
    }

    public void transitionToBulkRatingsActivity(ArrayList<String> bulkProductIds) {
        Bundle bulkExtra = new Bundle();
        bulkExtra.putSerializable("extra_bulk_product_ids", bulkProductIds);
        transitionToActivityWithExtras(DemoBulkRatingsActivity.class, bulkExtra);
    }

    public void transitionToAuthorActivity(String authorId) {
        DemoAuthorActivity.transitionTo(currentActivityContext, authorId);
    }

    public void transitionToCommentsActivity(String contentId, boolean isCommentId) {
        DemoCommentsActivity.transitionToCommentsActivity(currentActivityContext, contentId, isCommentId);
    }

    public void transitionToSubmitReviewActivity(String productId) {
        DemoSubmitReviewActivity.transitionTo(currentActivityContext, productId);
    }

    public void transitionToProgressiveSubmissionActivity(String type, String productId) {
        DemoProgressiveSubmissionActivity.transitionTo(currentActivityContext, type, productId );
    }
}
