package com.bazaarvoice.bvsdkdemoandroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppContext;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoAndroidModule {
  public static final String CONFIG_SHARED_PREFS = "config_shared_prefs";

  private final Application application;

  public DemoAndroidModule(Application application) {
    this.application = application;
  }

  @Provides @DemoAppContext
  Context provideContext(Application application) {
    return application.getApplicationContext();
  }

  @Provides
  Application provideApplication() {
    return application;
  }

  @Provides @DemoAppScope
  SharedPreferences provideSharedPrefs(@DemoAppContext Context context) {
    return context.getSharedPreferences(CONFIG_SHARED_PREFS, Context.MODE_PRIVATE);
  }
}
