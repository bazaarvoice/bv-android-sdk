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
import android.support.annotation.NonNull;
import android.util.Log;

import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.inject.Inject;

public class DemoConfigJsonParser implements DemoConfigParser {
    private static final String TAG = "DemoConfigJsonParser";
    private static final String DEMO_CONFIG_FILE = "demo_app_configs.json";
    private static final DemoConfig DEFAULT_CONFIG = new DemoConfig(
            DemoConstants.PASSKEY_CONVERSATIONS,
            DemoConstants.PASSKEY_CONVERSATIONS_STORES,
            DemoConstants.PASSKEY_CURATIONS,
            DemoConstants.PASSKEY_SHOPPER_AD,
            DemoConstants.PASSKEY_LOCATION,
            DemoConstants.PASSKEY_PIN,
            DemoConstants.BV_CLIENT_ID,
            "empty");
    private Gson gson;
    private DemoConfigs demoConfigs;
    private Context context;

    @Inject
    DemoConfigJsonParser(Gson gson, Context context) {
        this.gson = gson;
        this.context = context;
    }

    @Override @NonNull
    public DemoConfig getConfigFromClientId(String clientId) {
        DemoConfig clientConfig = DEFAULT_CONFIG;
        for (DemoConfig currConfig : getConfigList()) {
            if (currConfig.clientId.equals(clientId)) {
                clientConfig = currConfig;
                break;
            }
        }
        if (clientConfig == DEFAULT_CONFIG) {
            Log.e(TAG, "Failed to find clientId: " + clientId);
        }
        return clientConfig;
    }

    @Override
    public DemoConfig getDefaultConfig() {
        return getConfigList().get(0);
    }

    @Override
    public boolean configFileExists() {
        boolean exists = false;
        try {
            String[] assetFileNames = context.getAssets().list("");
            for (int i=0; i<assetFileNames.length; i++) {
                String assetFileName = assetFileNames[i];
                if (assetFileName.equals(DEMO_CONFIG_FILE)) {
                    exists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    @Override
    public List<DemoConfig> getConfigList() {
        try {
            if (demoConfigs == null) {
                Reader reader = new InputStreamReader(context.getAssets().open(DEMO_CONFIG_FILE));
                demoConfigs = gson.fromJson(reader, DemoConfigs.class);
            }
            if (DemoConstants.ENVIRONMENT == BazaarEnvironment.PRODUCTION) {
                return demoConfigs.getProdConfigs();
            } else {
                return demoConfigs.getStgConfigs();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to find config file", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
