package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoCommentsModule {
  private final DemoCommentsContract.View view;
  private final String contentId;
  private final boolean isCommentId;

  public DemoCommentsModule(DemoCommentsContract.View view, String contentId, boolean isCommentId) {
    this.view = view;
    this.contentId = contentId;
    this.isCommentId = isCommentId;
  }

  @Provides
  DemoCommentsContract.View provideCommentsView() {
    return view;
  }

  @Provides @Named("CommentsContentId")
  String provideCommentsContentId() {
    return contentId;
  }

  @Provides @Named("CommentsIsCommentId")
  boolean provideCommentsIsCommentId() {
    return isCommentId;
  }
}
