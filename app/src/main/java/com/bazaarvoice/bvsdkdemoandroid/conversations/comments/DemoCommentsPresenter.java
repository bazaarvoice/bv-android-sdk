package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.CommentOptions;
import com.bazaarvoice.bvandroidsdk.CommentsRequest;
import com.bazaarvoice.bvandroidsdk.CommentsResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.SortOrder;
import com.bazaarvoice.bvsdkdemoandroid.conversations.DemoConvResponseHandler;

import javax.inject.Inject;
import javax.inject.Named;

public class DemoCommentsPresenter implements DemoCommentsContract.Presenter, ConversationsCallback<CommentsResponse> {
  private final DemoCommentsContract.View view;
  private final String contentId;
  private final boolean isCommentId;
  private final BVConversationsClient conversationsClient;
  private final DemoConvResponseHandler responseHandler;

  @Inject
  public DemoCommentsPresenter(DemoCommentsContract.View view, @Named("CommentsContentId") String contentId, @Named("CommentsIsCommentId") boolean isCommentId, BVConversationsClient conversationsClient, DemoConvResponseHandler responseHandler) {
    this.view = view;
    this.contentId = contentId;
    this.isCommentId = isCommentId;
    this.conversationsClient = conversationsClient;
    this.responseHandler = responseHandler;
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
  public void onSuccess(CommentsResponse response) {
    responseHandler.handleDisplaySuccessResponse(response, new DemoConvResponseHandler.DisplayMessage() {
      @Override
      public void onSuccessMessage(String message) {}

      @Override
      public void onErrorMessage(String errorMessage) {
        view.showMessage(errorMessage);
      }
    });
    view.showComments(response.getResults());
  }

  @Override
  public void onFailure(BazaarException exception) {
    view.showMessage(exception.getMessage());
  }

  @Inject
  void setupListeners() {
    view.setPresenter(this);
  }
}
