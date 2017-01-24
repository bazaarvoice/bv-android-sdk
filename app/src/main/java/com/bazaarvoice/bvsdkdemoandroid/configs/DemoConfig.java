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
        return displayName.equals(DemoConfigParser.DEMO_DISPLAY_NAME);
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
