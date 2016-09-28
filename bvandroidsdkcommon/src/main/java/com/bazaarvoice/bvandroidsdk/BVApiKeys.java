/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * API Key keeper for supported BV products.
 */
class BVApiKeys {
    final String apiKeyShopperAdvertising;
    final String apiKeyConversations;
    final String apiKeyConversationsStores;
    final String apiKeyCurations;
    final String apiKeyLocations;

    public BVApiKeys(String apiKeyShopperAdvertising, String apiKeyConversations, String apiKeyConversationsStores, String apiKeyCurations, String apiKeyLocations) {
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.apiKeyConversations = apiKeyConversations;
        this.apiKeyConversationsStores = apiKeyConversationsStores;
        this.apiKeyCurations = apiKeyCurations;
        this.apiKeyLocations = apiKeyLocations;
    }

    public String getApiKeyShopperAdvertising() {
        return apiKeyShopperAdvertising;
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

    public String getApiKeyLocations() {
        return apiKeyLocations;
    }
}