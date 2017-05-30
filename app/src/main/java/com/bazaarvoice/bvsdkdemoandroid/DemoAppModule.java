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

import android.content.Context;
import android.graphics.Bitmap;

import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoMockDataUtil;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppScope;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.concurrent.Executors;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class DemoAppModule {
    @Provides @DemoAppScope
    Gson provideGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Provides @DemoAppScope
    Picasso providePicasso(@Named("AppContext") Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .executor(Executors.newFixedThreadPool(8))
                .memoryCache(new LruCache(context))
                .build();
    }

    @Provides @DemoAppScope
    PrettyTime providePrettyTime() {
        return new PrettyTime();
    }

    @Provides @DemoAppScope
    DemoSdkInterceptor provideSdkInterceptor(DemoClient demoClient, DemoMockDataUtil demoMockDataUtil) {
        return new DemoSdkInterceptor(demoClient, demoMockDataUtil);
    }

    @Provides @DemoAppScope
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides @DemoAppScope
    OkHttpClient provideHttpClient(DemoSdkInterceptor demoSdkInterceptor, StethoInterceptor stethoInterceptor) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(stethoInterceptor)
                .addInterceptor(demoSdkInterceptor)
                .build();
    }

    @Provides @DemoAppScope
    DemoAssetsUtil provideDemoAssetsUtil(@Named("AppContext") Context context, Gson gson) {
        return new DemoAssetsUtil(context, gson);
    }
}
