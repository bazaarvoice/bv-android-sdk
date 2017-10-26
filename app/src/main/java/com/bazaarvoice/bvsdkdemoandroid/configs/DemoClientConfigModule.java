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

import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppContext;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppScope;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoClientConfigModule {
    @Provides @DemoAppScope
    DemoClientConfigUtils provideDemoConfigUtils(@DemoAppContext Context context, BazaarEnvironment bazaarEnvironment,
                                                 DemoMultiTenantSource demoMultiTenantSource,
                                                 DemoSingleTenantSource demoSingleTenantSource, SharedPreferences sharedPrefs) {
        return new DemoClientConfigUtils(
            context,
            bazaarEnvironment,
            demoMultiTenantSource,
            demoSingleTenantSource,
            sharedPrefs);
    }

    @Provides @DemoAppScope
    DemoClient provideDemoClient(DemoClientConfigUtils demoClientConfigUtils) {
        return demoClientConfigUtils.getCurrentConfig();
    }

    @Provides @DemoAppScope
    DemoMockDataUtil provideMockDataUtil(Gson gson, DemoAssetsUtil demoAssetsUtil) {
        return new DemoMockDataUtil(gson, demoAssetsUtil);
    }
}
