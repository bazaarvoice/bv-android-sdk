package com.bazaarvoice.intentexample;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.bazaarvoice.OnBazaarResponse;

public abstract class BazaarUIThreadResponse implements OnBazaarResponse {

	private Activity context;
	
	public BazaarUIThreadResponse(Activity context){
		this.context = context;
	}
	
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
		this.context.runOnUiThread(new Runnable() {
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
