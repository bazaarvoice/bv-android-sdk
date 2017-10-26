package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.submit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoSubmitReviewActivity extends AppCompatActivity implements DemoSubmitReviewContract.View {
  private static final String EXTRA_PRODUCT_ID = "extra_product_id";

  @Inject DemoSubmitReviewContract.Presenter presenter;

  @BindView(R.id.reviewTitleInput) EditText reviewTitleInput;
  @BindView(R.id.reviewBodyInput) EditText reviewBodyInput;
  @BindView(R.id.userEmailInput) EditText userEmailInput;
  @BindView(R.id.userNicknameInput) EditText userNicknameInput;
  @BindView(R.id.reviewRating) RatingBar reviewRating;
  @BindView(R.id.submitReviewButton) Button submitReviewButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_submit_review);
    ButterKnife.bind(this);
    final String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
    DaggerDemoSubmitReviewComponent.builder()
        .demoAppComponent(DemoApp.getAppComponent(this))
        .demoSubmitReviewPresenterModule(new DemoSubmitReviewPresenterModule(this, productId))
        .build()
        .inject(this);
    presenter.start();
    setupViews();
  }

  private void setupViews() {
    reviewTitleInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onReviewTitleChanged(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    reviewBodyInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onReviewTextChanged(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    userEmailInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onHostedAuthenticationEmailChanged(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    userNicknameInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.onUserNicknameChanged(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    reviewRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> presenter.onRatingChanged(rating));
    submitReviewButton.setOnClickListener(view -> presenter.onSubmitReviewTapped());
  }

  public static void transitionTo(Context fromContext, String productId) {
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_PRODUCT_ID, productId);
    Intent intent = new Intent(fromContext, DemoSubmitReviewActivity.class);
    intent.putExtras(bundle);
    fromContext.startActivity(intent);
  }

  @Override
  public void setPresenter(DemoSubmitReviewContract.Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void showFailureToGetForm() {
    new AlertDialog.Builder(this)
        .setTitle("Failed")
        .setMessage("Failed to fetch the required form fields.")
        .setPositiveButton("OK", null)
        .show();

  }

  @Override
  public void showUncaughtError(String title, String message) {
    new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK", null)
        .show();

  }

  @Override
  public void showSubmitSuccess() {
    new AlertDialog.Builder(this)
        .setTitle("Success")
        .setMessage("Review has been submitted successfully!")
        .setPositiveButton("OK", (dialog, which) -> finish())
        .show();
  }

  @Override
  public void showNotSupported(String reasonMessage) {
    new AlertDialog.Builder(this)
        .setTitle("Not Supported")
        .setMessage(reasonMessage)
        .setPositiveButton("OK", null)
        .show();

  }
}
