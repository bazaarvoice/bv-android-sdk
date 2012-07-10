package com.bazaarvoice.example.reviewsubmission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bazaarvoice.Action;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.Equality;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.RequestType;
import com.bazaarvoice.SubmissionMediaParams;
import com.bazaarvoice.SubmissionParams;

/**
 * BazaarFunctions.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is a suite of functions that leverage the BazaarvoiceSDK. This class
 * consolidates the usage of these functions for easier understanding of how to
 * use the SDK.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public class BazaarFunctions {

	private static final String TAG = "BazaarFunctions";
	private static final String API_URL = "reviews.apitestcustomer.bazaarvoice.com/bvstaging";
	private static final String API_KEY = "2cpdrhohmgmwfz8vqyo48f52g";
	private static final String API_VERSION = "5.1";

	public static String photoUrl = "";

	/**
	 * A wrapper for uploadPhoto() that doesn't pass a callback function for
	 * upload completion.
	 * 
	 * @param file
	 *            the file location of the photo
	 */
	public static void uploadPhoto(File file) {
		uploadPhoto(file, null);
	}

	/**
	 * Creates a request to upload a photo to the Bazaarvoice image store for
	 * use with a review. This function also allows you to pass a callback
	 * function to be called when the upload completes.
	 * 
	 * @param file
	 *            the file location of the photo
	 * @param listener
	 *            an <code>OnImageUploadComplete</code> object
	 */
	public static void uploadPhoto(File file,
			final OnImageUploadComplete listener) {
		try {
			SubmissionMediaParams params = new SubmissionMediaParams("review");
			params.setPhoto(file);
			params.setUserId("test1");

			OnBazaarResponse response = new OnBazaarResponse() {

				@Override
				public void onException(String message, Throwable exception) {
					Log.e(TAG,
							"Error = " + message + "\n"
									+ Log.getStackTraceString(exception));
				}

				@Override
				public void onResponse(JSONObject json) {
					Log.i(TAG, "Response = \n" + json);
					if (listener != null) {
						listener.onFinish();
					}
					try {
						photoUrl = json.getJSONObject("Photo")
								.getJSONObject("Sizes").getJSONObject("normal")
								.getString("Url");
					} catch (JSONException exception) {
						Log.e(TAG, Log.getStackTraceString(exception));
					}
				}

			};

			BazaarRequest submitMedia = new BazaarRequest(API_URL, API_KEY,
					API_VERSION);
			submitMedia.queueSubmission(RequestType.PHOTOS, params, response);

		} catch (Exception exception) {
			Log.e(TAG, Log.getStackTraceString(exception));
		}
	}

	/**
	 * Submits the given review for the given product as a preview. This means
	 * that it will not actually be submitted but will be tested against the API
	 * and any errors will be reported.
	 * 
	 * @param prodId
	 *            the product ID
	 * @param review
	 *            the full review
	 * @param listener
	 *            the callback function for handling the response
	 */
	public static void previewReview(String prodId, BazaarReview review,
			OnBazaarResponse listener) {
		reviewAction(prodId, review, listener, false);
	}

	/**
	 * Submits the given review for the given product as a submission. This
	 * means that it will be entered into the system and be ready for display
	 * soon.
	 * 
	 * @param prodId
	 *            the product ID
	 * @param review
	 *            the full review
	 * @param listener
	 *            the callback function for handling the response
	 */
	public static void submitReview(String prodId, BazaarReview review,
			OnBazaarResponse listener) {
		reviewAction(prodId, review, listener, true);
	}

	/**
	 * Builds a review request and sends it off as either a preview or a
	 * submission.
	 * 
	 * @param prodId
	 *            the product ID
	 * @param review
	 *            the full review
	 * @param listener
	 *            the callback function for handling the response
	 * @param submit
	 *            true to submit, false to preview
	 */
	private static void reviewAction(String prodId, BazaarReview review,
			OnBazaarResponse listener, boolean submit) {
		SubmissionParams params = new SubmissionParams();
		if (submit)
			params.setAction(Action.submit);
		else
			params.setAction(Action.preview);

		params.setProductId(prodId);
		params.setRating(review.getRating());
		params.setReviewText(review.getReviewText());
		params.setTitle(review.getTitle());
		params.setUserNickname(review.getNickname());

		if (!photoUrl.equals(""))
			params.addPhotoUrl(photoUrl);

		if (!review.getAuthorId().equals("null"))
			params.setUserId(review.getAuthorId());
		else if (!review.getNickname().equals("null"))
			params.setUserId(review.getNickname());
		else
			params.setUserId("Anonymous");

		BazaarRequest submission = new BazaarRequest(API_URL, API_KEY,
				API_VERSION);
		submission.postSubmission(RequestType.REVIEWS, params, listener);
	}

}
