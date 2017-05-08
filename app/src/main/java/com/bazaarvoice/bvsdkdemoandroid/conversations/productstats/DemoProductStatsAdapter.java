/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.productstats;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.QAStatistics;
import com.bazaarvoice.bvandroidsdk.ReviewStatistics;
import com.bazaarvoice.bvsdkdemoandroid.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.List;

public class DemoProductStatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> statistics = Collections.emptyList();
    private PrettyTime prettyTime;

    public DemoProductStatsAdapter() {
        this.prettyTime = new PrettyTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View displayRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_basic_title_detail, parent, false);
        return new BasicRowViewHolder(displayRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Product product = statistics.get(position);
        BasicRowViewHolder viewHolder = (BasicRowViewHolder) holder;

        viewHolder.title.setText("Product Stats for: " + product.getDisplayName());

        // Here we'll just build up some structured text so you can see what
        // sort of statistical data you can get from a product id.

        ReviewStatistics reviewStats = product.getReviewStatistics();
        QAStatistics qaStats = product.getQaStatistics();

        StringBuilder bodyText = new StringBuilder()
            .append("Product Review Statistics\n")
            .append("----------------------------------\n")
            .append("\tTotal Review Count: ")
            .append(reviewStats.getTotalReviewCount().toString())
            .append("\n")
            .append("\tAverage Overall Rating: ")
            .append(reviewStats.getAverageOverallRating().toString())
            .append("\n")
            .append("\tHelpful Vote Count: ")
            .append(reviewStats.getHelpfulVoteCount().toString())
            .append("\n")
            .append("\tNonHelpful Vote Count: ")
            .append(reviewStats.getNotHelpfulVoteCount().toString())
            .append("\n")
            .append("\tRecommended Count: ")
            .append(reviewStats.getRecommendedCount().toString())
            .append("\n")
            .append("\tNot Recommended Count: ")
            .append(reviewStats.getNotRecommendedCount().toString())
            .append("\n")
            .append("\tOverall Rating Range: ")
            .append(reviewStats.getOverallRatingRange().toString())
            .append("\n")
            .append("\n");

        if (qaStats != null) {
            bodyText.append("Question & Answer Statistics\n")
                .append("----------------------------------\n")
                .append("\tTotal Questions: ")
                .append(qaStats.getTotalQuestionCount().toString())
                .append("\n")
                .append("\tTotal Answers: ")
                .append(qaStats.getTotalAnswerCount().toString())
                .append("\n")
                .append("\tAnswer Helpful Vote Count: ")
                .append(qaStats.getAnswerHelpfulVoteCount().toString())
                .append("\n")
                .append("\tAnswer Not Helpful Vote Count: ")
                .append(qaStats.getAnswerNotHelpfulVoteCount().toString())
                .append("\n").append("\tQuestion Helpful Vote Count: ")
                .append(qaStats.getQuestionHelpfulVoteCount().toString()).append("\n")
                .append("\tQuestion Not Helpful Vote Count: ")
                .append(qaStats.getQuestionNotHelpfulVoteCount().toString()).append("\n")
                .append("");
        }

        viewHolder.body.setText(bodyText.toString());
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }

    public void refreshReviews(List<Product> statistics) {
        this.statistics = statistics;
        notifyDataSetChanged();
    }

    private final class BasicRowViewHolder extends RecyclerView.ViewHolder {

        TextView title, body;

        public BasicRowViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.basic_row_title);
            body = (TextView) itemView.findViewById(R.id.basic_row_body);
        }
    }
}
