package com.bazaarvoice.example.browseproducts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * ReviewDisplayActivity.java <br>
 * BrowseProductExample<br>
 * 
 * <p>
 * This is a simple review display screen. It recieves a BazaarReview through an
 * Intent and displays it.
 * 
 * <p>
 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class ReviewDisplayActivity extends Activity {

	private BazaarReview selectedReview;

	/**
	 * Unpacks a BazaarReview from either the Intent or savedInstanceState
	 * depending on whether this is a new or recycled activity, and displays it.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_display);

		if (savedInstanceState != null) {
			selectedReview = savedInstanceState.getParcelable("review");
		} else {
			Intent myIntent = getIntent();
			selectedReview = myIntent.getParcelableExtra("review");
		}

		initializeViews();
	}

	/**
	 * Saves the review for recycling.
	 */
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putParcelable("review", selectedReview);
	}

	 /**
	  * Sets up the UI and populates it with the data from the review.
	  */
	private void initializeViews() {
		TextView reviewTitle = (TextView) findViewById(R.id.reviewTitle);
		reviewTitle.setText(selectedReview.getTitle());

		ImageView reviewImage = (ImageView) findViewById(R.id.reviewImage);
		if (selectedReview.getImageBitmap() != null) {
			Bitmap scaledImage = Bitmap.createScaledBitmap(
					selectedReview.getImageBitmap(), 250, 250, true);
			reviewImage.setImageBitmap(scaledImage);
		}

		TextView byLine = (TextView) findViewById(R.id.byLine);
		byLine.setText(Html.fromHtml("By <b>" + selectedReview.getAuthorId()
				+ "</b> on " + selectedReview.getDateString()));

		RatingBar reviewRating = (RatingBar) findViewById(R.id.reviewRating);
		reviewRating.setRating(selectedReview.getRating());

		TextView reviewText = (TextView) findViewById(R.id.reviewText);
		reviewText.setText(selectedReview.getReviewText());
	}

}
