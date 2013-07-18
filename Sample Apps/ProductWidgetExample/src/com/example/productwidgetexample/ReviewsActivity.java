package com.example.productwidgetexample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ReviewsActivity.java <br>
 * ProductWidgetExample<br>
 * 
 * <p>
 * This is a review browsing screen. It loads the product image and review
 * content on creation, and utilizes savedInstanceState to limit the amount of
 * data usage by restoring the information we previously downloaded.
 * 
 * <p>
 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class ReviewsActivity extends Activity {

	private BazaarProduct selectedProduct;
	private ListView reviewList;
	private TextView noReviews;
	private ReviewAdapter listAdapter;
	private ProgressDialog progDialog;
	protected String TAG = "ReviewsActivity";
	protected int imageCounter;

	/**
	 * Pulls the product out of the Intent if this is a new instance, or the
	 * savedInstanceState if this is a recycled instance and does any other
	 * necessary work needed to set up the display.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviews);

		Intent myIntent = getIntent();
		if (myIntent != null) {
			selectedProduct = myIntent.getParcelableExtra("product");

			initializeViews();
			progDialog.setCancelable(false);
			progDialog.show();
			downloadProductImage();
			if (selectedProduct.getNumReviews() != 0) {
				downloadReviews();
			} else {
				noReviews.setVisibility(View.VISIBLE);
			}

		} else if (savedInstanceState != null) {
			loadFromSavedState(savedInstanceState);
		}
	}

	/**
	 * Loads the content of the activity from a saved state.
	 * 
	 * @param savedInstanceState
	 *            the state saved in {@link #onSaveInstanceState(Bundle)}.
	 */
	private void loadFromSavedState(Bundle savedInstanceState) {
		selectedProduct = savedInstanceState.getParcelable("product");
		ImageView productImage = (ImageView) findViewById(R.id.productImage);
		if (selectedProduct.getImageBitmap() != null) {
			Bitmap scaledImage = Bitmap.createScaledBitmap(
					selectedProduct.getImageBitmap(), 250, 250, true);
			productImage.setImageBitmap(scaledImage);
		} else {
			Log.i(TAG, "Downloading new product image");
			downloadProductImage();
		}
		initializeViews();
	}

	/**
	 * Saves the current selected product and all of its review information.
	 * 
	 * @param bundle
	 *            the bundle that is stored when the activity closes
	 */
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putParcelable("product", selectedProduct);
	}

	/**
	 * Sets up the views and puts an OnItemClickListener on the review list.
	 */
	private void initializeViews() {
		TextView productTitle = (TextView) findViewById(R.id.productTitle);
		productTitle.setText(selectedProduct.getName());

		TextView productDescription = (TextView) findViewById(R.id.productDescription);
		productDescription.setText(selectedProduct.getDescription());

		TextView avgRating = (TextView) findViewById(R.id.avgRating);
		double rating = selectedProduct.getAverageRating();
		avgRating
				.setText("Average rating of "
						+ (rating == -1.0 ? 0 : String.format("%.2f", rating))
						+ "/5 based on " + selectedProduct.getNumReviews()
						+ " reviews");

		noReviews = (TextView) findViewById(R.id.noReviews);

		listAdapter = new ReviewAdapter(this, selectedProduct.getReviews());
		reviewList = (ListView) findViewById(R.id.reviewList);
		reviewList.setAdapter(listAdapter);

		progDialog = new ProgressDialog(this);
		progDialog.setMessage("Getting reviews...");
	}

	/**
	 * Sends a request to download the image and updates the UI when it
	 * finishes.
	 */
	private void downloadProductImage() {
		selectedProduct.downloadImage(new OnImageDownloadComplete() {

			@Override
			public void onFinish(final Bitmap image) {
				// Protect from image download errors
				if (image == null) {
					downloadProductImage();
					return;
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ImageView productImage = (ImageView) findViewById(R.id.productImage);
						Bitmap scaledImage = Bitmap.createScaledBitmap(image,
								250, 250, true);
						productImage.setImageBitmap(scaledImage);
						listAdapter.notifyDataSetChanged();

						/*
						 * If there are no reviews, there is nothing more to
						 * wait for.
						 */
						if (selectedProduct.getNumReviews() == 0) {
							progDialog.dismiss();
						}
					}

				});
			}

		});
	}

	/**
	 * Sends off a request for reviews and displays them on response.
	 */
	private void downloadReviews() {
		selectedProduct.downloadReviews(new BazaarUIThreadResponse(this) {

			@Override
			public void onUiResponse(JSONObject json) {
				Log.i(TAG, "Response = \n" + json);
				try {
					displayReviews(json);
				} catch (JSONException exception) {
					Log.e(TAG,
							"Error = " + exception.getMessage() + "\n"
									+ Log.getStackTraceString(exception));
				}
			}

		});
	}

	/**
	 * Parses the json response and saves all of the review information,
	 * downloading images as needed. When all of the images are downloaded, the
	 * loading dialog is dismissed.
	 * 
	 * @param json
	 *            a response to a review query
	 * @throws JSONException
	 *             if a field is missing
	 */
	protected void displayReviews(JSONObject json) throws JSONException {
		JSONArray results = json.getJSONArray("Results");
		for (int i = 0; i < results.length(); i++) {
			BazaarReview newReview = new BazaarReview(results.getJSONObject(i));
			selectedProduct.addReview(newReview);
			boolean imageDownloadStarted = newReview
					.downloadImage(new OnImageDownloadComplete() {

						@Override
						public void onFinish(Bitmap image) {
							imageCounter--;

							if (imageCounter == 0) {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										listAdapter.notifyDataSetChanged();
										progDialog.dismiss();
									}

								});
							}
						}

					});
			if (imageDownloadStarted) {
				imageCounter++;
			}
		}

		/*
		 * No image downloads were started, we are done waiting
		 */
		if (imageCounter == 0) {
			listAdapter.notifyDataSetChanged();
			progDialog.dismiss();
		}
	}

}
