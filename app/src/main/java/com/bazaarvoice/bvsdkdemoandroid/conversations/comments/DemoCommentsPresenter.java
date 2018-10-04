package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.CommentOptions;
import com.bazaarvoice.bvandroidsdk.CommentsRequest;
import com.bazaarvoice.bvandroidsdk.CommentsResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsException;
import com.bazaarvoice.bvandroidsdk.SortOrder;

import javax.inject.Inject;
import javax.inject.Named;

public class DemoCommentsPresenter implements DemoCommentsContract.Presenter, ConversationsDisplayCallback<CommentsResponse> {
  private final DemoCommentsContract.View view;
  private final String contentId;
  private final boolean isCommentId;
  private final BVConversationsClient conversationsClient;

  @Inject
  public DemoCommentsPresenter(DemoCommentsContract.View view, @Named("CommentsContentId") String contentId, @Named("CommentsIsCommentId") boolean isCommentId, BVConversationsClient conversationsClient) {
    this.view = view;
    this.contentId = contentId;
    this.isCommentId = isCommentId;
    this.conversationsClient = conversationsClient;
  }

  @Override
  public void start() {
    final CommentsRequest request = createRequest();
    conversationsClient.prepareCall(request).loadAsync(this);
  }

  private CommentsRequest createRequest() {
    CommentsRequest.Builder builder = null;
    if (isCommentId) {
      builder = new CommentsRequest.Builder(contentId);
    } else {
      builder = new CommentsRequest.Builder(contentId, 20, 0);
    }
    return builder
        .addSort(CommentOptions.Sort.SUBMISSION_TIME, SortOrder.DESC)
        .build();
  }

  @Override
  public void onSuccess(@NonNull CommentsResponse response) {
    view.showComments(response.getResults());
  }

  @Override
  public void onFailure(@NonNull ConversationsException exception) {
    view.showMessage(exception.getMessage());
  }

  @Inject
  void setupListeners() {
    view.setPresenter(this);
  }
}
