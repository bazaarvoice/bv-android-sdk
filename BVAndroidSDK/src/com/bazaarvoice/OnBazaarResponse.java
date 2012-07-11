package com.bazaarvoice;

import org.json.JSONObject;

/**
 * OnBazaarResponse.java <br>
 * Bazaarvoice Android SDK<br>
 * 
 * This interface is used to define callbacks for when a response to a request
 * is received.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public interface OnBazaarResponse {
	/**
	 * Handle a valid json response.
	 * 
	 * @param response
	 *            the JSONObject containing the response
	 */
	public void onResponse(final JSONObject response);

	/**
	 * Handle any exceptions that occurred as a result of the request.
	 * 
	 * @param message
	 *            the error message
	 * @param exception
	 *            the exception that caused the problem
	 */
	public void onException(final String message, final Throwable exception);
}
