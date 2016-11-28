/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk.types;

/**
 * An enum used for defining an additional content type to be included with a request.
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
