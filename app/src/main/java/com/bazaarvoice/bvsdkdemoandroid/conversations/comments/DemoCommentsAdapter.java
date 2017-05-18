package com.bazaarvoice.bvsdkdemoandroid.conversations.comments;

import android.app.Activity;
import android.support.v4.text.util.LinkifyCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Comment;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.author.DemoAuthorActivity;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class DemoCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Comment> comments = Collections.emptyList();
  private PrettyTime prettyTime;

  public DemoCommentsAdapter() {
    this.prettyTime = new PrettyTime();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_answer, parent, false);
    return new CommentRowViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Comment bazaarComment = comments.get(position);
    CommentRowViewHolder commentViewHolder = (CommentRowViewHolder) holder;

    commentViewHolder.answerText.setText(bazaarComment.getCommentText());

    boolean hasTimeAgo = bazaarComment.getSubmissionDate() != null;
    if (hasTimeAgo) {
      String timeAgo = prettyTime.format(bazaarComment.getSubmissionDate());
      boolean hasUserNickname = !TextUtils.isEmpty(bazaarComment.getUserNickname());
      final String nickName = bazaarComment.getUserNickname();
      final String authorId = bazaarComment.getAuthorId();
      String timeAgoBy = hasUserNickname ? timeAgo + " by " + bazaarComment.getUserNickname() : timeAgo;
      commentViewHolder.answerTimeAgo.setText(timeAgoBy);
      commentViewHolder.answerTimeAgo.setVisibility(View.VISIBLE);
      if (!TextUtils.isEmpty(nickName)) {
        Pattern authorPattern = Pattern.compile(nickName);
        LinkifyCompat.addLinks(commentViewHolder.answerTimeAgo, authorPattern, null);
        commentViewHolder.answerTimeAgo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            TextView tv = (TextView) v;
            String tvText = String.valueOf(tv.getText());
            int selectStart = tv.getSelectionStart();
            int selectEnd = tv.getSelectionEnd();
            String selectedString = null;
            if (0 <= selectStart && selectStart <= tvText.length() && 0 <= selectEnd && selectEnd <= tvText.length()) {
              selectedString = String.valueOf(tvText.subSequence(selectStart, selectEnd));
            }
            if (selectedString != null && selectedString.equals(nickName)) {
              DemoAuthorActivity.transitionTo((Activity) tv.getContext(), authorId);
            }
          }
        });
      }
    } else {
      commentViewHolder.answerTimeAgo.setVisibility(View.GONE);
    }

    boolean hasFeedback = bazaarComment.getTotalFeedbackCount() > 0;
    if (hasFeedback) {
      String foundHelpfulFormatter = commentViewHolder.answerFoundHelpful.getResources().getString(R.string.answer_found_helpful);
      String foundHelpful = String.format(foundHelpfulFormatter, bazaarComment.getTotalPositiveFeedbackCount(), bazaarComment.getTotalFeedbackCount());
      commentViewHolder.answerFoundHelpful.setText(foundHelpful);
    }
  }

  @Override
  public int getItemCount() {
    return comments.size();
  }

  public void refreshComments(List<Comment> comments) {
    this.comments = comments;
    notifyDataSetChanged();;
  }

  private final class CommentRowViewHolder extends RecyclerView.ViewHolder {
    TextView answerText, answerTimeAgo, answerFoundHelpful;

    public CommentRowViewHolder(View itemView) {
      super(itemView);
      answerText = (TextView) itemView.findViewById(R.id.answer_text);
      answerTimeAgo = (TextView) itemView.findViewById(R.id.answer_time_ago);
      answerFoundHelpful = (TextView) itemView.findViewById(R.id.answer_found_helpful);
    }
  }
}
