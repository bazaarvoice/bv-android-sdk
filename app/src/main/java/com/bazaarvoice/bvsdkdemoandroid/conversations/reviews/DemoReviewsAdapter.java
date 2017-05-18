/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.app.Activity;
import android.content.Context;
import android.support.v4.text.util.LinkifyCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BaseReview;
import com.bazaarvoice.bvandroidsdk.BazaarException;
import com.bazaarvoice.bvandroidsdk.ConversationsCallback;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.Photo;
import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.author.DemoAuthorActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.comments.DemoCommentsActivity;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DemoReviewsAdapter<ReviewType extends BaseReview> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ReviewType> reviews = Collections.emptyList();
    private PrettyTime prettyTime;
    private Picasso picasso;

    public DemoReviewsAdapter(Picasso picasso, PrettyTime prettyTime) {
        this.picasso = picasso;
        this.prettyTime = prettyTime;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View reviewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_review, parent, false);
        return new ReviewRowViewHolder(reviewRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReviewType bazaarReview = reviews.get(position);
        ReviewRowViewHolder viewHolder = (ReviewRowViewHolder) holder;

        String title = bazaarReview.getTitle();
        String reviewText = bazaarReview.getReviewText();
        Integer rating = bazaarReview.getRating();
        String location = bazaarReview.getUserLocation();
        Date submissionDate= bazaarReview.getSubmissionDate();
        String nickName = bazaarReview.getUserNickname();
        List<Photo> photos = bazaarReview.getPhotos();
        Integer helpfulVoteCount = bazaarReview.getTotalPositiveFeedbackCount();
        Integer notHelpfulVoteCount = bazaarReview.getTotalNegativeFeedbackCount();
        String authorId = bazaarReview.getAuthorId();
        int commentCount = bazaarReview.getTotalCommentCount();

        populateViewHolder(bazaarReview, viewHolder, title, reviewText, rating, location,
                submissionDate, nickName, photos, helpfulVoteCount, notHelpfulVoteCount, authorId, commentCount);
    }

    void populateViewHolder(final ReviewType reviewItem, final ReviewRowViewHolder viewHolder, String title, String reviewText, Integer rating, String location,
                            Date submissionDate, final String nickName, List<Photo> photos, final Integer helpfulVoteCount, final Integer notHelpfulVoteCount, final String authorId, int commentCount) {

        viewHolder.reviewTitle.setText(title);
        viewHolder.reviewBody.setText(reviewText);
        viewHolder.reviewRating.setRating(rating == null ? 0 : rating);

        if (TextUtils.isEmpty(reviewText) || helpfulVoteCount < 0){
            viewHolder.notHelpfulButton.setVisibility(View.GONE);
            viewHolder.helpfulButton.setVisibility(View.GONE);
            viewHolder.helpfulTextHeader.setVisibility(View.GONE);
        } else {
            setHelpfulnessVoteCountText(reviewItem.getTotalPositiveFeedbackCount(), reviewItem.getTotalNegativeFeedbackCount(), viewHolder);

            viewHolder.helpfulButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitHelpfulnessVote(reviewItem, FeedbackVoteType.POSITIVE, viewHolder);
                }
            });

            viewHolder.notHelpfulButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitHelpfulnessVote(reviewItem, FeedbackVoteType.NEGATIVE, viewHolder);
                }
            });

        }

        if (commentCount > 0) {
            viewHolder.commentsButton.setVisibility(View.VISIBLE);
            String commentCountTemplate = viewHolder.commentsButton.getResources().getString(R.string.conv_comments_count);
            String commentCountStr = String.format(commentCountTemplate, commentCount);
            viewHolder.commentsButton.setText(commentCountStr);
            viewHolder.commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context activityContext = viewHolder.commentsButton.getContext();
                    Activity activity = (Activity) activityContext;
                    if (activity != null && !activity.isFinishing()) {
                        DemoCommentsActivity.transitionToCommentsActivity(activity, reviewItem.getId(), false);
                    }
                }
            });
        } else {
            viewHolder.commentsButton.setVisibility(View.GONE);
            viewHolder.commentsButton.setOnClickListener(null);
        }

        boolean hasLocation = !TextUtils.isEmpty(location) && !location.equals("null");
        if (hasLocation) {
            viewHolder.reviewLocation.setText(location);
            viewHolder.reviewLocation.setVisibility(View.VISIBLE);
        } else {
            viewHolder.reviewLocation.setVisibility(View.GONE);
        }

        boolean hasTimeAgo = submissionDate != null;
        if (hasTimeAgo) {
            String timeAgo = prettyTime.format(submissionDate);
            boolean hasUserNickname = !TextUtils.isEmpty(nickName);
            String timeAgoBy = hasUserNickname ? timeAgo + " by " + nickName : timeAgo;
            viewHolder.reviewTimeAgo.setText(timeAgoBy);
            viewHolder.reviewTimeAgo.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(nickName)) {
                Pattern authorPattern = Pattern.compile(nickName);
                LinkifyCompat.addLinks(viewHolder.reviewTimeAgo, authorPattern, null);
                viewHolder.reviewTimeAgo.setOnClickListener(new View.OnClickListener() {
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
            viewHolder.reviewTimeAgo.setVisibility(View.GONE);
        }

        boolean hasImage = photos != null ? photos.size() > 0 : false;
        if (hasImage) {
            viewHolder.reviewImage.setVisibility(View.VISIBLE);
            picasso.load(photos.get(0).getContent().getThumbnailUrl()).into(viewHolder.reviewImage);
        } else {
            viewHolder.reviewImage.setVisibility(View.GONE);
        }
    }

    private void setHelpfulnessVoteCountText(Integer positiveReviewCount, Integer negativeReviewCount, ReviewRowViewHolder viewHolder){

        viewHolder.notHelpfulButton.setText("Not Helpful (" + negativeReviewCount + ")");
        viewHolder.helpfulButton.setText("Helpful (" + positiveReviewCount + ")");

    }

    private void submitHelpfulnessVote(final ReviewType reviewItem, final FeedbackVoteType vote, final ReviewRowViewHolder viewHolder){

        viewHolder.notHelpfulButton.setClickable(false);
        viewHolder.helpfulButton.setClickable(false);

        viewHolder.notHelpfulButton.setAlpha(0.5f);
        viewHolder.helpfulButton.setAlpha(0.5f);

        Integer posReviewCount = reviewItem.getTotalPositiveFeedbackCount();
        Integer negReviewCount = reviewItem.getTotalNegativeFeedbackCount();

        if (vote == FeedbackVoteType.POSITIVE){
            posReviewCount++;
        } else {
            negReviewCount++;
        }

        setHelpfulnessVoteCountText(posReviewCount, negReviewCount, viewHolder);

        final Integer finalPosReviewCount = posReviewCount;
        final Integer finalNegReviewCount = negReviewCount;

        FeedbackSubmissionRequest submission = new FeedbackSubmissionRequest.Builder(reviewItem.getId())
                .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicates -- FOR TESTING ONLY!!!
                .feedbackType(FeedbackType.HELPFULNESS)
                .feedbackContentType(FeedbackContentType.REVIEW)
                .feedbackVote(vote)
                .build();

        BVConversationsClient client = new BVConversationsClient();
        client.prepareCall(submission).loadAsync(new ConversationsCallback<FeedbackSubmissionResponse>() {

            @Override
            public void onSuccess(FeedbackSubmissionResponse response) {
                // Yeah! Success, no do nothing!
            }

            @Override
            public void onFailure(BazaarException exception) {
               //  Rollback
                viewHolder.notHelpfulButton.setClickable(true);
                viewHolder.helpfulButton.setClickable(true);

                viewHolder.notHelpfulButton.setAlpha(1.0f);
                viewHolder.helpfulButton.setAlpha(1.0f);

                if (vote == FeedbackVoteType.POSITIVE){
                    setHelpfulnessVoteCountText(finalPosReviewCount - 1, finalNegReviewCount, viewHolder);
                } else {
                    setHelpfulnessVoteCountText(finalPosReviewCount, finalNegReviewCount - 1, viewHolder);
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void refreshReviews(List<ReviewType> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    protected final class ReviewRowViewHolder extends RecyclerView.ViewHolder {

        TextView reviewTitle, reviewTimeAgo, reviewLocation, reviewBody;
        RatingBar reviewRating;
        ImageView reviewImage;
        Button helpfulButton, notHelpfulButton, commentsButton;
        TextView helpfulTextHeader;

        public ReviewRowViewHolder(View itemView) {
            super(itemView);
            reviewTitle = (TextView) itemView.findViewById(R.id.review_title);
            reviewTimeAgo = (TextView) itemView.findViewById(R.id.review_time_ago);
            reviewLocation = (TextView) itemView.findViewById(R.id.review_location);
            reviewBody = (TextView) itemView.findViewById(R.id.review_body);
            reviewRating = (RatingBar) itemView.findViewById(R.id.review_rating);
            reviewImage = (ImageView) itemView.findViewById(R.id.review_header_info_image);
            helpfulButton = (Button) itemView.findViewById(R.id.helpfulButton);
            notHelpfulButton = (Button) itemView.findViewById(R.id.notHelpfulButton);
            commentsButton = (Button) itemView.findViewById(R.id.commentsButton);
            helpfulTextHeader = (TextView) itemView.findViewById(R.id.helpfulTextView);
        }
    }
}