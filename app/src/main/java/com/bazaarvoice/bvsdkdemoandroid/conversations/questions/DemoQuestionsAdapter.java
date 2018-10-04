/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.questions;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Question;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.author.DemoAuthorActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.answers.DemoAnswersActivity;
import com.bazaarvoice.bvsdkdemoandroid.detail.DemoSubmitDialogFragment;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.util.LinkifyCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class DemoQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Question> questionList = Collections.emptyList();
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
        Question bazaarQuestion = questionList.get(position);
        QuestionRowViewHolder viewHolder = (QuestionRowViewHolder) holder;

        viewHolder.questionTitle.setText(bazaarQuestion.getQuestionSummary());

        boolean hasBody = !TextUtils.isEmpty(bazaarQuestion.getQuestionDetails()) && !bazaarQuestion.getQuestionDetails().equals("null");
        if (hasBody) {
            viewHolder.questionBody.setText(bazaarQuestion.getQuestionDetails());
            viewHolder.questionBody.setVisibility(View.VISIBLE);
        } else {
            viewHolder.questionBody.setVisibility(View.GONE);
        }

        boolean hasTimeAgo = bazaarQuestion.getSubmissionDate() != null;
        if (hasTimeAgo) {
            String timeAgo = prettyTime.format(bazaarQuestion.getSubmissionDate());
            boolean hasUserNickname = !TextUtils.isEmpty(bazaarQuestion.getUserNickname());
            final String nickName = bazaarQuestion.getUserNickname();
            final String authorId = bazaarQuestion.getAuthorId();
            String timeAgoBy = hasUserNickname ? timeAgo + " by " + nickName : timeAgo;
            viewHolder.questionTimeAgo.setText(timeAgoBy);
            viewHolder.questionTimeAgo.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(nickName)) {
                Pattern authorPattern = Pattern.compile(nickName);
                LinkifyCompat.addLinks(viewHolder.questionTimeAgo, authorPattern, null);
                viewHolder.questionTimeAgo.setOnClickListener(new View.OnClickListener() {
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
            Question bazaarQuestion = (Question) v.getTag();
            // transition to answers activity
            DemoQuestionsActivity questionsActivity = (DemoQuestionsActivity) v.getContext();
            DemoAnswersActivity.transitionTo(questionsActivity, productId, bazaarQuestion.getId());
        }
    };

    private final View.OnClickListener answerActionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Question bazaarQuestion = (Question) v.getTag();
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

    public void refreshQuestions(List<Question> questions) {
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
