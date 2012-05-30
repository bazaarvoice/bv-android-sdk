package com.bazaarvoice;

import org.json.JSONObject;

/**
 * User: gary
 * Date: 4/17/12
 * Time: 10:22 PM
 */
public interface OnBazaarResponse {
    /**
     * Handle a valid json response
     * @param response the JSONObject containing the response
     */
    public void onResponse(final JSONObject response);

    /**
     * Handle any exceptions that occurred as a result of the request
     * @param message the error message
     * @param exception the exception that caused the problem
     */
    public void onException(final String message, final Throwable exception);
}
