package com.bazaarvoice.example.reviewsubmission;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.bazaarvoice.types.*;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.SubmissionMediaParams;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.types.ApiVersion;

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
	private static final ApiVersion API_VERSION = ApiVersion.FIVE_FOUR;
	private static final int MIN_IMAGE_DIMENSIONS = 600;


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
	 * Actually sends the photo upload request -- this is a helper function for uploadPhoto.
	 * 
	 * @param params
	 *            the params of the request
	 * @param listener
	 *            an <code>OnImageUploadComplete</code> object
	 */
	private static void sendPhotoRequest(SubmissionMediaParams params, final OnImageUploadComplete listener){
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
		submitMedia.postSubmission(RequestType.PHOTOS, params, response);
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
			SubmissionMediaParams params = new SubmissionMediaParams(MediaParamsContentType.REVIEW);
			params.setPhoto(file);
			params.setUserId("test1");

			sendPhotoRequest(params, listener);

		} catch (Exception exception) {
			Log.e(TAG, Log.getStackTraceString(exception));
		}
	}

	/**
	 * Creates a request to upload a photo to the Bazaarvoice image store for
	 * use with a review. This function also allows you to pass a callback
	 * function to be called when the upload completes.
	 * 
	 * @param bitmap
	 *            the bitmap representation of the image to upload
	 * @param filename
	 *            the filename of the photo to upload -- this is necessary to determine mime type
	 */
	public static void uploadPhoto(Bitmap bitmap, String filenname, final OnImageUploadComplete listener) {
		try {
			SubmissionMediaParams params = new SubmissionMediaParams(MediaParamsContentType.REVIEW);
			if(bitmap.getHeight() < MIN_IMAGE_DIMENSIONS || bitmap.getWidth() < MIN_IMAGE_DIMENSIONS){
				float scale = Math.max(MIN_IMAGE_DIMENSIONS / (float)  bitmap.getHeight(), MIN_IMAGE_DIMENSIONS / (float) bitmap.getWidth());
				bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * scale), (int)(bitmap.getHeight() * scale), true);
			}
			
			params.setPhoto(bitmap, filenname);
			params.setUserId("test1");

			sendPhotoRequest(params, listener);
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
			params.setAction(Action.SUBMIT);
		else
			params.setAction(Action.PREVIEW);

		params.setProductId(prodId);
		params.setRating(review.getRating());
		params.setReviewText(review.getReviewText());
		params.setTitle(review.getTitle());
		params.setUserNickname(review.getNickname());
		params.setUserEmail("bv@bv.com");

		if (!photoUrl.equals(""))
			params.addPhotoUrl(photoUrl);

		if (!review.getAuthorId().equals("null"))
			params.setUserId(review.getAuthorId());
		else if ( !(review.getNickname().equals("null") || "".equals(review.getNickname().trim())) )
			params.setUserId(review.getNickname());
		else
			params.setUserId("Anonymous");

		BazaarRequest submission = new BazaarRequest(API_URL, API_KEY,
				API_VERSION);
		submission.postSubmission(RequestType.REVIEWS, params, listener);
	}

}
