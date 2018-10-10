package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.submit;

import androidx.annotation.NonNull;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BVHostedAuthenticationProvider;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionException;
import com.bazaarvoice.bvandroidsdk.Error;
import com.bazaarvoice.bvandroidsdk.FieldError;
import com.bazaarvoice.bvandroidsdk.FormField;
import com.bazaarvoice.bvandroidsdk.ReviewSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.ReviewSubmissionResponse;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DemoSubmitReviewPresenter implements DemoSubmitReviewContract.Presenter {
  private final DemoSubmitReviewContract.View view;
  private final BVConversationsClient client;
  private final String productId;

  private List<FormField> formFields; // Could be immutable if injected from model, but currently we'll fetch every time in here
  private Map<String, FormField> formFieldMap;

  // region Reason to change
  private String reviewTitle = null;
  private String reviewText = null;
  private String userNickname = null;
  private int rating = -1;
  private String hostedAuthenticationEmail = null;
  private final String HOSTED_AUTHENTICATION_CALLBACK_URL = "https://bazaarvoice.com"; // This is a common whitelisted url for testing. If this doesn't work for you look at what is whitelisted
  // endregion

  @Inject
  public DemoSubmitReviewPresenter(DemoSubmitReviewContract.View view, BVConversationsClient client, String productId) {
    this.view = view;
    this.client = client;
    this.productId = productId;
  }

  @Override
  public void start() {
    view.showLoading();
    final ReviewSubmissionRequest formRequest = new ReviewSubmissionRequest.Builder(Action.Form, productId)
        .build();
    client.prepareCall(formRequest).loadAsync(formCallback);
  }

  @Override
  public void onReviewTitleChanged(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  @Override
  public void onReviewTextChanged(String reviewText) {
    this.reviewText = reviewText;
  }

  @Override
  public void onUserNicknameChanged(String userNickname) {
    this.userNickname = userNickname;
  }

  @Override
  public void onRatingChanged(float rating) {
    this.rating = Math.round(rating);
  }

  @Override
  public void onHostedAuthenticationEmailChanged(String hostedAuthenticationEmail) {
    this.hostedAuthenticationEmail = hostedAuthenticationEmail;
  }

  @Override
  public void onSubmitReviewTapped() {
    if (!isReadyToValidate()) {
      return;
    }

    final boolean allFieldsAreValid = true; // let backend do validation for now
    if (allFieldsAreValid) {
      // form is valid, perform a preview to pretend to submit
      final ReviewSubmissionRequest submitRequest = new ReviewSubmissionRequest.Builder(Action.Preview, productId)
          .rating(rating)
          .title(reviewTitle)
          .reviewText(reviewText)
          .userNickname(userNickname)
          .authenticationProvider(new BVHostedAuthenticationProvider(hostedAuthenticationEmail, HOSTED_AUTHENTICATION_CALLBACK_URL))
          .build();
      client.prepareCall(submitRequest).loadAsync(submitCallback);
    }
  }

  private void getFormSuccess(@NonNull ReviewSubmissionResponse response) {
    // persist form fields in this instance for immediate validation
    formFields = response.getFormFields();
    formFieldMap = response.getFormData().getFormFieldMap();

    // for the sake of this multi-tenant app, we will not currently support site auth
    if (!isHostedAuthentication(response)) {
      view.showNotSupported("Site Authentication is not currently supported");
    }
  }

  private void getFormFailure(@NonNull ConversationsSubmissionException exception) {
    final StringBuilder stringBuilder = new StringBuilder("Submission has errors:");
    for (Error error : exception.getErrors()) {
      stringBuilder.append("\n")
          .append("\ncode: ").append(error.getCode())
          .append("\nmessage: ").append(error.getMessage());
    }
    for (FieldError fieldError : exception.getFieldErrors()) {
      stringBuilder.append("\n")
          .append("\ncode: ").append(fieldError.getCode())
          .append("\nmessage: ").append(fieldError.getMessage())
          .append("\nfieldId: ").append(fieldError.getFormField().getId());
    }
    view.showUncaughtError("Failed to Get Form", stringBuilder.toString());
    exception.printStackTrace();
  }

  private void submitReviewSuccess(@NonNull ReviewSubmissionResponse response) {
    view.showSubmitSuccess();
  }

  private void submitReviewFailure(@NonNull ConversationsSubmissionException exception) {
    final StringBuilder stringBuilder = new StringBuilder("Submission has errors:");
    for (Error error : exception.getErrors()) {
      stringBuilder.append("\n")
          .append("\ncode: ").append(error.getCode())
          .append("\nmessage: ").append(error.getMessage());
    }
    for (FieldError fieldError : exception.getFieldErrors()) {
      stringBuilder.append("\n")
          .append("\ncode: ").append(fieldError.getCode())
          .append("\nmessage: ").append(fieldError.getMessage())
          .append("\nfieldId: ").append(fieldError.getFormField().getId());
    }
    view.showUncaughtError("Failed to Submit", stringBuilder.toString());
    exception.printStackTrace();
  }

  private final ConversationsSubmissionCallback<ReviewSubmissionResponse> formCallback = new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
    @Override
    public void onSuccess(@NonNull ReviewSubmissionResponse response) {
      getFormSuccess(response);
    }

    @Override
    public void onFailure(@NonNull ConversationsSubmissionException exception) {
      getFormFailure(exception);
    }
  };

  private final ConversationsSubmissionCallback<ReviewSubmissionResponse> submitCallback = new ConversationsSubmissionCallback<ReviewSubmissionResponse>() {
    @Override
    public void onSuccess(@NonNull ReviewSubmissionResponse response) {
      submitReviewSuccess(response);
    }

    @Override
    public void onFailure(@NonNull ConversationsSubmissionException exception) {
      submitReviewFailure(exception);
    }
  };

  /**
   * @param response The submission response containing form data
   * @return If the Form contains the BV Hosted auth form fields, then this client passkey is BV Hosted
   */
  private boolean isHostedAuthentication(@NonNull ReviewSubmissionResponse response) {
    return formFieldMap.containsKey("hostedauthentication_authenticationemail");
  }

  private boolean isReadyToValidate() {
    return formFieldMap != null;
  }

  @Inject
  void setupListeners() {
    view.setPresenter(this);
  }
}
