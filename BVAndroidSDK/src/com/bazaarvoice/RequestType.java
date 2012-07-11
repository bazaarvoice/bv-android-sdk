package com.bazaarvoice;

/**
 * RequestType.java <br>
 * Bazaarvoice Android SDK<br>
 * 
 * An enum for all the different types of requests we can make to BazaarVoice.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public enum RequestType {
	REVIEWS("reviews", "submitreview"), QUESTIONS("questions", "submitquestion"), ANSWERS(
			"answers", "submitanswer"), STORIES("stories", "submitstory"), REVIEW_COMMENTS(
			"reviewcomments", "submitreviewcomment"), STORY_COMMENTS(
			"storycomments", "submitstorycomment"), PROFILES("authors",
			"submitauthor"), PHOTOS(null, "uploadphoto"), VIDEOS(null,
			"uploadvideo"), PRODUCTS("products", null), CATEGORIES(
			"categories", null), STATISTICS("statistics", null);

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
