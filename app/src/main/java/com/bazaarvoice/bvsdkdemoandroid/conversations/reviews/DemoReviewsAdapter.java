/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.BazaarReview;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoUtils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DemoReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BazaarReview> reviews = Collections.emptyList();
    private PrettyTime prettyTime;
    private SimpleDateFormat sdf;

    public DemoReviewsAdapter() {
        this.prettyTime = new PrettyTime();
        this.sdf = new SimpleDateFormat("MMM dd, yyyy");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_review, parent, false);
        return new ReviewRowViewHolder(reviewRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BazaarReview bazaarReview = reviews.get(position);
        ReviewRowViewHolder viewHolder = (ReviewRowViewHolder) holder;

        viewHolder.reviewTitle.setText(bazaarReview.getTitle());
        viewHolder.reviewBody.setText(bazaarReview.getReviewText());
        viewHolder.reviewRating.setRating(bazaarReview.getRating());

        boolean hasLocation = !TextUtils.isEmpty(bazaarReview.getUserLocation()) && !bazaarReview.getUserLocation().equals("null");
        if (hasLocation) {
            viewHolder.reviewLocation.setText(bazaarReview.getUserLocation());
            viewHolder.reviewLocation.setVisibility(View.VISIBLE);
        } else {
            viewHolder.reviewLocation.setVisibility(View.GONE);
        }

        boolean hasTimeAgo = !TextUtils.isEmpty(bazaarReview.getDateString());
        if (hasTimeAgo) {
            try {
                Date date = sdf.parse(bazaarReview.getDateString());
                String timeAgo = prettyTime.format(date);
                boolean hasUserNickname = !TextUtils.isEmpty(bazaarReview.getUserNickname());
                String timeAgoBy = hasUserNickname ? timeAgo + " by " + bazaarReview.getUserNickname() : timeAgo;
                viewHolder.reviewTimeAgo.setText(timeAgoBy);
                viewHolder.reviewTimeAgo.setVisibility(View.VISIBLE);
            } catch (ParseException e) {
                e.printStackTrace();
                viewHolder.reviewTimeAgo.setVisibility(View.GONE);
            }
        } else {
            viewHolder.reviewTimeAgo.setVisibility(View.GONE);
        }

        boolean hasImage = !TextUtils.isEmpty(bazaarReview.getImageUrl());
        if (hasImage) {
            viewHolder.reviewImage.setVisibility(View.VISIBLE);
            DemoUtils demoUtils = DemoUtils.getInstance(viewHolder.reviewImage.getContext());
            demoUtils.picassoThumbnailLoader().load(bazaarReview.getImageUrl()).into(viewHolder.reviewImage);
        } else {
            viewHolder.reviewImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void refreshReviews(List<BazaarReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    private final class ReviewRowViewHolder extends RecyclerView.ViewHolder {

        TextView reviewTitle, reviewTimeAgo, reviewLocation, reviewBody;
        RatingBar reviewRating;
        ImageView reviewImage;

        public ReviewRowViewHolder(View itemView) {
            super(itemView);
            reviewTitle = (TextView) itemView.findViewById(R.id.review_title);
            reviewTimeAgo = (TextView) itemView.findViewById(R.id.review_time_ago);
            reviewLocation = (TextView) itemView.findViewById(R.id.review_location);
            reviewBody = (TextView) itemView.findViewById(R.id.review_body);
            reviewRating = (RatingBar) itemView.findViewById(R.id.review_rating);
            reviewImage = (ImageView) itemView.findViewById(R.id.review_header_info_image);
        }
    }
}
