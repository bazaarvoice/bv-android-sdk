package com.bazaarvoice.bvsdkdemoandroid.conversations;

import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoConvApiPresenterModule {
  private final DemoConvApiContract.View view;

  public DemoConvApiPresenterModule(DemoConvApiContract.View view) {
    this.view = view;
  }

  @Provides @DemoActivityScope
  DemoConvApiContract.View provideDemoConvApiView() {
    return view;
  }
}
