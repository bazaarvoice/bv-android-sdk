/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.conversations.BazaarQuestion;
import com.bazaarvoice.bvsdkdemoandroid.conversations.answers.DemoAnswersActivity;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoSubmitDialogFragment;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.List;

public class DemoQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BazaarQuestion> questionList = Collections.emptyList();
    private PrettyTime prettyTime;
    private String productId;

    public DemoQuestionsAdapter(String productId) {
        this.prettyTime = new PrettyTime();
        this.productId = productId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_question, parent, false);
        return new QuestionRowViewHolder(reviewRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BazaarQuestion bazaarQuestion = questionList.get(position);
        QuestionRowViewHolder viewHolder = (QuestionRowViewHolder) holder;

        viewHolder.questionTitle.setText(bazaarQuestion.getQuestionSummary());

        boolean hasBody = !TextUtils.isEmpty(bazaarQuestion.getQuestionDetails()) && !bazaarQuestion.getQuestionDetails().equals("null");
        if (hasBody) {
            viewHolder.questionBody.setText(bazaarQuestion.getQuestionDetails());
            viewHolder.questionBody.setVisibility(View.VISIBLE);
        } else {
            viewHolder.questionBody.setVisibility(View.GONE);
        }

        boolean hasTimeAgo = bazaarQuestion.getSubmissionTime() != null;
        if (hasTimeAgo) {
            String timeAgo = prettyTime.format(bazaarQuestion.getSubmissionTime());
            boolean hasUserNickname = !TextUtils.isEmpty(bazaarQuestion.getUserNickname());
            String timeAgoBy = hasUserNickname ? timeAgo + " by " + bazaarQuestion.getUserNickname() : timeAgo;
            viewHolder.questionTimeAgo.setText(timeAgoBy);
            viewHolder.questionTimeAgo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.questionTimeAgo.setVisibility(View.GONE);
        }

        boolean hasAnswers = bazaarQuestion.getTotalAnswerCount() > 0;
        if (hasAnswers) {
            Resources resources = viewHolder.numAnswers.getResources();
            String formatStr = bazaarQuestion.getTotalAnswerCount() == 1 ?
                    resources.getString(R.string.conv_answer) :
                    resources.getString(R.string.conv_answers);
            String answersCountFormatted = String.format(formatStr, bazaarQuestion.getTotalAnswerCount());
            viewHolder.numAnswers.setText(answersCountFormatted);
            viewHolder.numAnswersContainer.setVisibility(View.VISIBLE);
            viewHolder.numAnswersSpacer.setVisibility(View.VISIBLE);
            viewHolder.numAnswersContainer.setTag(bazaarQuestion);
            viewHolder.numAnswersContainer.setOnClickListener(numAnswersClickListener);
        } else {
            viewHolder.numAnswersContainer.setVisibility(View.GONE);
            viewHolder.numAnswersSpacer.setVisibility(View.GONE);
            viewHolder.numAnswersContainer.setOnClickListener(null);
        }

        viewHolder.answerActionContainer.setTag(bazaarQuestion);
        viewHolder.answerActionContainer.setOnClickListener(answerActionClickListener);
    }

    private final View.OnClickListener numAnswersClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BazaarQuestion bazaarQuestion = (BazaarQuestion) v.getTag();
            // transition to answers activity
            DemoQuestionsActivity questionsActivity = (DemoQuestionsActivity) v.getContext();
            DemoAnswersActivity.transitionTo(questionsActivity, productId, bazaarQuestion.getId());
        }
    };

    private final View.OnClickListener answerActionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BazaarQuestion bazaarQuestion = (BazaarQuestion) v.getTag();
            // show answer question dialog
            showAnswerQuestionDialog((DemoQuestionsActivity) v.getContext());
        }
    };

    private void showAnswerQuestionDialog(AppCompatActivity appCompatActivity) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DemoSubmitDialogFragment.newInstance(appCompatActivity.getString(R.string.answer_body_hint));
        dialog.show(appCompatActivity.getSupportFragmentManager(), "SubmitQuestionDialogFragment");
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void refreshQuestions(List<BazaarQuestion> questions) {
        this.questionList = questions;
        notifyDataSetChanged();
    }

    private final class QuestionRowViewHolder extends RecyclerView.ViewHolder {

        TextView questionTitle, questionTimeAgo, questionBody, numAnswers, answerAction;
        ViewGroup numAnswersContainer, answerActionContainer;
        View numAnswersSpacer;
        TextView fontAwesomeNumAnswersIcon, fontAwesomeAnswerActionIcon;
        ProgressBar numAnswersLoading;

        public QuestionRowViewHolder(View itemView) {
            super(itemView);
            questionTitle = (TextView) itemView.findViewById(R.id.question_title);
            questionTimeAgo = (TextView) itemView.findViewById(R.id.question_time_ago);
            questionBody = (TextView) itemView.findViewById(R.id.question_body);
            numAnswers = (TextView) itemView.findViewById(R.id.num_answer_text);
            answerAction = (TextView) itemView.findViewById(R.id.answer_action_text);
            numAnswersSpacer = itemView.findViewById(R.id.spacer_2);
            numAnswersContainer = (ViewGroup) itemView.findViewById(R.id.num_answers_container);

            answerActionContainer = (ViewGroup) itemView.findViewById(R.id.answer_action_container);
            numAnswersLoading = (ProgressBar) itemView.findViewById(R.id.num_answers_loading);

            Typeface fontAwesomeFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fontawesome-webfont.ttf");
            fontAwesomeAnswerActionIcon = (TextView) itemView.findViewById(R.id.answer_action_icon);
            fontAwesomeAnswerActionIcon.setTypeface(fontAwesomeFont);
            fontAwesomeNumAnswersIcon = (TextView) itemView.findViewById(R.id.num_answers_icon);
            fontAwesomeNumAnswersIcon.setTypeface(fontAwesomeFont);
        }
    }
}
