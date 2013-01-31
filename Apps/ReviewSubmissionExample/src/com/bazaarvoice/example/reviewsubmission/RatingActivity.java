package com.bazaarvoice.example.reviewsubmission;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bazaarvoice.OnBazaarResponse;
import com.chute.sdk.model.GCAccountMediaModel;
import com.darko.imagedownloader.ImageLoader;
import com.darko.imagedownloader.ImageLoaderListener;

/**
 * RatingActivity.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is a rating submission form. The photo is uploaded as soon as the
 * activity launches to speed up the experience of submitting a review. While
 * the user inputs information, the larger part of the request is already being
 * done.
 * 
 * Of important note here is the use of client-side validation. Basic known
 * cases are checked for before sending a request to save traffic load for both
 * the phone and the server. Any missed validation by the client is covered by
 * the server, but the more client-side validation done, the better.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class RatingActivity extends Activity implements ImageLoaderListener {

	protected static final String TAG = "Rating Activity";
	private Bitmap displayImage;
	private ImageView thumbImage;
	private RatingBar ratingBar;
	private EditText titleField;
	private EditText nicknameField;
	private EditText reviewField;
	private Button submitButton;
	private ProgressDialog progressDialog;
	private boolean photoUploaded = false;

	/**
	 * Called when the activity is first created. This Activity is launched as
	 * an Intent from MainActivity with a thumbnail image, and the path pointing
	 * to the file where the original image is stored bundled inside.
	 * 
	 * This uploads the photo on creation, out of the assumption that the user
	 * will most likely submit this review, to smooth out the process of
	 * submitting the review. The user should have to wait the smallest amount
	 * of time possible after clicking submit.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating);

		Intent myIntent = getIntent();
		thumbImage = (ImageView) findViewById(R.id.thumbImage);
		if(myIntent.hasExtra("chuteMediaModel")){
			// Chute case
			GCAccountMediaModel model = myIntent.getParcelableExtra("chuteMediaModel");
			ImageLoader loader = ImageLoader.getLoader(RatingActivity.this);
			loader.fetchBitmapAsync(model.getThumbUrl(), new ImageLoaderListener() {
				@Override
				public void onImageLoadingError() {
					// If this happens, we have a problem...
				}
				
				@Override
				public void onImageLoadingComplete(String url, Bitmap bitmap) {
					thumbImage.setImageBitmap(bitmap);
					displayImage = bitmap;
				}
			});
			loader.fetchBitmapAsync(model.getLargeUrl(), this);
			
		} else {
			// Camera case
			byte[] byteArray = myIntent.getByteArrayExtra("capturedImage");
			if (byteArray == null) {
				displayImage = null;
			} else if (displayImage == null) {
				displayImage = BitmapFactory.decodeByteArray(byteArray, 0,
						byteArray.length);
			}
			thumbImage.setImageBitmap(displayImage);
			Uri imageUri = Uri.parse(myIntent.getStringExtra("imageUri"));
			uploadPhoto(imageUri);
		}		
		
		initializeViews();
	}

	/**
	 * Calls the necessary BazaarFunctions function to upload an image to the
	 * Bazaarvoice image store.
	 * 
	 * @param imageUri
	 *            the path to the file
	 */
	private void uploadPhoto(Uri imageUri) {
		BazaarFunctions.uploadPhoto(new File(imageUri.getPath()),
				new OnImageUploadComplete() {

					@Override
					public void onFinish() {
						/*
						 * If the user has clicked "Submit" before the photo
						 * finishes uploading we must now start submitting the
						 * review.
						 */
						photoUploaded = true;
						if (progressDialog.isShowing()) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									submitReview();
								}

							});
						}
					}

				});
	}

	/**
	 * Sets up the UI elements and listeners.
	 */
	private void initializeViews() {
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
		scrollView.post(new Runnable() {

			/*
			 * These functions ensure that the main form starts scrolled to the
			 * top and, if the view is small enough to need scrolling, adds a
			 * margin at the bottom.
			 */
			
			@Override
			public void run() {
				if (scrollView.getScrollY() != 0) {
					addBottomMargin();
				}
				scrollView.fullScroll(ScrollView.FOCUS_UP);
			}

			private void addBottomMargin() {
				View bottomSpace = (View) findViewById(R.id.bottomSpace);
				ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) bottomSpace
						.getLayoutParams();
				params.height = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
								.getDisplayMetrics());
				bottomSpace.setLayoutParams(params);
			}

		});

		progressDialog = new ProgressDialog(this);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);

		titleField = (EditText) findViewById(R.id.titleField);
		nicknameField = (EditText) findViewById(R.id.nicknameField);
		reviewField = (EditText) findViewById(R.id.reviewField);
		submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitReview();
			}

		});
	}

	/**
	 * Does some client-side validation before calling the necessary
	 * BazaarFunctions function to submit a review (only previews to facilitate
	 * easier testing). When the response comes in, it launches the next
	 * activity.
	 * 
	 * If the photo has not uploaded yet, we put off submitting and show an
	 * "Uploading Photo..." dialog.
	 */
	protected void submitReview() {
		if (ratingBar.getRating() == 0) {
			Toast.makeText(getBaseContext(),
					"You must give a rating between 1 and 5.",
					Toast.LENGTH_SHORT).show();
		} else if (titleField.getText().toString().equals("")) {
			Toast.makeText(getBaseContext(), "You must enter a title.",
					Toast.LENGTH_SHORT).show();
		} else if (nicknameField.getText().toString().equals("")) {
			Toast.makeText(getBaseContext(), "You must enter a nickname.",
					Toast.LENGTH_SHORT).show();
		} else if (reviewField.getText().toString().equals("")) {
			Toast.makeText(getBaseContext(), "You must enter a review.",
					Toast.LENGTH_SHORT).show();
		} else if (photoUploaded) {
			final BazaarReview review = new BazaarReview();
			review.setTitle(titleField.getText().toString());
			review.setReviewText(reviewField.getText().toString());
			review.setNickname(nicknameField.getText().toString());
			review.setRating((int) ratingBar.getRating());

			//set to preview for easier testing, intention here is to submit
			BazaarFunctions.previewReview(MainActivity.productId, review,
					new OnBazaarResponse() {

						@Override
						public void onException(String message,
								Throwable exception) {
							Log.e(TAG,
									"Error = "
											+ message
											+ "\n"
											+ Log.getStackTraceString(exception));
						}

						@Override
						public void onResponse(JSONObject json) {
							Log.i(TAG, "Response = \n" + json);

							try {
								if (json.getBoolean("HasErrors")) {
									displayErrorMessage(json);
									progressDialog.dismiss();
								} else {
									Intent intent = new Intent(
											getBaseContext(),
											RatingPreviewActivity.class);
									ByteArrayOutputStream stream = new ByteArrayOutputStream();
									displayImage.compress(
											Bitmap.CompressFormat.PNG, 100,
											stream);
									byte[] byteArray = stream.toByteArray();
									intent.putExtra("displayImage", byteArray);
									intent.putExtra("reviewTitle",
											review.getTitle());
									intent.putExtra("reviewText",
											review.getReviewText());
									intent.putExtra("reviewNickname",
											review.getNickname());
									intent.putExtra("reviewRating",
											review.getRating());
									progressDialog.dismiss();
									startActivity(intent);
								}
							} catch (JSONException exception) {
								Log.e(TAG, Log.getStackTraceString(exception));
							}

						}

					});
			progressDialog.setMessage("Submitting Review...");
			progressDialog.show();
		} else {
			progressDialog.setMessage("Uploading Photo...");
			progressDialog.show();
		}
	}

	/**
	 * Grabs the first field error and displays it in a toast. If no form errors
	 * occurred, displays a general error.
	 * 
	 * @param json
	 *            the response to the BazaarRequest
	 */
	protected void displayErrorMessage(final JSONObject json) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject formErrors = json.getJSONObject("FormErrors");
					JSONArray errorNames = formErrors
							.getJSONArray("FieldErrorsOrder");
					JSONObject fieldErrors = formErrors
							.getJSONObject("FieldErrors");
					if (!errorNames.optString(0).equals("")) {
						String name = errorNames.getString(0);
						JSONObject error = fieldErrors.getJSONObject(name);
						String message = error.getString("Message");
						Toast.makeText(getBaseContext(), message,
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getBaseContext(),
								"An error has occurred", Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException exception) {
					Log.e(TAG, Log.getStackTraceString(exception));
				}
			}

		});
	}

	@Override
	public void onImageLoadingComplete(String url, Bitmap bitmap) {
		BazaarFunctions.uploadPhoto(bitmap,
				url,
				new OnImageUploadComplete() {

					@Override
					public void onFinish() {
						/*
						 * If the user has clicked "Submit" before the photo
						 * finishes uploading we must now start submitting the
						 * review.
						 */
						photoUploaded = true;
						if (progressDialog.isShowing()) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									submitReview();
								}

							});
						}
					}

				});
	}

	@Override
	public void onImageLoadingError() {
		Toast.makeText(getBaseContext(),
				"An error has occurred", Toast.LENGTH_LONG)
				.show();
	}

}
