package com.bazaarvoice.bvsdkdemoandroid.conversations;

import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBaseView;

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
    void onFilterTypeChanged(String filterType);
    void onFilterValueChanged(String filterValue);
  }

  enum ConvApiMethod {
    DISPLAY_REVIEWS("Display Reviews"),
    DISPLAY_REVIEWS_PRODUCT("Display Reviews - ProductId"),
    DISPLAY_REVIEWS_FILTER("Display Reviews - Primary Filter"),
    DISPLAY_QANDA("Display QandA"),
    DISPLAY_REVIEW_HIGHLGHTS("Display Review Highlights"),
    DISPLAY_PDP("Display PDP"),
    DISPLAY_BULK_RATINGS("Display Bulk Ratings"),
    DISPLAY_AUTHOR("Display Author"),
    DISPLAY_COMMENTS("Display Comments"),
    SUBMIT_REVIEW("Submit Review"),
    SUBMIT_QUESTION("Submit Question"),
    SUBMIT_ANSWER("Submit Answer"),
    SUBMIT_FEEDBACK("Submit Feedback"),
    SUBMIT_COMMENT("Submit Comment");

    private String method;

    ConvApiMethod(String method) {
      this.method = method;
    }

    public static ConvApiMethod from(String input) {
      if (input.equals(DISPLAY_REVIEWS_PRODUCT.getMethod()) || input.equals(DISPLAY_REVIEWS_FILTER.getMethod())) {
        return DISPLAY_REVIEWS;
      } else if (input.equals(DISPLAY_QANDA.getMethod())) {
        return DISPLAY_QANDA;
      } else if (input.equals(DISPLAY_REVIEW_HIGHLGHTS.getMethod())) {
        return DISPLAY_REVIEW_HIGHLGHTS;
      }else if (input.equals(DISPLAY_PDP.getMethod())) {
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
      } else if (input.equals(SUBMIT_COMMENT.getMethod())) {
        return SUBMIT_COMMENT;
      } else {
        throw new IllegalStateException("Not an available API method");
      }
    }

    public String getMethod() {
      return method;
    }
  }
}
