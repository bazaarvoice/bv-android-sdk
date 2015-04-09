package com.bazaarvoice.intentexample;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.SubmissionMediaParams;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.types.Action;
import com.bazaarvoice.types.ApiVersion;
import com.bazaarvoice.types.MediaParamsContentType;
import com.bazaarvoice.types.RequestType;

/**
 * BazaarFunctions.java <br>
 * IntentExample<br>
 * 
 * <p>
 * This is a suite of functions that leverage the BazaarvoiceSDK. This class
 * consolidates the usage of these functions for easier understanding of how to
 * use the SDK.
 * 
 * 
 * 
 * <p>
 * Created on 7/19/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public class BazaarFunctions {

	public static final String API_URL = "stories.apitestcustomer.bazaarvoice.com/bvstaging";
	public static final String API_KEY = "2cpdrhohmgmwfz8vqyo48f52g";
	public static final ApiVersion API_VERSION = ApiVersion.FIVE_FOUR;
	protected static final String TAG = "BazaarFunctions";

	public static String USER_ID = "";
	public static String USER_NICKNAME = "";

	protected static String submittedPhotoUrl;
	private static boolean photoSubmitted = false;
	protected static boolean submitButtonClicked = false;

	private static OnBazaarResponse submissionListener;
	private static String storyTitle = "";
	private static String storyText = "";
	private static String caption = "";

	/**
	 * Submits the photo associated with the given file and then holds onto the
	 * photo url for later story submission.
	 * 
	 * @param file
	 *            a file holding a photo
	 */
	public static void doPhotoSubmission(File file) {
		SubmissionMediaParams submissionMediaParams = new SubmissionMediaParams(MediaParamsContentType.STORY);

		// This can be done more easily by enabling anonymous submission
		USER_ID = java.util.UUID.randomUUID().toString().substring(0, 8);
		USER_NICKNAME = USER_ID;
		submissionMediaParams.setUserId(USER_ID);

		try {
			submissionMediaParams.setPhoto(file);
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}

		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		request.postSubmission(RequestType.PHOTOS, submissionMediaParams,
				new OnBazaarResponse() {
					public void onResponse(JSONObject jsonObject) {
						JSONObject photo, sizes, normal;
						Log.i("Photo response", jsonObject.toString());
						try {
							photo = jsonObject.getJSONObject("Photo");
							sizes = photo.getJSONObject("Sizes");
							normal = sizes.getJSONObject("normal");

							submittedPhotoUrl = normal.getString("Url");
							photoSubmitted = true;
							if (submitButtonClicked) {
								doStorySubmission();
							}
						} catch (JSONException ex) {
							Log.e(TAG, ex.getMessage());
						}

					}

					public void onException(String message, Throwable exception) {
						Log.e(TAG,
								"Error = " + message + "\n"
										+ Log.getStackTraceString(exception));

						/*
						 * When using the NOTIFICATION method, we would need to
						 * add a fail safe here. If the user were to press
						 * "Submit" before the photo is uploaded and the photo
						 * fails to upload, we need to alert them.
						 * 
						 * That is very difficult to do from a separate file,
						 * though. Separating these functions from the main file
						 * is done here for clarity but shouldn't be done in
						 * your apps.
						 */
					}
				});
	}

	/**
	 * Submits the story only if the photo is finished uploading. The story is
	 * defined by calls to {@link #setStoryCaption(String)}
	 * {@link #setStoryText(String)} {@link #setStoryTitle(String)} and the
	 * request uses the listener defined by
	 * {@link #setSubmissionResponse(OnBazaarResponse)} to handle the response.
	 * 
	 * <p>
	 * These all need to be called first to be applied.
	 */
	public static void doStorySubmission() {
		if (photoSubmitted == false) {
			submitButtonClicked = true;
			return;
		}

		// Do not let it try to use a null listener
		if (submissionListener == null) {
			return;
		}

		SubmissionParams submissionParams = new SubmissionParams();
		submissionParams.setUserId(USER_ID);
		submissionParams.setUserNickname(USER_NICKNAME);

		submissionParams.setTitle(storyTitle);
		submissionParams.setStoryText(storyText);
		submissionParams.addPhotoCaption(caption);

		/*
		 * For story submission, you must include either a ProductId or a
		 * CategoryId
		 */
		submissionParams.setCategoryId("Yellow");

		submissionParams.addPhotoUrl(submittedPhotoUrl);
		submissionParams.setAction(Action.SUBMIT);
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		request.postSubmission(RequestType.STORIES, submissionParams,
				submissionListener);
	}

	/**
	 * Submits the story for preview only. This is used to check for form errors
	 * before actually submitting with {@link #doStorySubmission()}.
	 * 
	 * @param text
	 *            the StoryText
	 * @param title
	 *            the StoryTitle
	 * @param listener
	 *            the callback function to handle the response
	 */
	public static void doStoryPreview(String text, String title,
			OnBazaarResponse listener) {
		SubmissionParams submissionParams = new SubmissionParams();

		submissionParams.setUserId(USER_ID);
		submissionParams.setUserNickname(USER_NICKNAME);

		submissionParams.setTitle(title);
		submissionParams.setStoryText(text);
		submissionParams.setCategoryId("Yellow");
		submissionParams.setAction(Action.PREVIEW);

		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		request.postSubmission(RequestType.STORIES, submissionParams, listener);
	}

	/**
	 * Set the title of the story to be submitted by
	 * {@link #doStorySubmission()}.
	 * 
	 * @param title
	 *            the story title
	 */
	public static void setStoryTitle(String title) {
		storyTitle = title;
	}

	/**
	 * Set the text of the story to be submitted by {@link #doStorySubmission()}
	 * .
	 * 
	 * @param text
	 *            the story text
	 */
	public static void setStoryText(String text) {
		storyText = text;
	}

	/**
	 * Set the caption for the photo. Captions are tied to stories and not the
	 * actual photos, so it will be submitted with the story via
	 * {@link #doStorySubmission()}.
	 * 
	 * @param cap
	 *            the photo caption
	 */
	public static void setStoryCaption(String cap) {
		caption = cap;
	}

	/**
	 * Set the callback function for the response of the story submitted by
	 * {@link #doStorySubmission()}.
	 * 
	 * @param submissionResponseHandler
	 *            the callback function for handling the response
	 */
	public static void setSubmissionResponse(
			OnBazaarResponse submissionResponseHandler) {
		submissionListener = submissionResponseHandler;
	}

}
