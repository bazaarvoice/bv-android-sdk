package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import com.bazaarvoice.bvandroidsdk.Comment;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBasePresenter;
import com.bazaarvoice.bvsdkdemoandroid.mvp.DemoBaseView;

import java.util.List;

public interface DemoCommentsContract {
  interface View extends DemoBaseView<Presenter> {
    void showComments(List<Comment> comments);
    void showMessage(String message);
  }
  interface Presenter extends DemoBasePresenter {}
}
