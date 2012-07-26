package com.bazaarvoice;

/**
 * 
 * An enum for all the different types of requests we can make.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
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
	 * Request for feedback.
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
