package com.bazaarvoice;

/**
 * 
 * An enum for all available equality types in BazaarParams requests.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */

public enum Equality {
	/**
	 * Matches only results less than the value.
	 */
	LESS_THAN("lt"),

	/**
	 * Matches only results less than or equal to the value.
	 */
	LESS_THAN_EQ("lte"),

	/**
	 * Matches only results equal to the value.
	 */
	EQUAL("eq"),

	/**
	 * Matches only results greater than the value.
	 */
	GREATER_THAN("gt"),

	/**
	 * Matches only results greater than or equal to the value.
	 */
	GREATER_THAN_EQ("gte");

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
