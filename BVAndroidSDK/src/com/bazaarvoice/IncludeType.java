package com.bazaarvoice;

/**
 * 
 * An enum for all the different types of content you can include in a request.
 * 
 * <p>
 * Created on 7/26/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public enum IncludeType {
	/**
	 * Include product reviews
	 */
	REVIEWS("reviews"),
	/**
	 * Include questions
	 */
	QUESTIONS("questions"),
	/**
	 * Include question answers
	 */
	ANSWERS("answers"),
	/**
	 * Include stories
	 */
	STORIES("stories"),
	/**
	 * Include comments
	 */
	COMMENTS("comments"),
	/**
	 * Include products
	 */
	PRODUCTS("products"),
	/**
	 * Include categories
	 */
	CATEGORIES("categories"),
	/**
	 * Include authors
	 */
	AUTHORS("authors");
	
	private String displayName;

	/**
	 * Create a new IncludeType with the given display name.
	 * 
	 * @param displayName
	 *            the display name
	 */
	IncludeType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Get the name used for display requests.
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

}
