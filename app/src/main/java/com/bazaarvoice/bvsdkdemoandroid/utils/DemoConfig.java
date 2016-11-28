/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.utils;

public class DemoConfig {
    public String apiKeyConversations;
    public String apiKeyConversationsStores;
    public String apiKeyCurations;
    public String apiKeyLocationAndroid;
    public String apiKeyShopperAdvertising;
    public String apiKeyPIN;
    public String clientId;
    public String displayName;

    public DemoConfig() {
    }

    public DemoConfig(String apiKeyConversations, String apiKeyConversationsStores, String apiKeyCurations, String apiKeyShopperAdvertising, String apiKeyLocationAndroid, String apiKeyPIN, String clientId, String displayName) {
        this.apiKeyConversations = apiKeyConversations;
        this.apiKeyConversationsStores = apiKeyConversationsStores;
        this.apiKeyCurations = apiKeyCurations;
        this.apiKeyShopperAdvertising = apiKeyShopperAdvertising;
        this.apiKeyPIN = apiKeyPIN;
        this.clientId = clientId;
        this.displayName = displayName;
        this.apiKeyLocationAndroid = apiKeyLocationAndroid;
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

    public boolean hasShopperAds() {
        return apiKeyShopperAdvertising != null && !apiKeyShopperAdvertising.equals("REPLACE_ME");
    }

    public boolean hasLocation() {
        return apiKeyLocationAndroid != null && !apiKeyLocationAndroid.equals("REPLACE_ME");
    }

    public boolean hasPin() {
        return apiKeyPIN != null && !apiKeyPIN.equals("REPLACE_ME");
    }

    public boolean isDemoClient() {
        return displayName.equals(DemoConfigXmlParser.demoDisplayName);
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
        if (hasShopperAds()) {
            summary.append("shopper advertising, ");
        }
        if (hasLocation()) {
            summary.append("location, ");
        }

        summary.delete(summary.length() - 2, summary.length());

        return summary.toString();
    }
}
