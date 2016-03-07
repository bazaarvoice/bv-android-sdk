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

import static com.bazaarvoice.bvandroidsdk.Utils.mapPutSafe;

/**
 * Internal API wrapper around {@code AnalyticsManger}
 * for sending Conversations analytics events
 */
class ConversationsAnalyticsManager {

    public static void sendAnalyticsEvent(RequestType requestType, JSONObject response) {
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
                if (productId != null && !productIdSet.contains(productId)) {
                    productIdSet.add(productId);
                    sendProductPageViewEvent(productId);
                }
            }

        } catch (Exception e) {
            BazaarException bazaarException = new BazaarException("Error sending impression event", e);
            bazaarException.printStackTrace();
        }
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
                eventMap.put("contentType", "Question");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("ProductId"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                break;
            case PRODUCTS:
                eventMap.put("contentType", "Product");
                eventMap.put("contentId", response.getString("Id"));
                eventMap.put("productId", response.getString("Id"));
                eventMap.put("categoryId", response.getString("CategoryId"));
                break;
            case STATISTICS:
                eventMap.put("contentType", "Statistic");
                eventMap.put("contentId", "");
                eventMap.put("productId", response.getJSONObject("ProductStatistics").getString("ProductId"));
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
        mapPutSafe(dataMap, "cl", "Impression");
        mapPutSafe(dataMap, "type", "UGC");
        mapPutSafe(dataMap, "source", "bv-android-sdk");
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        magpieMobileAppPartialSchema.addPartialData(dataMap);
        analyticsManager.enqueueEvent(dataMap);
    }

    private static void sendProductPageViewEvent(String productId) {
        BVSDK bvsdk = BVSDK.getInstance();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        mapPutSafe(dataMap, "productId", productId);
        mapPutSafe(dataMap, "cl", "PageView");
        mapPutSafe(dataMap, "type", "Product");
        mapPutSafe(dataMap, "source", "bv-android-sdk");
        AnalyticsManager analyticsManager = bvsdk.getAnalyticsManager();
        MagpieMobileAppPartialSchema magpieMobileAppPartialSchema = analyticsManager.getMagpieMobileAppPartialSchema();
        magpieMobileAppPartialSchema.addPartialData(dataMap);
        analyticsManager.enqueueEvent(dataMap);
    }

}
