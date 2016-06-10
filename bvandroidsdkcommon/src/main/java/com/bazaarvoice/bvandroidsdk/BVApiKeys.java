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
    final String apiKeyLocations;

    public BVApiKeys(String apiKeyShopperAdvertising, String apiKeyConversations, String apiKeyCurations, String apiKeyLocations) {
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.apiKeyConversations = apiKeyConversations;
        this.apiKeyCurations = apiKeyCurations;
        this.apiKeyLocations = apiKeyLocations;
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

    public String getApiKeyLocations() {
        return apiKeyLocations;
    }
}