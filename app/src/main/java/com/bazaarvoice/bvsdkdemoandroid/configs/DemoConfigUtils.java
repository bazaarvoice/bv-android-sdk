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

import android.content.Context;
import android.content.SharedPreferences;

import com.bazaarvoice.bvsdkdemoandroid.R;

import java.util.List;

import javax.inject.Inject;

public class DemoConfigUtils {

    private Context context;
    private DemoConfigParser demoConfigParser;
    private SharedPreferences sharedPrefs;

    @Inject
    DemoConfigUtils(Context context, DemoConfigParser demoConfigParser, SharedPreferences sharedPrefs) {
        this.context = context;
        this.demoConfigParser = demoConfigParser;
        this.sharedPrefs = sharedPrefs;
    }

    private void putStringInPrefs(String key, String value) {
        sharedPrefs.edit().putString(key, value).commit();
    }

    private String getStringInPrefs(String key, String defaultValue) {
        return sharedPrefs.getString(key, defaultValue);
    }

    public void putClientId(String clientId) {
        String keyClientId = context.getString(R.string.key_client_id);
        putStringInPrefs(keyClientId, clientId);
    }

    public String getClientId() {
        String keyClientId = context.getString(R.string.key_client_id);
        String defaultClientId = demoConfigParser.getDefaultConfig().clientId;
        return getStringInPrefs(keyClientId, defaultClientId);
    }

    public String getShopperAdPasskey() {
        DemoConfig config = demoConfigParser.getConfigFromClientId(getClientId());
        return config.apiKeyShopperAdvertising;
    }

    public String getConversationsPasskey() {
        DemoConfig config = demoConfigParser.getConfigFromClientId(getClientId());
        return config.apiKeyConversations;
    }

    public String getConversationsStoresPasskey() {
        DemoConfig config = demoConfigParser.getConfigFromClientId(getClientId());
        return config.apiKeyConversationsStores;
    }

    public String getCurationsPasskey() {
        DemoConfig config = demoConfigParser.getConfigFromClientId(getClientId());
        return config.apiKeyCurations;
    }

    public String getLocationPasskey() {
        DemoConfig config = demoConfigParser.getConfigFromClientId(getClientId());
        return config.apiKeyLocationAndroid;
    }

    public String getPinPasskey() {
        DemoConfig config = demoConfigParser.getConfigFromClientId(getClientId());
        return config.apiKeyPIN;
    }

    public DemoConfig getCurrentConfig() {
        return demoConfigParser.getConfigFromClientId(getClientId());
    }

    public DemoConfig getConfigFromClientId(String clientId) {
        return demoConfigParser.getConfigFromClientId(clientId);
    }

    public CharSequence[] getDisplayNames() {
        List<DemoConfig> configList = demoConfigParser.getConfigList();
        CharSequence[] displayNames = new CharSequence[configList.size()];
        int i = 0;
        for (DemoConfig config : configList) {
            displayNames[i++] = config.displayName;
        }
        return displayNames;
    }

    public CharSequence[] getClientIdNames() {
        List<DemoConfig> configList = demoConfigParser.getConfigList();
        CharSequence[] clientIdNames = new CharSequence[configList.size()];
        int i = 0;
        for (DemoConfig config : configList) {
            clientIdNames[i++] = config.clientId;
        }
        return clientIdNames;
    }

    public void putLastLocationEvent(String lastLocationEvent) {
        String keyLastLocationEvent = context.getString(R.string.key_last_location_event);
        putStringInPrefs(keyLastLocationEvent, lastLocationEvent);
    }

    public String getLastLocationEvent() {
        String keyLastLocationEvent = context.getString(R.string.key_last_location_event);
        return getStringInPrefs(keyLastLocationEvent, "none");
    }

    public boolean isDemoClient() {
        DemoConfig demoConfig = getCurrentConfig();
        return demoConfig.displayName.equals(DemoConfigParser.DEMO_DISPLAY_NAME);
    }

    public boolean configFileExists() {
        return demoConfigParser.configFileExists();
    }
}
