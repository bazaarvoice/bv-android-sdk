/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.bulkratings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.ReviewStatistics;
import com.bazaarvoice.bvandroidsdk.Statistics;
import com.bazaarvoice.bvsdkdemoandroid.R;

import java.util.Collections;
import java.util.List;

public class DemoBulkRatingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Statistics> statistics = Collections.emptyList();

    public DemoBulkRatingsAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_basic_title_detail, parent, false);
        return new BasicRowViewHolder(reviewRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Statistics stat = statistics.get(position);
        BasicRowViewHolder viewHolder = (BasicRowViewHolder) holder;

        viewHolder.title.setText("Product ID: " + stat.getProductStatistics().getProductId());

        ReviewStatistics reviewStats = stat.getProductStatistics().getNativeReviewStatistics();

        String totalReviewCount = reviewStats.getTotalReviewCount() == null ? "0" : reviewStats.getTotalReviewCount().toString();
        String overallRatingRange = reviewStats.getOverallRatingRange() == null ? "0" : reviewStats.getOverallRatingRange().toString();
        String avgOverallRating = reviewStats.getAverageOverallRating() == null ? "0" : reviewStats.getAverageOverallRating().toString();

        String bodyText = "Total Review Count: " + totalReviewCount
                + "\nOverall Rating Range: " + overallRatingRange
                + "\nOverall Average Rating " + avgOverallRating;

        viewHolder.body.setText(bodyText);

    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }

    public void refreshReviews(List<Statistics> statistics) {
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
