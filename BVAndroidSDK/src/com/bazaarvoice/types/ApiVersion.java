package com.bazaarvoice.types;

/**
 * 
 * An enum used for defining for the API version to use when making a request.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
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
	FIVE_THREE("5.3");
	
	private String apiString;
	
	ApiVersion(String apiString){
		this.apiString = apiString;
	}
	public String getVersionName() {
		return this.apiString;
	}
}
