package com.bazaarvoice.types;

/**
 * 
 * An enum used for defining the vote type for feedback to be submitted.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public enum FeedbackVoteType {
	/**
	 * Leave positive feedback.
	 */
	POSITIVE("positive"),
	/**
	 * Leave negative feedback.
	 */
	NEGATIVE("negative");
	
	private String feedbackVoteType;
	
	FeedbackVoteType(String feedbackType){
		this.feedbackVoteType = feedbackType;
	}
	public String getTypeString() {
		return this.feedbackVoteType;
	}
}
