package com.bazaarvoice.types;

/**
 * 
 * An enum used for defining the type of statistics to include with a request.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public enum IncludeStatsType {
	/**
	 * Include stats on answers
	 */
	ANSWERS("answers"),
	/**
	 * Include stats on questions
	 */
	QUESTIONS("questions"),
	/**
	 * Include stats on reviews
	 */
	REVIEWS("reviews"),
	/**
	 * Include stats on nativereviews
	 */
	NATIVE_REVIEWS("nativereviews"),
	/**
	 * Include stats on stories
	 */
	STORIES("stories");
	
	private String includeStatsType;
	
	IncludeStatsType(String includeStatsType){
		this.includeStatsType = includeStatsType;
	}
	public String getTypeString() {
		return this.includeStatsType;
	}
}
