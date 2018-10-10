package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.bazaarvoice.bvandroidsdk.Comment;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.utils.VerticalSpaceItemDecoration;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoCommentsView extends FrameLayout implements DemoCommentsContract.View {
  private DemoCommentsContract.Presenter presenter;
  @BindView(R.id.comments_recycler_view)
  RecyclerView commentRecyclerView;
  private DemoCommentsAdapter commentsAdapter;

  public DemoCommentsView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater.from(context).inflate(R.layout.view_comments, this, true);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    setupToolbarViews();
    setupRecyclerView();
  }

  private void setupToolbarViews() {
    if (!appCompatActivityActive()) {
      return;
    }
    AppCompatActivity appCompatActivity = (AppCompatActivity) getContext();
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    appCompatActivity.setSupportActionBar(toolbar);
    appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void setupRecyclerView() {
    if (!appCompatActivityActive()) {
      return;
    }
    AppCompatActivity appCompatActivity = (AppCompatActivity) getContext();
    commentsAdapter = new DemoCommentsAdapter();
    commentRecyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));
    int spacing = getResources().getDimensionPixelSize(R.dimen.margin_3);
    commentRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(spacing));
    commentRecyclerView.setAdapter(commentsAdapter);
    commentRecyclerView.setNestedScrollingEnabled(false);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) {
      return;
    }
    presenter.start();
  }

  @Override
  public void showComments(List<Comment> comments) {
    commentsAdapter.refreshComments(comments);
  }

  @Override
  public void showMessage(String message) {
    if (!appCompatActivityActive()) {
      return;
    }
    AppCompatActivity appCompatActivity = (AppCompatActivity) getContext();
    AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
    builder.setMessage(message);
    builder.setNegativeButton("OK", null).create().show();
  }

  @Override
  public void setPresenter(DemoCommentsContract.Presenter presenter) {
    this.presenter = presenter;
  }

  public boolean appCompatActivityActive() {
    return getContext() != null && getContext() instanceof AppCompatActivity && !((AppCompatActivity) getContext()).isFinishing();
  }
}
