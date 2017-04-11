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

import com.bazaarvoice.bvandroidsdk.BVConfig;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.bazaarvoice.bvandroidsdk.CurationsImageLoader;
import com.bazaarvoice.bvandroidsdk.PinClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoImageLoader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient.mapToBvConfig;

@Module
public class DemoBvModule {
    @Provides @Singleton
    BVSDK provideBvSdk(
        Application application,
        BazaarEnvironment bazaarEnvironment,
        BVConfig bvConfig,
        OkHttpClient okHttpClient,
        BVLogLevel bvLogLevel) {
        // Builder used to initialize the Bazaarvoice SDKs
        return BVSDK.builderWithConfig(application, bazaarEnvironment, bvConfig)
            .logLevel(bvLogLevel)
            .okHttpClient(okHttpClient)
            .build();
    }

    @Provides @Singleton
    BVConversationsClient provideConversationsClient() {
        return new BVConversationsClient();
    }

    @Provides @Singleton
    BazaarEnvironment provideBazaarEnvironment() {
        return DemoConstants.ENVIRONMENT;
    }

    @Provides @Singleton
    BVLogLevel provideBVLogLevel() {
        return DemoConstants.LOG_LEVEL;
    }

    @Provides @Singleton
    PinClient providePinClient(BVSDK bvsdk) {
        return new PinClient();
    }

    @Provides @Singleton
    BVConfig provideBVConfig(DemoClientConfigUtils demoClientConfigUtils) {
        return mapToBvConfig(demoClientConfigUtils.getCurrentConfig());
    }

    @Provides @Singleton
    CurationsImageLoader provideCurationImageLoader(Picasso picasso) {
        return new DemoImageLoader(picasso);
    }
}
