/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import org.json.JSONObject;

/**
 * An interface used to define callbacks for when a response to a request
 * is received.
 */
public interface OnBazaarResponse {
	/**
	 * Handle a valid json response.
	 *  @param url
     * @param response
     */
	public void onResponse(String url, final JSONObject response);

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
