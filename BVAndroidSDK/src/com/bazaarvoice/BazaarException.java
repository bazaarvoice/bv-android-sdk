package com.bazaarvoice;

/**
 * 
 * An exception type used for problems that arise in the use of the
 * Bazaarvoice SDK.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public class BazaarException extends Exception {

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
