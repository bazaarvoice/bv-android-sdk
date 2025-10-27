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
package com.bazaarvoice.bvsdkdemoandroid.configs;

import androidx.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.BVConfig;

/**
 * Wrapper around {@link BVConfig} for adding app specific data, such
 * as a display name for the settings page, and a boolean indicating
 * whether this client should be used for real requests, or use mock data
 */
public class DemoClient {
    public static final String MOCK_DISPLAY_NAME = "(Mock) Endurance Cycles";
    public static final DemoClient EMPTY_CONFIG = new DemoClient(
            "REPLACE_ME",
            "REPLACE_ME",
            "REPLACE_ME",
            "REPLACE_ME",
            "REPLACE_ME",
            "00000000-0000-0000-0000-000000000000",
            "REPLACE_ME",
            "REPLACE_ME",
            MOCK_DISPLAY_NAME,
            false);
    private String apiKeyConversations;
    private String apiKeyConversationsStores;
    private String apiKeyCurations;
    private String apiKeyLocationAndroid;
    private String apiKeyShopperAdvertising;
    private String apiKeyProgressiveSubmission;
    private String apiKeyProductSentiments;
    private String clientId;
    private String displayName;
    private boolean dryRunAnalytics;

    private DemoClient(String apiKeyConversations,
                       String apiKeyConversationsStores,
                       String apiKeyCurations,
                       String apiKeyShopperAdvertising,
                       String apiKeyProgressiveSubmission,
                       String apiKeyLocationAndroid,
                       String apiKeyProductSentiments,
                       String clientId,
                       String displayName,
                       boolean dryRunAnalytics) {
        this.apiKeyConversations = apiKeyConversations;
        this.apiKeyConversationsStores = apiKeyConversationsStores;
        this.apiKeyCurations = apiKeyCurations;
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.apiKeyProgressiveSubmission = apiKeyProgressiveSubmission;
        this.clientId = clientId;
        this.displayName = displayName;
        this.apiKeyLocationAndroid = apiKeyLocationAndroid;
        this.apiKeyProductSentiments = apiKeyProductSentiments;
        this.dryRunAnalytics = dryRunAnalytics;
    }

    public String getApiKeyConversations() {
        return apiKeyConversations;
    }

    public String getApiKeyConversationsStores() {
        return apiKeyConversationsStores;
    }

    public String getApiKeyCurations() {
        return apiKeyCurations;
    }

    public String getApiKeyLocationAndroid() {
        return apiKeyLocationAndroid;
    }

    public String getApiKeyShopperAdvertising() {
        return apiKeyShopperAdvertising;
    }


    public String getClientId() {
        return clientId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getApiKeyProgressiveSubmission() { return apiKeyProgressiveSubmission; }
    public String getApiKeyProductSentiments() {
        return apiKeyProductSentiments;
    }

    /**
     * Use this for SingleTenantSource since the generated config files have no concept of displayName
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean hasConversations() {
        return apiKeyConversations != null && !apiKeyConversations.equals("REPLACE_ME");
    }

    public boolean hasConversationsStores() {
        return apiKeyConversationsStores != null && !apiKeyConversationsStores.equals("REPLACE_ME");
    }

    public boolean hasCurations() {
        return apiKeyCurations != null && !apiKeyCurations.equals("REPLACE_ME");
    }
    public boolean hasProductSentiments() {
        return apiKeyProductSentiments != null && !apiKeyProductSentiments.equals("REPLACE_ME");
    }
    public boolean hasShopperAds() {
        return apiKeyShopperAdvertising != null && !apiKeyShopperAdvertising.equals("REPLACE_ME");
    }

    public boolean hasLocation() {
        return apiKeyLocationAndroid != null && !apiKeyLocationAndroid.equals("REPLACE_ME");
    }

    public boolean isMockClient() {
        return displayName.equals(MOCK_DISPLAY_NAME);
    }

    public boolean isDryRunAnalytics() {
        return dryRunAnalytics;
    }

    @Override
    public String toString() {
        StringBuilder summary = new StringBuilder(displayName + ": ");

        if (hasConversations()) {
            summary.append("conversations, ");
        }
        if (hasConversationsStores()) {
            summary.append("stores, ");
        }
        if (hasCurations()) {
            summary.append("curations, ");
        }
        if (hasProductSentiments()) {
            summary.append("product sentiments, ");
        }
        if (hasShopperAds()) {
            summary.append("shopper advertising, ");
        }
        if (hasLocation()) {
            summary.append("location, ");
        }

        summary.delete(summary.length() - 2, summary.length());

        return summary.toString();
    }

    public static BVConfig mapToBvConfig(DemoClient demoClient) {
        return new BVConfig.Builder()
                .apiKeyConversations(getKeyOrReplaceMe(demoClient.getApiKeyConversations()))
                .apiKeyConversationsStores(getKeyOrReplaceMe(demoClient.getApiKeyConversationsStores()))
                .apiKeyCurations(getKeyOrReplaceMe(demoClient.getApiKeyCurations()))
                .apiKeyLocation(getKeyOrReplaceMe(demoClient.getApiKeyLocationAndroid()))
                .apiKeyProgressiveSubmission(getKeyOrReplaceMe(demoClient.getApiKeyProgressiveSubmission()))
                .apiKeyShopperAdvertising(getKeyOrReplaceMe(demoClient.getApiKeyShopperAdvertising()))
                .apiKeyProductSentiments(getKeyOrReplaceMe(demoClient.getApiKeyProductSentiments()))
                .clientId(getKeyOrReplaceMe(demoClient.getClientId()))
                .dryRunAnalytics(demoClient.isDryRunAnalytics())
                .build();
    }

    private static String getKeyOrReplaceMe(@Nullable String key) {
        return key != null ? key : "REPLACE_ME";
    }

}
