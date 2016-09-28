/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk.types;

/**
 * Bazaarvoice Conversations enum used for defining for the API version to use when making a request.
 */
@Deprecated
public enum ApiVersion {

	FIVE_FOUR("5.4");
	
	private String apiString;
	
	ApiVersion(String apiString){
		this.apiString = apiString;
	}
	public String getVersionName() {
		return this.apiString;
	}
}
