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

public class BazaarFunctions {
	
	private static final String TAG = "BazaarFunctions";
	private static final String API_URL = "reviews.apitestcustomer.bazaarvoice.com/bvstaging";
	private static final String API_KEY = "2cpdrhohmgmwfz8vqyo48f52g";
	private static final String API_VERSION = "5.1";
	
	public static String photoUrl = "";
	
	public static void uploadPhoto(File file){
		uploadPhoto(file, null);
	}
	
	public static void uploadPhoto(File file, final OnImageUploadComplete listener){
		try {
			SubmissionMediaParams params = new SubmissionMediaParams("review");
			params.setPhoto(file);
			params.setUserId("test1");
			
			OnBazaarResponse response = new OnBazaarResponse(){

				@Override
				public void onException(String message, Throwable exception) {
					Log.e(TAG, "Error = " + message + "\n" + Log.getStackTraceString(exception));
				}

				@Override
				public void onResponse(JSONObject json) {
					Log.i(TAG, "Response = \n" + json);
					if (listener != null){
						listener.onFinish();
					}
					try {
						photoUrl = json.getJSONObject("Photo").getJSONObject("Sizes").getJSONObject("normal").getString("Url");
					} catch (JSONException exception) {
						Log.e(TAG, Log.getStackTraceString(exception));
					}
				}
				
			};
			
			BazaarRequest submitMedia = new BazaarRequest(API_URL, API_KEY, API_VERSION);
			submitMedia.queueSubmission(RequestType.PHOTOS, params, response);
			
		} catch (Exception exception) {
			Log.e(TAG, Log.getStackTraceString(exception));
		}
	}
	
	public static boolean isPhotoUploaded(){
		return !photoUrl.equals("");
	}
	
	public static void downloadReviews(String prodId, OnBazaarResponse listener){
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		DisplayParams params = new DisplayParams();
		
		params.addFilter("ProductId", Equality.EQUAL, prodId);
		params.setLimit(15);
		
		//false => descending order
		params.addSort("SubmissionTime", false);
		
		request.sendDisplayRequest(RequestType.REVIEWS, params, listener);
	}
	
	public static void previewReview(String prodId, BazaarReview review, OnBazaarResponse listener){
		reviewAction(prodId, review, listener, false);
	}
	
	public static void submitReview(String prodId, BazaarReview review, OnBazaarResponse listener){
		reviewAction(prodId, review, listener, true);
	}
	
	private static void reviewAction(String prodId, BazaarReview review, OnBazaarResponse listener, boolean submit){
		SubmissionParams params = new SubmissionParams();
		if(submit)
			params.setAction(Action.submit);
		else
			params.setAction(Action.preview);
		
		params.setProductId(prodId);
		params.setRating(review.getRating());
		params.setReviewText(review.getReviewText());
		params.setTitle(review.getTitle());
		params.setUserNickname(review.getNickname());
		
		if(!photoUrl.equals(""))
			params.setPhotoUrl(photoUrl);
		/*
		if(!review.getAuthorId().equals("null"))
			params.setUserId(review.getAuthorId());
		else if(!review.getNickname().equals("null"))
			params.setUserId(review.getNickname());
		else
			params.setUserId("Anonymous");
			*/
		params.setUserId("testid1234523");
		
		BazaarRequest submission = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		submission.postSubmission(RequestType.REVIEWS, params, listener);
	}

}
