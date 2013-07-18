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
 * An enum used for defining the content type for which feedback is submitted.
 * 
 */
public enum FeedbackContentType {
	/**
	 * Leave feedback on an answer.
	 */
	ANSWER("answer"),
	/**
	 * Leave feedback on a question.
	 */
	QUESTION("question"),
	/**
	 * Leave feedback on a review.
	 */
	REVIEW("review"),
	/**
	 * Leave feedback on a review comment.
	 */
	REVIEW_COMMENT("review_comment"),
	/**
	 * Leave feedback on a story.
	 */
	STORY("story"),
	/**
	 * Leave feedback on a story comment.
	 */
	STORY_COMMENT("story_comment");
	
	private String feedbackContentType;
	
	FeedbackContentType(String feedbackContentType){
		this.feedbackContentType = feedbackContentType;
	}
	public String getTypeString() {
		return this.feedbackContentType;
	}
}
