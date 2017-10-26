package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.submit;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvsdkdemoandroid.di.DemoActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoSubmitReviewPresenterModule {
  private final DemoSubmitReviewContract.View view;
  private final String productId;

  public DemoSubmitReviewPresenterModule(DemoSubmitReviewContract.View view, String productId) {
    this.view = view;
    this.productId = productId;
  }

  @Provides @DemoActivityScope
  public DemoSubmitReviewContract.View provideView() {
    return view;
  }

  @Provides @DemoActivityScope
  public DemoSubmitReviewContract.Presenter providePresenter(BVConversationsClient client) {
    return new DemoSubmitReviewPresenter(view, client, productId);
  }
}
