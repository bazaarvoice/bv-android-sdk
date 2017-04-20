package com.bazaarvoice.bvsdkdemoandroid.home;

import com.bazaarvoice.bvsdkdemoandroid.DemoActivityModule;
import com.bazaarvoice.bvsdkdemoandroid.DemoAppComponent;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;
import com.bazaarvoice.bvsdkdemoandroid.products.DemoProductsCarouselPresenterModule;

import dagger.Component;

@DemoActivityScope
@Component(dependencies = DemoAppComponent.class, modules = {DemoProductsCarouselPresenterModule.class, DemoActivityModule.class})
public interface DemoHomeComponent {
  void inject(DemoHomeActivity activity);
}
