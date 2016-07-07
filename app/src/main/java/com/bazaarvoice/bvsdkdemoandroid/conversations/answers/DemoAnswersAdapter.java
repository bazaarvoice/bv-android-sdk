/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.answers;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Answer;
import com.bazaarvoice.bvsdkdemoandroid.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.List;

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
            String timeAgoBy = hasUserNickname ? timeAgo + " by " + bazaarAnswer.getUserNickname() : timeAgo;
            answerRowViewHolder.answerTimeAgo.setText(timeAgoBy);
            answerRowViewHolder.answerTimeAgo.setVisibility(View.VISIBLE);

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
