package com.bazaarvoice.bvsdkdemoandroid;

import android.app.Activity;

import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoActivityModule {
  private Activity activity;

  public DemoActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @DemoActivityScope
  DemoRouter providesRouter() {
    return new DemoRouter(activity);
  }
}
