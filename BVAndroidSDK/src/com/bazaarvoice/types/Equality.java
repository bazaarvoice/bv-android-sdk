/*******************************************************************************
 * Copyright 2013 Bazaarvoice
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bazaarvoice.types;

/**
 * 
 * An enum for all available equality types in BazaarParams requests.
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
	 * Matches only results not equal to the value.
	 */
	NOT_EQUAL("neq"),

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

	public String getEquality() {
		return equalityStr;
	}

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
