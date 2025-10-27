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

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.BVConfig;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BVProductSentimentsClient;
import com.bazaarvoice.bvandroidsdk.BVRecommendations;
import com.bazaarvoice.bvandroidsdk.BVSDK;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.bazaarvoice.bvandroidsdk.CurationsImageLoader;
import com.bazaarvoice.bvandroidsdk.IovationFingerprint;
import com.bazaarvoice.bvandroidsdk.ProductSentimentsFingerprintProvider;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClientConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoImageLoader;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppScope;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

import static com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient.mapToBvConfig;

@Module
public class DemoBvModule {
    @Provides @DemoAppScope
    BVSDK provideBvSdk() {
        // Builder used to initialize the Bazaarvoice SDKs
        return BVSDK.getInstance();
    }

    @Provides @DemoAppScope
    BVConversationsClient provideConversationsClient(BVSDK bvsdk) {
        return new BVConversationsClient.Builder(bvsdk)
            .fingerprintProvider(new IovationFingerprint(bvsdk)) // For North America authentication
            .build();
    }

    @Provides @DemoAppScope
    BVProductSentimentsClient provideProductSentimentsClient(BVSDK bvsdk) {
        return new BVProductSentimentsClient.Builder(bvsdk)
                .build();
    }

    @Provides @DemoAppScope
    BazaarEnvironment provideBazaarEnvironment() {
        return DemoConstants.ENVIRONMENT;
    }

    @Provides @DemoAppScope
    BVLogLevel provideBVLogLevel() {
        return DemoConstants.LOG_LEVEL;
    }

    @Provides @DemoAppScope
    Action provideSubmitAction() {
        return DemoConstants.SUBMIT_ACTION;
    }

    @Provides @DemoAppScope
    BVRecommendations provideBvRecommendations(BVSDK bvsdk) {
        return new BVRecommendations();
    }

    @Provides @DemoAppScope
    BVConfig provideBVConfig(DemoClientConfigUtils demoClientConfigUtils) {
        return mapToBvConfig(demoClientConfigUtils.getCurrentConfig());
    }

    @Provides @DemoAppScope
    CurationsImageLoader provideCurationImageLoader(Picasso picasso) {
        return new DemoImageLoader(picasso);
    }

    @Provides @DemoAppScope
    DemoConvResponseHandler provideConvResponseHandler() {
        return new DemoConvResponseHandler();
    }
}
