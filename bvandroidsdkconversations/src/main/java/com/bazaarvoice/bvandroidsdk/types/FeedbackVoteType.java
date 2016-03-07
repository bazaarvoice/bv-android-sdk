/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk.types;

/**
 * An enum used for defining the vote type for feedback to be submitted.
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
