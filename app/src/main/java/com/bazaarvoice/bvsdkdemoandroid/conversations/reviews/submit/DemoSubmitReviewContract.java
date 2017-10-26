package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.submit;

import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBaseView;

public interface DemoSubmitReviewContract {
  interface View extends DemoBaseView<Presenter> {
    void showLoading();
    void showFailureToGetForm();
    void showUncaughtError(String title, String message);
    void showSubmitSuccess();
    void showNotSupported(String reasonMessage);
  }

  interface Presenter extends DemoBasePresenter {
    void onReviewTitleChanged(String reviewTitle);
    void onReviewTextChanged(String reviewText);
    void onUserNicknameChanged(String userNickname);
    void onRatingChanged(float rating);
    void onHostedAuthenticationEmailChanged(String hostedAuthenticationEmail);
    void onSubmitReviewTapped();
  }

  enum ViewFormField {
    TITLE, TEXT, RATING, AUTH_EMAIL, AUTH_CALLBACK
  }
}
