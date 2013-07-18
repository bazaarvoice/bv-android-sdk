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
 * An enum for the content type with which to associate a media submission.
 * 
 */
public enum MediaParamsContentType {
	/**
	 * Submit media associated with an answer.
	 */
	ANSWER("answer"),
	/**
	 * Submit media associated with a question.
	 */
	QUESTION("question"),
	/**
	 * Submit media associated with a review.
	 */
	REVIEW("review"),
	/**
	 * Submit media associated with a review comment.
	 */
	REVIEW_COMMENT("review_comment "),
	/**
	 * Submit media associated with a story comment.
	 */
	STORY_COMMENT("story_comment "),
	/**
	 * Submit media associated with a revstoryiew.
	 */
	STORY("story");
	
	private String mediaContentType;
	
	MediaParamsContentType(String mediaContentType){
		this.mediaContentType = mediaContentType;
	}
	public String getTypeString() {
		return this.mediaContentType;
	}
}
