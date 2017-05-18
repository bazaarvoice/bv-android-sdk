package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import com.bazaarvoice.bvandroidsdk.Comment;
import com.bazaarvoice.bvsdkdemoandroid.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.DemoBaseView;

import java.util.List;

public interface DemoCommentsContract {
  interface View extends DemoBaseView<Presenter> {
    void showComments(List<Comment> comments);
    void showMessage(String message);
  }
  interface Presenter extends DemoBasePresenter {}
}
