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
 * An enum for all the different types of requests we can make via the BazaarVoice SDK.
 */
public enum RequestType {
	/**
	 * Request product reviews.
	 */
	REVIEWS("reviews", "submitreview"),
	/**
	 * Request questions.
	 */
	QUESTIONS("questions", "submitquestion"),
	/**
	 * Request question answers.
	 */
	ANSWERS("answers", "submitanswer"),
	/**
	 * Request stories.
	 */
	STORIES("stories", "submitstory"),
	/**
	 * Request review comments.
	 */
	REVIEW_COMMENTS("reviewcomments", "submitreviewcomment"),
	/**
	 * Request story comments.
	 */
	STORY_COMMENTS("storycomments", "submitstorycomment"),
	/**
	 * Request profiles.
	 */
	PROFILES("authors", "submitauthor"),
	/**
	 * Request for photos.
	 */
	PHOTOS(null, "uploadphoto"),
	/**
	 * Request for videos.
	 */
	VIDEOS(null, "uploadvideo"),
	/**
	 * Request for products
	 */
	PRODUCTS("products", null),
	/**
	 * Request categories.
	 */
	CATEGORIES("categories", null),
	/**
	 * Request statistics.
	 */
	STATISTICS("statistics", null),
	/**
	 * Request feedback.
	 */
	FEEDBACK(null, "submitfeedback");

	private String displayName;
	private String submissionName;

	/**
	 * Create a new RequestType with the given display and submission names.
	 * 
	 * @param displayName
	 *            the display name
	 * @param submissionName
	 *            the submission name
	 */
	RequestType(String displayName, String submissionName) {
		this.displayName = displayName;
		this.submissionName = submissionName;
	}

	/**
	 * Get the name used for display requests.
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Get the name used for submission requests.
	 * 
	 * @return the submission name
	 */
	public String getSubmissionName() {
		return submissionName;
	}

}
