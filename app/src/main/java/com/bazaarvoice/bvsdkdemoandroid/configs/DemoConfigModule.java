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

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoConfigModule {
    @Provides @Singleton
    DemoConfigUtils provideDemoConfigUtils(Context context, DemoConfigParser demoConfigParser, SharedPreferences sharedPrefs) {
        return new DemoConfigUtils(context, demoConfigParser, sharedPrefs);
    }

    @Provides @Singleton
    DemoDataUtil provideDemoDataUtil(Context context, Gson gson) {
        return new DemoDataUtil(context, gson);
    }

    @Provides @Singleton
    DemoConfigParser provideDemoConfigParser(Gson gson, Context context) {
        return new DemoConfigJsonParser(gson, context);
    }
}
