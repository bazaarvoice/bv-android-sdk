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

/**
 * 
 * An exception type used for problems that arise in the use of the
 * Bazaarvoice SDK.
 */
public class BazaarException extends Exception {

	private static final long serialVersionUID = -1458240370794878582L;

	/**
	 * Constructs a new BazaarException with the current stack trace and the
	 * specified detail message.
	 * 
	 * @param detailMessage the message for the exception
	 */
	public BazaarException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * Constructs a new BazaarException with the current stack trace, the
	 * specified detail message and the specified cause.
	 * 
	 * @param detailMessage the message for the exception
	 * @param throwable the cause of the exception
	 */
	public BazaarException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
