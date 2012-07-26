package com.bazaarvoice;

/**
 * 
 * An enum for the API version to use when making a request.
 * 
 * <p>
 * Created on 7/26/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public enum ApiVersion {
	/**
	 * Use version "5.1"
	 */
	FIVE_ONE("5.1"),
	/**
	 * Use version "5.2"
	 */
	FIVE_TWO("5.2");

	private String version;

	/**
	 * Create a new ApiVersion with the given version name.
	 * 
	 * @param versionName
	 *            the version name
	 */
	ApiVersion(String versionName) {
		version = versionName;
	}

	/**
	 * Get the name used for requests.
	 * 
	 * @return the version name
	 */
	public String getVersionName() {
		return version;
	}
}
