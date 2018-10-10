/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.conversations.reviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bazaarvoice.bvandroidsdk.BVConversationsClient;
import com.bazaarvoice.bvandroidsdk.BaseReview;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionCallback;
import com.bazaarvoice.bvandroidsdk.ConversationsSubmissionException;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionRequest;
import com.bazaarvoice.bvandroidsdk.FeedbackSubmissionResponse;
import com.bazaarvoice.bvandroidsdk.Photo;
import com.bazaarvoice.bvandroidsdk.Store;
import com.bazaarvoice.bvandroidsdk.StoreReview;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.text.util.LinkifyCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DemoReviewsAdapter<ReviewType extends BaseReview> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  List<ReviewType> reviews = Collections.emptyList();
  private PrettyTime prettyTime;
  private Picasso picasso;
  private final BVConversationsClient client;

  public DemoReviewsAdapter(Picasso picasso, PrettyTime prettyTime, BVConversationsClient client) {
    this.picasso = picasso;
    this.prettyTime = prettyTime;
    this.client = client;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View reviewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_review, parent, false);
    return new ReviewRowViewHolder(reviewRow);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ReviewType reviewItem = reviews.get(position);
    ReviewRowViewHolder viewHolder = (ReviewRowViewHolder) holder;

    viewHolder.reviewTitle.setText(reviewItem.getTitle());
    viewHolder.reviewBody.setText(reviewItem.getReviewText());
    viewHolder.reviewRating.setRating(reviewItem.getRating());

    setFeedbackViewItems(reviewItem, viewHolder);
    setCommentViewItems(reviewItem, viewHolder);
    setLocationViewItems(reviewItem, viewHolder);
    setTimeAgoViewItems(reviewItem, viewHolder);
    setImageViewItems(reviewItem, viewHolder);
  }

  private void setImageViewItems(ReviewType reviewItem, ReviewRowViewHolder viewHolder) {
    List<Photo> photos = reviewItem.getPhotos();
    boolean hasImage = photos != null && photos.size() > 0;
    if (hasImage) {
      viewHolder.reviewImage.setVisibility(View.VISIBLE);
      picasso.load(photos.get(0).getContent().getThumbnailUrl()).into(viewHolder.reviewImage);
    } else {
      viewHolder.reviewImage.setVisibility(View.GONE);
    }
  }

  private void setLocationViewItems(ReviewType reviewItem, ReviewRowViewHolder viewHolder) {
    final String location;
    if (reviewItem instanceof StoreReview) {
      StoreReview storeReview = (StoreReview) reviewItem;
      Store store = storeReview.getIncludedIn().getStores().get(0);
      location = store.getLocationAttribute(Store.StoreAttributeType.CITY);
    } else {
      location = reviewItem.getUserLocation();
    }

    boolean hasLocation = !TextUtils.isEmpty(location) && !location.equals("null");
    if (hasLocation) {
      viewHolder.reviewLocation.setText(location);
      viewHolder.reviewLocation.setVisibility(View.VISIBLE);
    } else {
      viewHolder.reviewLocation.setVisibility(View.GONE);
    }
  }

  private void setTimeAgoViewItems(final ReviewType reviewItem, final ReviewRowViewHolder viewHolder) {
    final Date submissionDate = reviewItem.getSubmissionDate();
    final String nickName = reviewItem.getUserNickname();
    final String authorId = reviewItem.getAuthorId();

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
              DemoAuthorActivity.transitionTo(tv.getContext(), authorId);
            }
          }
        });
      }
    } else {
      viewHolder.reviewTimeAgo.setVisibility(View.GONE);
    }
  }

  private void setCommentViewItems(final ReviewType reviewItem, final ReviewRowViewHolder viewHolder) {
    final Resources res = viewHolder.commentsImage.getResources();

    int commentCount = reviewItem.getTotalCommentCount();
    if (commentCount > 0) {
      viewHolder.commentsImage.setColorFilter(res.getColor(R.color.bv_green_2));
      viewHolder.commentsImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Context activityContext = viewHolder.commentsImage.getContext();
          Activity activity = (Activity) activityContext;
          if (activity != null && !activity.isFinishing()) {
            DemoCommentsActivity.transitionToCommentsActivity(activity, reviewItem.getId(), false);
          }
        }
      });
    } else {
      viewHolder.commentsImage.setColorFilter(res.getColor(R.color.bv_gray_1));
      viewHolder.commentsImage.setOnClickListener(null);
    }

    String commentCountStr = String.valueOf(commentCount);
    viewHolder.commentCountText.setText(commentCountStr);
  }

  private void setFeedbackViewItems(final ReviewType reviewItem, final ReviewRowViewHolder viewHolder){
    final Resources res = viewHolder.helpfulImage.getResources();

    viewHolder.thumbsDownCountText.setText(String.valueOf(reviewItem.getTotalNegativeFeedbackCount()));
    viewHolder.thumbsUpCountText.setText(String.valueOf(reviewItem.getTotalPositiveFeedbackCount()));
    viewHolder.helpfulImage.setColorFilter(res.getColor(R.color.bv_gray_1));
    viewHolder.notHelpfulImage.setColorFilter(res.getColor(R.color.bv_gray_1));
    viewHolder.helpfulImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int currentCount = Integer.valueOf((String) viewHolder.thumbsUpCountText.getText());
        int newCount = currentCount + 1;
        String newCountStr = String.valueOf(newCount);
        viewHolder.thumbsUpCountText.setText(newCountStr);
        viewHolder.helpfulImage.setColorFilter(res.getColor(R.color.bv_green_2));
        viewHolder.helpfulImage.setOnClickListener(null);
        viewHolder.notHelpfulImage.setOnClickListener(null);
        submitHelpfulnessVote(reviewItem, FeedbackVoteType.POSITIVE, viewHolder);
      }
    });

    viewHolder.notHelpfulImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int currentCount = Integer.valueOf((String) viewHolder.thumbsDownCountText.getText());
        int newCount = currentCount + 1;
        String newCountStr = String.valueOf(newCount);
        viewHolder.thumbsDownCountText.setText(newCountStr);
        viewHolder.notHelpfulImage.setColorFilter(res.getColor(R.color.bv_green_2));
        viewHolder.helpfulImage.setOnClickListener(null);
        viewHolder.notHelpfulImage.setOnClickListener(null);
        submitHelpfulnessVote(reviewItem, FeedbackVoteType.NEGATIVE, viewHolder);
      }
    });
  }

  private void submitHelpfulnessVote(final ReviewType reviewItem, final FeedbackVoteType vote, final ReviewRowViewHolder viewHolder){
    FeedbackSubmissionRequest submission = new FeedbackSubmissionRequest.Builder(reviewItem.getId())
        .userId("user1234" + Math.random()) // Creating a random user id to avoid duplicates -- FOR TESTING ONLY!!!
        .feedbackType(FeedbackType.HELPFULNESS)
        .feedbackContentType(FeedbackContentType.REVIEW)
        .feedbackVote(vote)
        .build();

    client.prepareCall(submission).loadAsync(new ConversationsSubmissionCallback<FeedbackSubmissionResponse>() {
      @Override
      public void onSuccess(@NonNull FeedbackSubmissionResponse response) {

      }

      @Override
      public void onFailure(@NonNull ConversationsSubmissionException exception) {

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
    AppCompatImageView helpfulImage, notHelpfulImage, commentsImage;
    TextView helpfulTextHeader, thumbsUpCountText, thumbsDownCountText, commentCountText;

    public ReviewRowViewHolder(View itemView) {
      super(itemView);
      reviewTitle = itemView.findViewById(R.id.reviewTitleText);
      reviewTimeAgo = itemView.findViewById(R.id.reviewTimeAgoText);
      reviewLocation = itemView.findViewById(R.id.reviewLocationText);
      reviewBody = itemView.findViewById(R.id.reviewBodyText);
      reviewRating = itemView.findViewById(R.id.reviewRatingBar);
      reviewImage = itemView.findViewById(R.id.reviewImage);
      helpfulImage = itemView.findViewById(R.id.thumbsUpImage);
      notHelpfulImage = itemView.findViewById(R.id.thumbsDownImage);
      commentsImage = itemView.findViewById(R.id.commentsImage);
      helpfulTextHeader = itemView.findViewById(R.id.reviewHelpfulText);
      thumbsUpCountText = itemView.findViewById(R.id.thumbUpCountText);
      thumbsDownCountText = itemView.findViewById(R.id.thumbsDownCountText);
      commentCountText = itemView.findViewById(R.id.commentsCountText);
    }
  }
}