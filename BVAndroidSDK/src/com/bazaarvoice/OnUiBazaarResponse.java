package com.bazaarvoice;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * 
 * This is to be used in the place of OnBazaarResponse when you want to alter UI elements to reflect the results. The only required method, onUiResponse(JSONObject), will be run on the UI thread. You may choose to override onException(String, Throwable) as well to take any action when there is an error with the request.
 * 
 * <p>
 * Created on 8/8/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public abstract class OnUiBazaarResponse implements OnBazaarResponse {
	
	
	/**
	 * Prints the error and stack trace to Logcat.
	 * 
	 * @param message
	 *            the error message
	 * @param exception
	 *            the exception that caused the problem
	 */
	public void onException(String message, Throwable exception) {
		Log.e("ERROR", message + "\n" + exception.getMessage());
	}
	
	/**
	 * Calls onUiResponse(JSONObject) on the UI thread.
	 * 
	 * @param response
	 *            the JSONObject containing the response
	 */
	public void onResponse(JSONObject response) {
		final JSONObject myResponse = response;
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				onUiResponse(myResponse);
			}
		});
	}

	/**
	 * Called on the UI thread when the response is received.
	 * 
	 * @param response
	 *            the JSONObject containing the response
	 */
	public abstract  void onUiResponse(JSONObject response);
}
