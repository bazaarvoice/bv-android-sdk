/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.HashMap;
import java.util.Map;

/**
 * Helper class that wraps {@code AnalyticsManger}, builds and enqueues
 * Conversations specific Analytic events.
 */
class ProductSentimentsAnalyticsManager {
    // region Properties
    private final BVPixel bvPixel;
    private final String clientId;
    // endregion

    // region Constructor

    ProductSentimentsAnalyticsManager(BVPixel bvPixel, String clientId) {
        this.bvPixel = bvPixel;
        this.clientId = clientId;
    }

    // endregion

    // region API

    /**
     * Route all Display API Responses here to dispatch events
     *
     * @param
     */

    private void sendFeatureUsedEvent(String productId, BVEventValues.BVProductType bvProductType, BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType) {
        BVFeatureUsedEvent featureUsedEvent = new BVFeatureUsedEvent(productId, bvProductType, bvFeatureUsedEventType, null);
        bvPixel.track(featureUsedEvent);
    }




    /**
     * Route non-response dependent Used-Feature events dispatching here
     *
     * @param productId
     * @param containerId
     * @param bvProductType
     */
    public void sendUsedFeatureInViewEvent(String productId, String containerId, BVEventValues.BVProductType bvProductType) {
        // TODO: Add Integration Test for all the Views calling this
        if (productId == null || containerId == null) {
            return;
        }
        BVInViewEvent event = new BVInViewEvent(productId, containerId, bvProductType, null);
        bvPixel.trackEventForClient(event, clientId);
    }

    /**
     * Route non-response dependent Used-Feature events dispatching here
     *
     * @param productId
     * @param bvProductType
     */
    public void sendUsedFeatureScrolledEvent(String productId, BVEventValues.BVProductType bvProductType) {
        productId = productId == null ? "" : productId;
        BVFeatureUsedEvent event = new BVFeatureUsedEvent(
                productId,
                bvProductType,
                BVEventValues.BVFeatureUsedEventType.SCROLLED,
                null);
        bvPixel.trackEventForClient(event, clientId);
    }

    // endregion

    // region Helper Methods

    private void sendUgcImpressionEvent(String productId, String contentId, BVEventValues.BVProductType bvProductType, BVEventValues.BVImpressionContentType bvImpressionContentType, String categoryId, String brand) {
        BVImpressionEvent event = new BVImpressionEvent(
                productId, contentId, bvProductType, bvImpressionContentType, categoryId, brand);
        bvPixel.trackEventForClient(event, clientId);
    }

  


    private void sendUsedFeatureUgcContentSubmission(
            @NonNull BVEventValues.BVProductType bvProductType,
            @NonNull BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType,
            @Nullable String productId, boolean hasFingerprint) {
        sendUsedFeatureUgcContentSubmission(bvProductType, bvFeatureUsedEventType, productId, hasFingerprint, null, null);
    }

    private void sendUsedFeatureUgcContentSubmission(
            @NonNull BVEventValues.BVProductType bvProductType,
            @NonNull BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType,
            @Nullable String productId, boolean hasFingerprint,
            @Nullable String contentId,
            @Nullable String contentType) {
        productId = productId == null ? "none" : productId;

        BVFeatureUsedEvent event = new BVFeatureUsedEvent(productId, bvProductType, bvFeatureUsedEventType, null);

        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put(BVEventKeys.FeatureUsedEvent.HAS_FINGERPRINT, hasFingerprint);

        if (contentId != null) {
            extraParams.put(BVEventKeys.FeatureUsedEvent.CONTENT_ID, contentId);
        }
        if (contentType != null) {
            extraParams.put(BVEventKeys.FeatureUsedEvent.BV_CONTENT_TYPE, contentType);
        }

        event.setAdditionalParams(extraParams);

        bvPixel.trackEventForClient(event, clientId);
    }

  

    private void sendUsedFeatureDisplayAuthor(String profileId) {
        String productId = "none"; // TODO This matches iOS but seems wrong
        BVEventValues.BVProductType bvProductType = BVEventValues.BVProductType.CONVERSATIONS_PROFILE;
        BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType = BVEventValues.BVFeatureUsedEventType.PROFILE;
        BVFeatureUsedEvent event = new BVFeatureUsedEvent(productId, bvProductType, bvFeatureUsedEventType, null);

        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("interaction", false);
        extraParams.put("page", profileId);
        event.setAdditionalParams(extraParams);

        bvPixel.trackEventForClient(event, clientId);
    }

    //BVPixel for reviewHighlights API
    private void sendUsedFeatureEventReviewHighlights(String productId){
        BVEventValues.BVProductType bvProductType = BVEventValues.BVProductType.CONVERSATIONS_REVIEWS;
        BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType = BVEventValues.BVFeatureUsedEventType.REVIEWHIGHLIGHTS;
        BVFeatureUsedEvent event = new BVFeatureUsedEvent(productId, bvProductType, bvFeatureUsedEventType, null);
        bvPixel.trackEventForClient(event, clientId);
    }
    // endregion
}
