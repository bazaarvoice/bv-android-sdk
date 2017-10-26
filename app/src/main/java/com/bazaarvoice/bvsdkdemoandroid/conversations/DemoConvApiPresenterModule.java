package com.bazaarvoice.bvsdkdemoandroid.conversations;

import android.content.Context;

import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoAppContext;

import javax.inject.Named;

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

  @Provides @Named("DefaultRequiredId")
  String provideDefaultRequiredId(@DemoAppContext Context context) {
    return context.getResources().getString(R.string.required_id);
  }
}
