package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bazaarvoice.bvsdkdemoandroid.DemoApp;
import com.bazaarvoice.bvsdkdemoandroid.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoCommentsActivity extends AppCompatActivity {
  private static final String EXTRA_CONTENT_ID = "extra_content_id";
  private static final String EXTRA_CONTENT_ID_IS_COMMENT = "extra_is_comment_id";

  @BindView(R.id.demoCommentsView) DemoCommentsView demoCommentsView;
  @Inject DemoCommentsPresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_conversations_comments);
    ButterKnife.bind(this);

    String contentId = getIntent().getStringExtra(EXTRA_CONTENT_ID);
    boolean isCommentId = getIntent().getBooleanExtra(EXTRA_CONTENT_ID_IS_COMMENT, true);

    DaggerDemoCommentsComponent.builder()
        .demoAppComponent(DemoApp.getAppComponent(this))
        .demoCommentsModule(new DemoCommentsModule(demoCommentsView, contentId, isCommentId))
        .build()
        .inject(this);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return super.onSupportNavigateUp();
  }

  public static void transitionToCommentsActivity(Context fromActivityContext, String contentId, boolean isCommentId) {
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_CONTENT_ID, contentId);
    bundle.putBoolean(EXTRA_CONTENT_ID_IS_COMMENT, isCommentId);
    Intent intent = new Intent(fromActivityContext, DemoCommentsActivity.class);
    intent.putExtras(bundle);
    fromActivityContext.startActivity(intent);
  }
}
