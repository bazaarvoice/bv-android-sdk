/*******************************************************************************
 * Copyright 2013 Bazaarvoice
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bazaarvoice;

import org.json.JSONObject;

/**
 * 
 * An interface used to define callbacks for when a response to a request
 * is received.
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
