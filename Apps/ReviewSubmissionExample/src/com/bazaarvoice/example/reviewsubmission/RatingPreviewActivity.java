package com.bazaarvoice.example.reviewsubmission;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

/**
 * RatingPreviewActivity.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is a simple display showing the user that their review has been
 * submitted and what it looks like. There is a simple "Thank You" animation as
 * well.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class RatingPreviewActivity extends Activity {

	private ViewAnimator thankYou;

	/**
	 * Called when the activity is first created. All of this activity's
	 * information is sent over in an Intent, so it must be unpacked.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		CameraUtils.clearPhotos();

		Intent myIntent = getIntent();
		initializeViews(myIntent);
	}

	/**
	 * Changes the action of the back button to restart the app. We don't want
	 * the user to be able to go back to the submission form after submitting.
	 */
	@Override
	public void onBackPressed() {
		Intent i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

	/**
	 * Sets up the UI elements and listeners.
	 * 
	 * @param myIntent
	 *            the intent that launched this activity
	 */
	private void initializeViews(Intent myIntent) {
		thankYou = (ViewAnimator) findViewById(R.id.thankYou);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(2000);
		thankYou.setInAnimation(animation);
		animation = new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(2000);
		thankYou.setOutAnimation(animation);
		thankYou.setAnimateFirstView(true);
		thankYou.setDisplayedChild(0);
		setAnimationTimer(1, 4000);

		TextView reviewTitle = (TextView) findViewById(R.id.reviewTitle);
		TextView reviewNickname = (TextView) findViewById(R.id.reviewNickname);
		TextView reviewText = (TextView) findViewById(R.id.reviewText);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ImageView reviewPhoto = (ImageView) findViewById(R.id.reviewPhoto);

		reviewTitle.setText(myIntent.getStringExtra("reviewTitle"));
		reviewNickname.setText(Html.fromHtml("By <b>"
				+ myIntent.getStringExtra("reviewNickname") + "</b> "));
		reviewText.setText(myIntent.getStringExtra("reviewText"));
		ratingBar.setRating(myIntent.getIntExtra("reviewRating", 3));
		byte[] byteArray = myIntent.getByteArrayExtra("displayImage");
		reviewPhoto.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length));
	}

	/**
	 * Sets a timer to transition to a child of the ViewAnimator after a certain
	 * delay.
	 * 
	 * @param childNum
	 *            the child we want to transition to
	 * @param delay
	 *            the delay in milliseconds before firing
	 */
	private void setAnimationTimer(final int childNum, int delay) {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						thankYou.setDisplayedChild(childNum);
					}

				});
			}

		};
		Timer timer = new Timer();
		timer.schedule(task, delay);
	}

}
