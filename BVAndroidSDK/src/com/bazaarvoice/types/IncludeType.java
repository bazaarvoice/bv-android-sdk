package com.bazaarvoice.types;

/**
 * 
 * An enum used for defining an additional content type to be included with a request.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public enum IncludeType {
	/**
	 * Include answers
	 */
	ANSWERS("answers"),
	/**
	 * Include authors
	 */
	PROFILES("authors"),
	/**
	 * Include categories
	 */
	CATEGORIES("categories"),
	/**
	 * Include comments
	 */
	COMMENTS("comments"),
	/**
	 * Include products
	 */
	PRODUCTS("products"),
	/**
	 * Include questions
	 */
	QUESTIONS("questions"),
	/**
	 * Include reviews
	 */
	REVIEWS("reviews"),
	/**
	 * Include stories
	 */
	STORIES("stories");
	
	private String includeType;
	
	IncludeType(String includeType){
		this.includeType = includeType;
	}
	public String getTypeString() {
		return this.includeType;
	}
}
