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
 * An enum used for defining an additional content type to be included with a request.
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
