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

        String bodyText = "Product Review Statistics\n" +
                "----------------------------------\n" +
                "\tTotal Review Count: " + reviewStats.getTotalReviewCount().toString() + "\n" +
                "\tAverage Overall Rating: " + reviewStats.getAverageOverallRating().toString() + "\n" +
                "\tHelpful Vote Count: " + reviewStats.getHelpfulVoteCount().toString() + "\n" +
                "\tNonHelpful Vote Count: " + reviewStats.getNotHelpfulVoteCount().toString() + "\n" +
                "\tRecommended Count: " + reviewStats.getRecommendedCount().toString() + "\n" +
                "\tNot Recommended Count: " + reviewStats.getNotRecommendedCount().toString() + "\n" +
                "\tOverall Rating Range: " + reviewStats.getOverallRatingRange().toString() + "\n" +
                "\n" +
                "Question & Answer Statistics\n" +
                "----------------------------------\n" +
                "\tTotal Questions: " + qaStats.getTotalQuestionCount().toString() + "\n" +
                "\tTotal Answers: " + qaStats.getTotalAnswerCount().toString() + "\n" +
                "\tAnswer Helpful Vote Count: " + qaStats.getAnswerHelpfulVoteCount().toString() + "\n" +
                "\tAnswer Not Helpful Vote Count: " + qaStats.getAnswerNotHelpfulVoteCount().toString() + "\n" +
                "\tQuestion Helpful Vote Count: " + qaStats.getQuestionHelpfulVoteCount().toString() + "\n" +
                "\tQuestion Not Helpful Vote Count: " + qaStats.getQuestionNotHelpfulVoteCount().toString() + "\n" +
                "";


        viewHolder.body.setText(bodyText);
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
