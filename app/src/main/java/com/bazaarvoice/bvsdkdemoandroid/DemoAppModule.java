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
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.bazaarvoice.bvsdkdemoandroid.configs.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoDataUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class DemoAppModule {

    public static final String CONFIG_SHARED_PREFS = "config_shared_prefs";
    private final Application application;

    DemoAppModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Provides @Singleton
    Picasso providePicasso(Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .executor(Executors.newFixedThreadPool(8))
                .memoryCache(new LruCache(context))
                .build();
    }

    @Provides @Singleton
    PrettyTime providePrettyTime() {
        return new PrettyTime();
    }

    @Provides @Singleton
    SharedPreferences provideSharedPrefs(Context context) {
        return context.getSharedPreferences(CONFIG_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Provides @Singleton
    DemoSdkInterceptor provideSdkInterceptor(DemoConfigUtils demoConfigUtils, DemoDataUtil demoDataUtil) {
        return new DemoSdkInterceptor(demoConfigUtils, demoDataUtil);
    }

    @Provides @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides @Singleton
    OkHttpClient provideHttpClient(DemoSdkInterceptor demoSdkInterceptor, StethoInterceptor stethoInterceptor) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(stethoInterceptor)
                .addInterceptor(demoSdkInterceptor)
                .build();
    }

}
