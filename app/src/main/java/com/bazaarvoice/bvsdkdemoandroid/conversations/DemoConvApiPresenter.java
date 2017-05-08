package com.bazaarvoice.bvsdkdemoandroid.conversations;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.AnswerSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.AnswerSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.QuestionSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.QuestionSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.ReviewSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.ReviewSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;
import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter;
import com.bazaarvoice.bvsdkdemoandroid.configs.DemoClient;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoAssetsUtil;

import java.io.File;

import javax.inject.Inject;

public class DemoConvApiPresenter implements DemoConvApiContract.Presenter {
  private final DemoConvApiContract.View view;
  private final BVConversationsClient bvConversationsClient;
  private final DemoClient demoClient;
  private final DemoAssetsUtil demoAssetsUtil;
  private final DemoRouter demoRouter;
  private final DemoConvResponseHandler demoConvResponseHandler;
  private final Action submitAction;
  private DemoConvApiContract.ConvApiMethod convApiMethod;
  private String requiredId;

  @Inject
  DemoConvApiPresenter(DemoConvApiContract.View view, DemoAssetsUtil demoAssetsUtil, DemoRouter demoRouter, BVConversationsClient bvConversationsClient, DemoClient demoClient, DemoConvResponseHandler demoConvResponseHandler, Action submitAction) {
    this.view = view;
    this.bvConversationsClient = bvConversationsClient;
    this.demoClient = demoClient;
    this.demoAssetsUtil = demoAssetsUtil;
    this.demoRouter = demoRouter;
    this.demoConvResponseHandler = demoConvResponseHandler;
    this.submitAction = submitAction;
    this.convApiMethod = DemoConvApiContract.ConvApiMethod.DISPLAY_REVIEWS;
    this.requiredId = "test1";
  }

  @Override
  public void start() {
    if (!readyForDemo()) {
      view.showDialogWithNotConfiguredMessage(demoClient.getDisplayName());
    }
  }

  @Inject
  void setupListeners() {
    view.setPresenter(this);
  }

  @Override
  public void onApiMethodChanged(DemoConvApiContract.ConvApiMethod convApiMethod) {
    this.convApiMethod = convApiMethod;

    // TODO: This is where we could swap out different forms for each Submission type
    switch (convApiMethod) {
      case DISPLAY_REVIEWS:
      case DISPLAY_QANDA:
      case DISPLAY_PDP:
      case SUBMIT_REVIEW:
      case SUBMIT_QUESTION: {
        view.showRequiredIdTitle("Product Id");
        break;
      }
      case DISPLAY_BULK_RATINGS: {
        view.showRequiredIdTitle("Bulk Ids");
        break;
      }
      case DISPLAY_AUTHOR: {
        view.showRequiredIdTitle("Author Id");
        break;
      }
      case SUBMIT_ANSWER: {
        view.showRequiredIdTitle("Question Id");
        break;
      }
      case SUBMIT_FEEDBACK: {
        view.showRequiredIdTitle("Review Id");
        break;
      }
    }

  }

  @Override
  public void onRunMethodTapped() {
    if (!readyForDemo()) {
      view.showDialogWithNotConfiguredMessage(demoClient.getDisplayName());
      return;
    }

    if (requiredId.isEmpty()) {
      view.showDialogWithMessage("Required ID must not be empty");
      return;
    }

    switch (convApiMethod) {
      case DISPLAY_REVIEWS: {
        demoRouter.transitionToReviewsActivity(requiredId);
        break;
      }
      case DISPLAY_QANDA: {
        demoRouter.transitionToQuestionsActivity(requiredId);
        break;
      }
      case DISPLAY_PDP: {
        demoRouter.transitionToProductStatsActivity(requiredId);
        break;
      }
      case DISPLAY_BULK_RATINGS: {
        demoRouter.transitionToBulkRatingsActivity(DemoConstants.TEST_BULK_PRODUCT_IDS);
        break;
      }
      case DISPLAY_AUTHOR: {
        demoRouter.transitionToAuthorActivity(requiredId);
        break;
      }
      case SUBMIT_REVIEW: {
        submitReviewWithPhoto(requiredId);
        break;
      }
      case SUBMIT_QUESTION: {
        submitQuestion(requiredId);
        break;
      }
      case SUBMIT_ANSWER: {
        submitAnswer(requiredId);
        break;
      }
      case SUBMIT_FEEDBACK: {
        submitFeedback(requiredId);
        break;
      }
    }
  }

  @Override
  public void onRequiredIdChanged(String requiredId) {
    this.requiredId = requiredId;
  }

  private boolean readyForDemo() {
    String conversationsKey = demoClient.getApiKeyConversations();
    if (!demoClient.isMockClient() && !DemoConstants.isSet(conversationsKey)) {
      return false;
    }
    return true;
  }

  private void submitReviewWithPhoto(String productId) {
    // For clients non-EU clients, iovation SDK is required!
    // String blackbox = getBlackbox(getContext().getApplicationContext());

    view.showProgressWithTitle("Submitting Review...");

    File localImageFile = demoAssetsUtil.parseImageFileFromAssets("puppy_thumbnail.jpg");

    ReviewSubmissionRequest submission = new ReviewSubmissionRequest.Builder(submitAction, productId)
//                            .fingerPrint(blackbox)  // uncomment me when using iovation SDK
        .userNickname("shazbat")
        .userEmail("foo@bar.com")
        .userId("shazbatuser" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
        .rating(5)
        .title("Review title")
        .reviewText("This is the review text the user adds about how great the product is!")
        .sendEmailAlertWhenCommented(true)
        .sendEmailAlertWhenPublished(true)
        .agreedToTermsAndConditions(true)
        .addPhoto(localImageFile, "What a cute pupper!")
        .build();

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(ReviewSubmissionResponse response) {
        handleSubmissionSuccessResponse(response, "review");
      }

      @Override
      public void onFailure(BazaarException exception) {
        handleSubmissionFailureResponse(exception);
      }
    });
  }

  private void submitQuestion(String productId) {
    view.showProgressWithTitle("Submitting Question...");

    QuestionSubmissionRequest submission = new QuestionSubmissionRequest.Builder(submitAction, productId)
        .userNickname("shazbat")
        .userEmail("foo@bar.com")
        .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
        .questionDetails("Question details...")
        .questionSummary("Question summary/title...")
        .sendEmailAlertWhenPublished(true)
        .agreedToTermsAndConditions(true)
        .build();

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsCallback<QuestionSubmissionResponse>() {
      @Override
      public void onSuccess(QuestionSubmissionResponse response) {
        handleSubmissionSuccessResponse(response, "question");
      }

      @Override
      public void onFailure(BazaarException exception) {
        handleSubmissionFailureResponse(exception);
      }
    });
  }

  private void submitAnswer(String questionId) {
    view.showProgressWithTitle("Submitting Answer...");

    AnswerSubmissionRequest submission = new AnswerSubmissionRequest.Builder(submitAction, questionId, "User answer text goes here....")
        //.fingerPrint(blackbox)  // uncomment me when using iovation SDK
        .userNickname("shazbat")
        .userEmail("foo@bar.com")
        .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
        .sendEmailAlertWhenPublished(true)
        .agreedToTermsAndConditions(true)
        .build();

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsCallback<AnswerSubmissionResponse>() {
      @Override
      public void onSuccess(AnswerSubmissionResponse response) {
        handleSubmissionSuccessResponse(response, "answer");
      }

      @Override
      public void onFailure(BazaarException exception) {
        handleSubmissionFailureResponse(exception);
      }
    });
  }

  private void submitFeedback(String reviewId) {
    view.showProgressWithTitle("Submitting Feedback...");

    FeedbackSubmissionRequest submission = new FeedbackSubmissionRequest.Builder(reviewId)
        .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicates -- FOR TESTING ONLY!!!
        .feedbackType(FeedbackType.HELPFULNESS)
        .feedbackContentType(FeedbackContentType.REVIEW)
        .feedbackVote(FeedbackVoteType.POSITIVE)
        .build();

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsCallback<FeedbackSubmissionResponse>() {
      @Override
      public void onSuccess(FeedbackSubmissionResponse response) {
        handleSubmissionSuccessResponse(response, "feedback");
      }

      @Override
      public void onFailure(BazaarException exception) {
        handleSubmissionFailureResponse(exception);
      }
    });
  }

  private void handleSubmissionSuccessResponse(ConversationsSubmissionResponse response, String apiMethodName) {
    view.hideProgress();
    demoConvResponseHandler.handleSubmissionSuccessResponse(response, apiMethodName, submitAction, new DemoConvResponseHandler.DisplayMessage() {
      @Override
      public void onSuccessMessage(String message) {
        view.showDialogWithMessage(message);
      }

      @Override
      public void onErrorMessage(String errorMessage) {
        view.showDialogWithMessage(errorMessage);
      }
    });
  }

  private void handleSubmissionFailureResponse(BazaarException exception) {
    view.hideProgress();
    view.showDialogWithMessage(exception.getMessage());
  }
}
