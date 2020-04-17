package com.bazaarvoice.bvsdkdemoandroid.conversations;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.AnswerSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.AnswerSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.CommentSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.CommentSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionException;
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
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

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
  private String filterType;
  private String filterValue;

  @Inject
  DemoConvApiPresenter(DemoConvApiContract.View view, DemoAssetsUtil demoAssetsUtil, DemoRouter demoRouter, BVConversationsClient bvConversationsClient, DemoClient demoClient, DemoConvResponseHandler demoConvResponseHandler, Action submitAction, @Named("DefaultRequiredId") String defaultRequiredId) {
    this.view = view;
    this.bvConversationsClient = bvConversationsClient;
    this.demoClient = demoClient;
    this.demoAssetsUtil = demoAssetsUtil;
    this.demoRouter = demoRouter;
    this.demoConvResponseHandler = demoConvResponseHandler;
    this.submitAction = submitAction;
    this.convApiMethod = DemoConvApiContract.ConvApiMethod.DISPLAY_REVIEWS;
    this.requiredId = defaultRequiredId;
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

    // TODO: This is where we could swap out different forms for each Submission Type
    switch (convApiMethod) {
      case DISPLAY_REVIEWS:
      case DISPLAY_QANDA:
      case DISPLAY_PDP:
      case SUBMIT_REVIEW:
      case DISPLAY_REVIEW_HIGHLGHTS:
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
      case DISPLAY_COMMENTS:
      case SUBMIT_COMMENT:
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
        if (filterValue != null) {
          demoRouter.transitionToReviewsActivity(filterType, filterValue);
        } else {
          demoRouter.transitionToReviewsActivity(requiredId);
        }
        break;
      }
      case DISPLAY_QANDA: {
        demoRouter.transitionToQuestionsActivity(requiredId);
        break;
      }
      case DISPLAY_REVIEW_HIGHLGHTS: {
        demoRouter.transitionToReviewHighlights(requiredId);
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
      case DISPLAY_COMMENTS: {
        demoRouter.transitionToCommentsActivity(requiredId, false);
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
      case SUBMIT_COMMENT: {
        submitComment(requiredId);
        break;
      }
    }
  }

  @Override
  public void onRequiredIdChanged(String requiredId) {
    this.requiredId = requiredId;
  }

  @Override
  public void onFilterTypeChanged(String filterType) {
    this.filterType = filterType;
  }

    @Override
    public void onFilterValueChanged(String filterValue) {
        this.filterValue = filterValue;
    }

  private boolean readyForDemo() {
    String conversationsKey = demoClient.getApiKeyConversations();
    if (!demoClient.isMockClient() && !DemoConstants.isSet(conversationsKey)) {
      return false;
    }
    return true;
  }

  private void submitReviewWithPhoto(String productId) {
    view.showProgressWithTitle("Submitting Review...");

    File localImageFile = demoAssetsUtil.parseImageFileFromAssets("puppy_thumbnail.jpg");
    String userNickName = "shazbat105" + new Random().nextInt(1001);
    ReviewSubmissionRequest submission = new ReviewSubmissionRequest.Builder(submitAction, productId)
        .userNickname(userNickName)
        .userEmail("foo@bar.com")
        .userId(userNickName) // Creating a random user id to avoid duplicated -- FOR TESTING ONLY!!!
        .rating(5)
        .title("Review title")
        .reviewText("This is the review text the user adds about how great the product is!")
        .sendEmailAlertWhenCommented(true)
        .sendEmailAlertWhenPublished(true)
        .agreedToTermsAndConditions(true)
        .addPhoto(localImageFile, "What a cute pupper!")
        .build();

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull ReviewSubmissionResponse response) {
        handleSubmissionSuccessResponse();
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
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

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsSubmissionCallback<QuestionSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull QuestionSubmissionResponse response) {
        handleSubmissionSuccessResponse();
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
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

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsSubmissionCallback<AnswerSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull AnswerSubmissionResponse response) {
        handleSubmissionSuccessResponse();
      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {
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

    bvConversationsClient.prepareCall(submission).loadAsync(new ConversationsSubmissionCallback<FeedbackSubmissionResponse>() {
      @Override
      public void onSuccess(FeedbackSubmissionResponse response) {
        handleSubmissionSuccessResponse();
      }

      @Override
      public void onFailure(ConversationsSubmissionException exception) {
        handleSubmissionFailureResponse(exception);
      }
    });
  }

  private void submitComment(String reviewId) {
    view.showProgressWithTitle("Submitting Comment...");

    String commentText = "Test comment from Android SDK";
    String commentTitle = "Android SDK";
    File localImageFile = demoAssetsUtil.parseImageFileFromAssets("bike_shop_photo.png");

    CommentSubmissionRequest request = new CommentSubmissionRequest.Builder(submitAction, reviewId, commentText)
        .title(commentTitle)
        .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicates -- FOR TESTING ONLY!!!
        .userNickname("androidsdkUserNick")
        .addPhoto(localImageFile, "Cute puppy there")
        .build();

    bvConversationsClient.prepareCall(request).loadAsync(new ConversationsSubmissionCallback<CommentSubmissionResponse>() {
      @Override
      public void onSuccess(CommentSubmissionResponse response) {
        handleSubmissionSuccessResponse();
      }

      @Override
      public void onFailure(ConversationsSubmissionException exception) {
        handleSubmissionFailureResponse(exception);
      }
    });
  }

  private void handleSubmissionSuccessResponse() {
    view.hideProgress();
    view.showDialogWithMessage(demoConvResponseHandler.getSubmissionSuccessMessage(submitAction));
  }

  private void handleSubmissionFailureResponse(ConversationsSubmissionException exception) {
    exception.printStackTrace();
    view.hideProgress();
    view.showDialogWithMessage(demoConvResponseHandler.getSubmissionErrorMessage(exception));
  }
}
