package com.bazaarvoice.bvsdkdemoandroid;

import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvApiPresenterModule;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;

import dagger.Component;

@DemoActivityScope
@Component(dependencies = DemoAppComponent.class, modules = {DemoConvApiPresenterModule.class, DemoActivityModule.class})
public interface DemoCodeHomeComponent {
  void inject(DemoMainActivity activity);
}
