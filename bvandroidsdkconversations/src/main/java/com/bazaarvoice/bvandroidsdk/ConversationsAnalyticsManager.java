/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.types.RequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Internal API wrapper around {@code AnalyticsManger}
 * for sending Conversations analytics events
 */
class ConversationsAnalyticsManager {

    public static void sendDisplayAnalyticsEvent(RequestType requestType, JSONObject response) {
        Set<String> productIdSet = new HashSet<>();

        try {
            JSONArray results = response.getJSONArray("Results");

            if (results == null || results.length() == 0) {
                return;
            }

            for (int i=0; i<results.length(); i++) {
                JSONObject resultJsonObject = results.getJSONObject(i);
                Map<String, Object> resultInfo = getResultInfo(requestType, resultJsonObject);
                if (resultInfo == null) {
                    return;
                }

                sendUgcImpressionEvent(resultInfo);

                String productId = (String) resultInfo.get("productId");
                String categoryId = (String) resultInfo.get("categoryId");
                String brand = (String) resultInfo.get("brand");
                if (!productIdSet.contains(productId)) {
                    productIdSet.add(productId);
                    sendProductPageViewEvent(productId, categoryId, brand);
                }
            }

        } catch (Exception e) {
            BazaarException bazaarException = new BazaarException("Error sending display event", e);
            bazaarException.printStackTrace();
        }
    }

    public static void sendSubmissionAnalyticsEvent(RequestType requestType, BazaarParams bazaarParams) {
        SubmitUsedFeatureSchema.SubmitFeature submitFeature = getSubmitFeatureFromRequestType(requestType);

        if (submitFeature == null) {
            return;
        }

        boolean isFingerprintUsed = false;
        String productId = null;
        String categoryId = null;
        if (bazaarParams != null) {
            if (bazaarParams instanceof SubmissionParams) {
                SubmissionParams submissionParams = (SubmissionParams) bazaarParams;
                String fingerprint = submissionParams.getFingerprint();
                isFingerprintUsed = fingerprint != null && !fingerprint.isEmpty();

                productId = submissionParams.getProductId();
                categoryId = submissionParams.getCategoryId();
            }
        }

        AnalyticsManager analyticsManager = BVSDK.getInstance().getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        SubmitUsedFeatureSchema schema = new SubmitUsedFeatureSchema.Builder(submitFeature, magpieMobileAppPartialSchema)
                .fingerprint(isFingerprintUsed)
                .build();

        schema.addKeyVal("productId", productId);
        schema.addKeyVal("categoryId", categoryId);

        analyticsManager.enqueueEvent(schema);
    }

    private static SubmitUsedFeatureSchema.SubmitFeature getSubmitFeatureFromRequestType(RequestType requestType) {
        SubmitUsedFeatureSchema.SubmitFeature submitFeature = null;

        switch (requestType) {
            case ANSWERS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.ANSWER;
                break;
            case REVIEW_COMMENTS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.COMMENT;
                break;
            case STORY_COMMENTS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.STORY_COMMENT;
                break;
            case FEEDBACK:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.FEEDBACK;
                break;
            case QUESTIONS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.ASK;
                break;
            case REVIEWS:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.WRITE;
                break;
            case STORIES:
                submitFeature = SubmitUsedFeatureSchema.SubmitFeature.STORY;
                break;
        }

        return submitFeature;
    }

    private static Map<String, Object> getResultInfo(RequestType requestType, JSONObject response) throws JSONException {
        Map<String, Object> eventMap = new HashMap<String, Object>();

        switch (requestType) {
            case REVIEWS:
                eventMap.put("contentType", "Review");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("visible", false);
                eventMap.put("productId", response.getString("ProductId"));
                break;
            case QUESTIONS:
                eventMap.put("contentType", "IncludeBase");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("ProductId"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                break;
            case PRODUCTS:
                eventMap.put("contentType", "Product");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("Id"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                JSONObject brandJsonObject = response.getJSONObject("Brand");
                if (brandJsonObject != null) {
                    String brand = brandJsonObject.getString("Name");
                    if (brand != null) {
                        eventMap.put("brand", brand);
                    }
                }
                break;
            case STATISTICS:
                eventMap.put("contentType", "Statistic");
                eventMap.put("contentId", "");
                eventMap.put("productId", response.getJSONObject("Statistics").getString("ProductId"));
                break;
            case STORY_COMMENTS:
                eventMap.put("contentType", "Comment");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case CATEGORIES:
                eventMap.put("contentType", "Category");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case STORIES:
                eventMap.put("contentType", "Story");
                eventMap.put("contentId", response.getString("Id"));
                break;
            case ANSWERS:
                eventMap.put("contentType", "Answer");
                eventMap.put("contentId", response.getString("Id"));
                break;
            default:
                break;
        }

        return eventMap;
    }

    private static void sendUgcImpressionEvent(Map<String, Object> dataMap) {
        BVSDK bvsdk = BVSDK.getInstance();
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        String productId = (String) dataMap.get("productId");
        String bvProduct = (String) dataMap.get("bvProduct");
        UgcImpressionSchema schema = new UgcImpressionSchema(magpieMobileAppPartialSchema, productId, bvProduct);
        analyticsManager.enqueueEvent(schema);
    }

    private static void sendProductPageViewEvent(String productId, String categoryId, String brand) {
        BVSDK bvsdk = BVSDK.getInstance();
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        ProductPageViewSchema schema = new ProductPageViewSchema(productId, categoryId, brand, magpieMobileAppPartialSchema);
        analyticsManager.enqueueEvent(schema);
    }

}
