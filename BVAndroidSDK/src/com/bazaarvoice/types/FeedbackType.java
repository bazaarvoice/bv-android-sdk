package com.bazaarvoice.types;

/**
 * 
 *  An enum used for defining the type of feedback to be submitted.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public enum FeedbackType {
	/**
	 * Provide feedback on helpfulness.
	 */
	HELPFULNESS("helpfulness"),
	/**
	 * Provide feedback that the subject is inappropriate.
	 */
	INAPPROPRIATE("inappropriate");
	
	private String feedbackType;
	
	FeedbackType(String feedbackType){
		this.feedbackType = feedbackType;
	}
	public String getTypeString() {
		return this.feedbackType;
	}
}
