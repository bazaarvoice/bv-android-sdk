package com.bazaarvoice.bvsdkdemoandroid.products;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoProductsCarouselPresenterModule {
  private DemoProductsContract.View view;

  public DemoProductsCarouselPresenterModule(DemoProductsContract.View view) {
    this.view = view;
  }

  @Provides @Named("ProductsCarouselView")
  DemoProductsContract.View providesCarouselView() {
    return view;
  }
}
