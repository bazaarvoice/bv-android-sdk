package com.bazaarvoice;

/**
 * 
 * Handles the parameters for feedback submission. Use of this class may be
 * eased with knowledge of the <a
 * href="http://developer.bazaarvoice.com/">Bazaarvoice API</a>. You might want
 * to use this site as a reference for which parameters to pass using this
 * class. There are a few parameters that are required as described on the API
 * site. Please make sure you set all of them.
 * 
 * <p>
 * Created on 7/26/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class SubmissionFeedbackParams extends BazaarParams {
	private String contentId;
	private String contentType;
	private String feedbackType;
	private String vote;
	private String userId;
	private String reasonText;

	/**
	 * 
	 * An enum for all different types of feedback that can be submitted.
	 * 
	 * <p>
	 * Created on 7/26/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
	 * 
	 * @author Bazaarvoice Engineering
	 */
	public enum FeedbackType {
		/**
		 * Flag content as inappropriate
		 */
		INAPPROPRIATE("inappropriate"),
		/**
		 * Give helpfulness vote
		 */
		HELPFULNESS("helpfulness");

		public String type;

		FeedbackType(String type) {
			this.type = type;
		}
	}

	/**
	 * 
	 * An enum for the different types votes of Helpfulness.
	 * 
	 * <p>
	 * Created on 7/26/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
	 * 
	 * @author Bazaarvoice Engineering
	 */
	public enum Vote {
		/**
		 * Give a positive helpfulness vote
		 */
		POSITIVE("Positive"),
		/**
		 * Give a negative helpfulness vote
		 */
		NEGATIVE("Negative");

		public String value;

		Vote(String value) {
			this.value = value;
		}
	}

	/**
	 * Create a new instance of SubmissionFeedbackParams with a given
	 * "Content Type" parameter.
	 * 
	 * @param contentType
	 *            the content we are giving feedback for
	 */
	public SubmissionFeedbackParams(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Add the parameters set in this instance to the given url string.
	 * 
	 * @param url
	 *            the base url to append to
	 * @return the url with the parameter list on it
	 */
	public String toURL(String url) {
		url = addURLParameter(url, "contentType", contentType);
		url = addURLParameter(url, "contentId", getContentId());
		url = addURLParameter(url, "feedbackType", getFeedbackType());
		url = addURLParameter(url, "vote", getVote());
		url = addURLParameter(url, "userId", getUserId());
		url = addURLParameter(url, "reasonText", getReasonText());
		return url;
	}

	/**
	 * Get the "ContentId" set for this feedback submission.
	 * 
	 * @return the contentId
	 */
	public String getContentId() {
		return contentId;
	}

	/**
	 * Set the "ContentId" parameter fot this feedback submission.
	 * 
	 * @param contentId
	 *            the contentId to set
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	/**
	 * Get the "FeedbackType" set for this feedback submission.
	 * 
	 * @return the feedback type
	 */
	public String getFeedbackType() {
		return feedbackType;
	}

	/**
	 * Set the "FeedbackType" parameter for this feedback submission.
	 * 
	 * @param feedbackType
	 *            the feedbackType to set
	 */
	public void setFeedbackType(FeedbackType feedbackType) {
		this.feedbackType = feedbackType.type;
	}

	/**
	 * Get the "Vote" parameter value set for this feedback submission.
	 * 
	 * @return the vote value
	 */
	public String getVote() {
		return vote;
	}

	/**
	 * Set the "Vote" parameter value for this feedback submission.
	 * 
	 * @param vote
	 *            the vote to set
	 */
	public void setVote(Vote vote) {
		this.vote = vote.value;
	}

	/**
	 * Get the "UserId" set for this feedback submission.
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the "UserId" for this feedback submission.
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Get the "ReasonText" set for this feedback submission.
	 * 
	 * @return the reasonText
	 */
	public String getReasonText() {
		return reasonText;
	}

	/**
	 * Set the "ReasonText" for this feedback submission.
	 * 
	 * @param reasonText
	 *            the reasonText to set
	 */
	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}
}
