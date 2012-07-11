package com.bazaarvoice;

/**
 * Equality.java <br>
 * Bazaarvoice Android SDK<br>
 * 
 * An enum for all available equality types in BazaarParams requests.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */

public enum Equality {
	LESS_THAN("lt"), LESS_THAN_EQ("lte"), EQUAL("eq"), GREATER_THAN("gt"), GREATER_THAN_EQ(
			"gte");

	private String equalityStr;

	Equality(String text) {
		equalityStr = text;
	}

	/**
	 * Get the equality string associated with this instance.
	 * 
	 * @return the equality string
	 */
	public String getEquality() {
		return equalityStr;
	}

	/**
	 * Gets the Equality value from the given equality string.
	 * 
	 * @param text
	 *            the equality string
	 * @return an Equality value
	 */
	public static Equality fromString(String text) {
		if (text != null) {
			for (Equality b : Equality.values()) {
				if (text.equalsIgnoreCase(b.equalityStr)) {
					return b;
				}
			}
		}
		return null;
	}
}
