package com.bazaarvoice.bvsdkdemoandroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoAndroidModule {
  public static final String CONFIG_SHARED_PREFS = "config_shared_prefs";

  private final Application application;

  public DemoAndroidModule(Application application) {
    this.application = application;
  }

  @Provides @Named("AppContext")
  Context provideContext(Application application) {
    return application.getApplicationContext();
  }

  @Provides
  Application provideApplication() {
    return application;
  }

  @Provides @DemoAppScope
  SharedPreferences provideSharedPrefs(@Named("AppContext") Context context) {
    return context.getSharedPreferences(CONFIG_SHARED_PREFS, Context.MODE_PRIVATE);
  }
}
