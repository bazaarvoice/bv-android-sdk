package com.bazaarvoice.types;

/**
 * 
 * An enum used for defining the content type for which feedback is submitted.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public enum FeedbackContentType {
	/**
	 * Leave feedback on an answer.
	 */
	ANSWER("answer"),
	/**
	 * Leave feedback on a question.
	 */
	QUESTION("question"),
	/**
	 * Leave feedback on a review.
	 */
	REVIEW("review"),
	/**
	 * Leave feedback on a review comment.
	 */
	REVIEW_COMMENT("review_comment"),
	/**
	 * Leave feedback on a story.
	 */
	STORY("story"),
	/**
	 * Leave feedback on a story comment.
	 */
	STORY_COMMENT("story_comment");
	
	private String feedbackContentType;
	
	FeedbackContentType(String feedbackContentType){
		this.feedbackContentType = feedbackContentType;
	}
	public String getTypeString() {
		return this.feedbackContentType;
	}
}
