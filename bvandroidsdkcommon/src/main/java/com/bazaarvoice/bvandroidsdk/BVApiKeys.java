/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * TODO: Describe file here.
 */
class BVApiKeys {
    final String apiKeyShopperAdvertising;
    final String apiKeyConversations;
    final String apiKeyCurations;

    public BVApiKeys(String apiKeyShopperAdvertising, String apiKeyConversations, String apiKeyCurations) {
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.apiKeyConversations = apiKeyConversations;
        this.apiKeyCurations = apiKeyCurations;
    }

    public String getApiKeyShopperAdvertising() {
        return apiKeyShopperAdvertising;
    }

    public String getApiKeyConversations() {
        return apiKeyConversations;
    }

    public String getApiKeyCurations() {
        return apiKeyCurations;
    }
}