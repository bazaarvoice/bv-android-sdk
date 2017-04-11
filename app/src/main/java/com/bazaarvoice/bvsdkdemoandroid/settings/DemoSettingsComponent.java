package com.bazaarvoice.bvsdkdemoandroid.settings;

import com.bazaarvoice.bvsdkdemoandroid.DemoAppComponent;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoFragmentScope;

import dagger.Component;

@DemoFragmentScope
@Component(dependencies = DemoAppComponent.class, modules = DemoSettingsModule.class)
interface DemoSettingsComponent {
  void inject(DemoPreferencesFragment fragment);
}
