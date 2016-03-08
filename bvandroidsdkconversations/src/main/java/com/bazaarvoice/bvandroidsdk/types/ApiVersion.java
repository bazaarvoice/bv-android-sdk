/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk.types;

/**
 * Bazaarvoice Conversations enum used for defining for the API version to use when making a request.
 */
public enum ApiVersion {
	/**
	 * Use version 5.1.
	 */
	FIVE_ONE("5.1"),
	/**
	 * Use version 5.2.
	 */
	FIVE_TWO("5.2"),
	/**
	 * Use version 5.3.
	 */
	FIVE_THREE("5.3"),
	
	FIVE_FOUR("5.4");
	
	private String apiString;
	
	ApiVersion(String apiString){
		this.apiString = apiString;
	}
	public String getVersionName() {
		return this.apiString;
	}
}
