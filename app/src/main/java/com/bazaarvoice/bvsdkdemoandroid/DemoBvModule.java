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

package com.bazaarvoice.bvsdkdemoandroid;

import android.app.Application;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvandroidsdk.PinClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class DemoBvModule {
    @Provides @Singleton
    BVSDK provideBvSdk(Application application, DemoConfigUtils demoConfigUtils, OkHttpClient okHttpClient) {
        String clientId = demoConfigUtils.getClientId();
        String shopperAdvertisingApiKey = demoConfigUtils.getShopperAdPasskey();
        String conversationsApiKey = demoConfigUtils.getConversationsPasskey();
        String conversationsStoresApiKey = demoConfigUtils.getConversationsStoresPasskey();
        String curationsApiKey = demoConfigUtils.getCurationsPasskey();
        String locationApiKey = demoConfigUtils.getLocationPasskey();
        String pinApiKey = demoConfigUtils.getPinPasskey();


        // Builder used to initialize the Bazaarvoice SDKs
        return BVSDK.builder(application, clientId)
                .bazaarEnvironment(DemoConstants.ENVIRONMENT)
                .apiKeyShopperAdvertising(shopperAdvertisingApiKey)
                .apiKeyConversations(conversationsApiKey)
                .apiKeyConversationsStores(conversationsStoresApiKey)
                .apiKeyCurations(curationsApiKey)
                .apiKeyLocation(locationApiKey)
                .apiKeyPin(pinApiKey)
                .logLevel(BVLogLevel.VERBOSE)
                .okHttpClient(okHttpClient)
                .build();
    }

    @Provides @Singleton
    BVConversationsClient provideConversationsClient() {
        return new BVConversationsClient();
    }

    @Provides @Singleton
    PinClient providePinClient(BVSDK bvsdk) {
        return new PinClient();
    }
}
