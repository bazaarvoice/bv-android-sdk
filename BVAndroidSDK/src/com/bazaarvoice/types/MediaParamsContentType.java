package com.bazaarvoice.types;

/**
 * 
 * An enum for the content type with which to associate a media submission.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public enum MediaParamsContentType {
	/**
	 * Submit media associated with an answer.
	 */
	ANSWER("answer"),
	/**
	 * Submit media associated with a question.
	 */
	QUESTION("question"),
	/**
	 * Submit media associated with a review.
	 */
	REVIEW("review"),
	/**
	 * Submit media associated with a review comment.
	 */
	REVIEW_COMMENT("review_comment "),
	/**
	 * Submit media associated with a story comment.
	 */
	STORY_COMMENT("story_comment "),
	/**
	 * Submit media associated with a revstoryiew.
	 */
	STORY("story");
	
	private String mediaContentType;
	
	MediaParamsContentType(String mediaContentType){
		this.mediaContentType = mediaContentType;
	}
	public String getTypeString() {
		return this.mediaContentType;
	}
}
