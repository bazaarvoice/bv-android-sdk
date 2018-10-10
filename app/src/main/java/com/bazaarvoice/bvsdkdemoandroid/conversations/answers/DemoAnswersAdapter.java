/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Answer;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.author.DemoAuthorActivity;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import androidx.core.text.util.LinkifyCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DemoAnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Answer> answers = Collections.emptyList();
    private PrettyTime prettyTime;

    public DemoAnswersAdapter() {
        this.prettyTime = new PrettyTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_answer, parent, false);
        return new AnswerRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Answer bazaarAnswer = answers.get(position);
        AnswerRowViewHolder answerRowViewHolder = (AnswerRowViewHolder) holder;

        answerRowViewHolder.answerText.setText(bazaarAnswer.getAnswerText());

        boolean hasTimeAgo = bazaarAnswer.getSubmissionDate() != null;
        if (hasTimeAgo) {
            String timeAgo = prettyTime.format(bazaarAnswer.getSubmissionDate());
            boolean hasUserNickname = !TextUtils.isEmpty(bazaarAnswer.getUserNickname());
            final String nickName = bazaarAnswer.getUserNickname();
            final String authorId = bazaarAnswer.getAuthorId();
            String timeAgoBy = hasUserNickname ? timeAgo + " by " + bazaarAnswer.getUserNickname() : timeAgo;
            answerRowViewHolder.answerTimeAgo.setText(timeAgoBy);
            answerRowViewHolder.answerTimeAgo.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(nickName)) {
                Pattern authorPattern = Pattern.compile(nickName);
                LinkifyCompat.addLinks(answerRowViewHolder.answerTimeAgo, authorPattern, null);
                answerRowViewHolder.answerTimeAgo.setOnClickListener(new View.OnClickListener() {
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
            answerRowViewHolder.answerTimeAgo.setVisibility(View.GONE);
        }

        boolean hasFeedback = bazaarAnswer.getTotalFeedbackCount() > 0;
        if (hasFeedback) {
            String foundHelpfulFormatter = answerRowViewHolder.answerFoundHelpful.getResources().getString(R.string.answer_found_helpful);
            String foundHelpful = String.format(foundHelpfulFormatter, bazaarAnswer.getTotalPositiveFeedbackCount(), bazaarAnswer.getTotalFeedbackCount());
            answerRowViewHolder.answerFoundHelpful.setText(foundHelpful);
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void refreshAnswers(List<Answer> answers) {
        this.answers = answers;
        notifyDataSetChanged();;
    }

    private final class AnswerRowViewHolder extends RecyclerView.ViewHolder {
        TextView answerText, answerTimeAgo, answerFoundHelpful;

        public AnswerRowViewHolder(View itemView) {
            super(itemView);
            answerText = (TextView) itemView.findViewById(R.id.answer_text);
            answerTimeAgo = (TextView) itemView.findViewById(R.id.answer_time_ago);
            answerFoundHelpful = (TextView) itemView.findViewById(R.id.answer_found_helpful);
        }
    }
}
