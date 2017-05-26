package com.bazaarvoice.bvsdkdemoandroid.conversations;

import com.bazaarvoice.bvsdkdemoandroid.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.DemoBaseView;

public interface DemoConvApiContract {
  interface View extends DemoBaseView<Presenter> {
    void showDialogWithNotConfiguredMessage(String displayName);
    void showDialogWithMessage(String message);
    void showProgressWithTitle(String title);
    void hideProgress();
    void showRequiredIdTitle(String title);
  }

  interface Presenter extends DemoBasePresenter {
    void onApiMethodChanged(ConvApiMethod convApiMethod);
    void onRunMethodTapped();
    void onRequiredIdChanged(String requiredId);
  }

  enum ConvApiMethod {
    DISPLAY_REVIEWS("Display Reviews"),
    DISPLAY_QANDA("Display QandA"),
    DISPLAY_PDP("Display PDP"),
    DISPLAY_BULK_RATINGS("Display Bulk Ratings"),
    DISPLAY_AUTHOR("Display Author"),
    DISPLAY_COMMENTS("Display Comments"),
    SUBMIT_REVIEW("Submit Review"),
    SUBMIT_QUESTION("Submit Question"),
    SUBMIT_ANSWER("Submit Answer"),
    SUBMIT_FEEDBACK("Submit Feedback");

    private String method;

    ConvApiMethod(String method) {
      this.method = method;
    }

    public static ConvApiMethod from(String input) {
      if (input.equals(DISPLAY_REVIEWS.getMethod())) {
        return DISPLAY_REVIEWS;
      } else if (input.equals(DISPLAY_QANDA.getMethod())) {
        return DISPLAY_QANDA;
      } else if (input.equals(DISPLAY_PDP.getMethod())) {
        return DISPLAY_PDP;
      } else if (input.equals(DISPLAY_BULK_RATINGS.getMethod())) {
        return DISPLAY_BULK_RATINGS;
      } else if (input.equals(DISPLAY_AUTHOR.getMethod())) {
        return DISPLAY_AUTHOR;
      } else if (input.equals(DISPLAY_COMMENTS.getMethod())) {
        return DISPLAY_COMMENTS;
      } else if (input.equals(SUBMIT_REVIEW.getMethod())) {
        return SUBMIT_REVIEW;
      } else if (input.equals(SUBMIT_QUESTION.getMethod())) {
        return SUBMIT_QUESTION;
      } else if (input.equals(SUBMIT_ANSWER.getMethod())) {
        return SUBMIT_ANSWER;
      } else if (input.equals(SUBMIT_FEEDBACK.getMethod())) {
        return SUBMIT_FEEDBACK;
      } else {
        throw new IllegalStateException("Not an available API method");
      }
    }

    public String getMethod() {
      return method;
    }
  }
}