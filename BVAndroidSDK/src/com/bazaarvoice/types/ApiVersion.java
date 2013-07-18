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
 * An enum used for defining for the API version to use when making a request.
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
