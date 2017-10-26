package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.submit;

import com.bazaarvoice.bvsdkdemoandroid.DemoAppComponent;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;

import dagger.Component;

@DemoActivityScope
@Component(dependencies = DemoAppComponent.class, modules = {DemoSubmitReviewPresenterModule.class})
public interface DemoSubmitReviewComponent {
  void inject(DemoSubmitReviewActivity activity);
}
