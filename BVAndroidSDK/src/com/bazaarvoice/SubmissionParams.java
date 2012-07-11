package com.bazaarvoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SubmissionParams.java <br>
 * Bazaarvoice Android SDK<br>
 * 
 * This class handles the parameters for content submission. <br>
 * Use of this class will rely on knowledge of the <a
 * href="http://developer.bazaarvoice.com/">Bazaarvoice API</a>. You should use
 * this site as a reference for which parameters to pass using this class.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class SubmissionParams extends BazaarParams {

	// used by all (except photo, video - handled in SubmissionMediaParams
	private Action action;
	private Boolean agreedToTermsAndConditions;
	private String answerText;
	private String campaignId;
	private Map<String, String> contextDataValue;
	private String locale;
	private List<String> photoCaptions;
	private List<String> photoUrls;
	private List<String> productRecommendationIds;
	private Boolean sendEmailAlertWhenPublished;
	private String userEmail;
	private String userId;
	private String userLocation;
	private String userNickname;
	private List<String> videoCaptions;
	private List<String> videoUrls;

	// used by review, question, answer, story
	private Map<String, String> additionalField;

	// used by review, question, story
	private String productId;

	private Map<String, String> tagDim;

	// used by review, story, comment
	private String title;

	// used by question, story
	private String categoryId;

	// used by review
	private Boolean isRecommended;
	private String netPromoterComment;
	private Integer netPromoterScore;
	private Integer rating;
	private Map<String, String> ratingDim;
	private String reviewText;

	// used by question
	private Boolean isUserAnonymous;
	private String questionSummary;
	private String questionDetails;

	// used by answer
	private String questionId;

	// used by story
	private Boolean sendEmailAlertWhenCommented;
	private String storyText;

	// used by comment
	private String reviewId;
	private String storyId;
	private String commentText;

	/**
	 * Get the Action type for the "Action" parameter for this submission if it
	 * has been set.
	 * 
	 * @return the action type
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Set the Action type for the "Action" parameter for this submission.
	 * 
	 * @param action
	 *            an action type
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Get the value of the "AgreedToTermsAndConditions" parameter for this
	 * submission if it has been set.
	 * 
	 * @return the value
	 */
	public Boolean isAgreedToTermsAndConditions() {
		return agreedToTermsAndConditions;
	}

	/**
	 * Set the value of the "AgreedToTermsAndConditions" parameter for this
	 * submission.
	 * 
	 * @param agreedToTermsAndConditions
	 *            true for yes, false for no
	 */
	public void setAgreedToTermsAndConditions(boolean agreedToTermsAndConditions) {
		this.agreedToTermsAndConditions = agreedToTermsAndConditions;
	}

	/**
	 * Get the "CampaignId" parameter for this submission if it has been set.
	 * 
	 * @return the campaign id
	 */
	public String getCampaignId() {
		return campaignId;
	}

	/**
	 * Set the "CampaignId" parameter for this submission.
	 * 
	 * @param campaignId
	 *            a campaign id
	 */
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * Get the "AnswerText" parameter for this submission if it has been set.
	 * 
	 * @return the answer text
	 */
	public String getAnswerText() {
		return answerText;
	}

	/**
	 * Set the "AnswerText" parameter for this submission.
	 * 
	 * @param answerText
	 *            an answer text
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	/**
	 * Get the "ContextDataValue_<Dimension-External-Id>" parameter of the given
	 * id for this submission if it has been set.
	 * 
	 * @param id
	 *            the id of context data value
	 * @return the parameter value
	 */
	public String getContextDataValue(String id) {
		return contextDataValue != null ? contextDataValue.get(id) : null;
	}

	/**
	 * Add a "ContextDataValue_<Dimension-External-Id>" parameter of the given
	 * id for this submission.
	 * 
	 * @param id
	 *            the id of the context data value
	 * @param value
	 *            a parameter value
	 */
	public void addContextDataValue(String id, String value) {
		contextDataValue = addToMap(contextDataValue, id, value);
	}

	/**
	 * Get the "Locale" parameter for this submission if it has been set.
	 * 
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Set the "Locale" parameter for this submission.
	 * 
	 * @param locale
	 *            a locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Get the list of "PhotoCaption_{@literal<n>}" parameters for this
	 * submission if any have been added.
	 * 
	 * @return the list of captions
	 */
	public List<String> getPhotoCaptions() {
		return photoCaptions;
	}

	/**
	 * Add a photo caption to the list of "PhotoCaption_{@literal<n>}"
	 * parameters for this submission.
	 * 
	 * @usage For each photo added, the caption must be added in the same order.
	 *        i.e.<br>
	 *        addPhotoUrl(photo1);<br>
	 *        addPhotoUrl(photo2);<br>
	 *        addPhotoCaption(captionfor1);<br>
	 *        addPhotoCaption(captionfor2);<br>
	 * @param photoCaption
	 *            a photo caption
	 */
	public void addPhotoCaption(String photoCaption) {
		if (photoCaptions == null) {
			photoCaptions = new ArrayList<String>();
		}
		photoCaptions.add(photoCaption);
	}

	/**
	 * Get the list of "PhotoUrl_{@literal<n>}" parameters for this submission
	 * if any have been added.
	 * 
	 * @return the list of photo urls
	 */
	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	/**
	 * Add a photo url to the list of "PhotoUrl_{@literal<n>}" parameters for
	 * this submission.
	 * 
	 * @param photoUrl
	 *            the url of a photo hosted by Bazaarvoice (uploaded though the
	 *            API)
	 */
	public void addPhotoUrl(String photoUrl) {
		if (photoUrls == null) {
			photoUrls = new ArrayList<String>();
		}
		photoUrls.add(photoUrl);
	}

	/**
	 * Get the list of "ProductRecommendationId_{@literal<n>}" parameters for
	 * this submission if any have been added.
	 * 
	 * @return the list of product ids
	 */
	public List<String> getProductRecommendationIds() {
		return productRecommendationIds;
	}

	/**
	 * Add a product id to the list of "ProductRecommendationId_{@literal<n>}"
	 * parameters for this submission.
	 * 
	 * @param productId
	 *            a product id
	 */
	public void addProductRecommendationId(String productId) {
		if (productRecommendationIds == null) {
			productRecommendationIds = new ArrayList<String>();
		}
		productRecommendationIds.add(productId);
	}

	/**
	 * Get the value of the "SendEmailAlertWhenPublished" parameter for this
	 * submission if it has been set.
	 * 
	 * @return the value
	 */
	public Boolean isSendEmailAlertWhenPublished() {
		return sendEmailAlertWhenPublished;
	}

	/**
	 * Set the value of the "SendEmailAlertWhenPublished" parameter for this
	 * submission.
	 * 
	 * @param sendEmailAlertWhenPublished
	 *            true for yes, false for no
	 */
	public void setSendEmailAlertWhenPublished(
			boolean sendEmailAlertWhenPublished) {
		this.sendEmailAlertWhenPublished = sendEmailAlertWhenPublished;
	}

	/**
	 * Get the "UserEmail" parameter for this submission if it has been set.
	 * 
	 * @return the user email
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * Set the "UserEmail" parameter for this submission.
	 * 
	 * @param userEmail
	 *            an email address
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * Get the "UserId" parameter for this submission if it has been set.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the "UserId" parameter for this submission.
	 * 
	 * @param userId
	 *            a user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Get the "UserLocation" parameter for this submission if it has been set.
	 * 
	 * @return the user location
	 */
	public String getUserLocation() {
		return userLocation;
	}

	/**
	 * Set the "UserLocation" parameter for this submission.
	 * 
	 * @param userLocation
	 *            a user location.
	 */
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	/**
	 * Get the "UserNickname" parameter for this submission if it has been set.
	 * 
	 * @return the user nickname
	 */
	public String getUserNickname() {
		return userNickname;
	}

	/**
	 * Set the "UserNickname" parameter for this submission.
	 * 
	 * @param userNickname
	 *            a user nickname
	 */
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	/**
	 * Get the list of {@literal "VideoCaption_<n>"} parameters for this
	 * submission if any have been added.
	 * 
	 * @return the list of video captions
	 */
	public List<String> getVideoCaptions() {
		return videoCaptions;
	}

	/**
	 * Add a video caption to the list of {@literal "VideoCaption_<n>"}
	 * parameters for this submission.
	 * 
	 * @usage For each video added, the caption must be added in the same order.
	 *        i.e.<br>
	 *        addVideoUrl(video1);<br>
	 *        addVideoUrl(video2);<br>
	 *        addVideoCaption(captionfor1);<br>
	 *        addVideoCaption(captionfor2);<br>
	 * @param videoCaption
	 *            a video caption
	 */
	public void addVideoCaption(String videoCaption) {
		if (videoCaptions == null) {
			videoCaptions = new ArrayList<String>();
		}
		videoCaptions.add(videoCaption);
	}

	/**
	 * Get the list of {@literal "VideoUrl_<n>"} parameters for this submission
	 * if any have been added.
	 * 
	 * @return the list of video urls
	 */
	public List<String> getVideoUrls() {
		return videoUrls;
	}

	/**
	 * Add a video url to the list of {@literal "VideoUrl_<n>"} parameters for
	 * this submission.
	 * 
	 * @param videoUrl
	 *            the url of a video hosted by either YouTube or Bazaarvoice
	 *            (Uploaded through the API)
	 */
	public void addVideoUrl(String videoUrl) {
		if (videoUrls == null) {
			videoUrls = new ArrayList<String>();
		}
		videoUrls.add(videoUrl);
	}

	/**
	 * Add an "AdditionalField_<Dimension-External-Id>" parameter to the
	 * submission.
	 * 
	 * @param id
	 *            the id of the additional field
	 * @param value
	 *            a value
	 */
	public void addAdditionalField(String id, String value) {
		additionalField = addToMap(additionalField, id, value);
	}

	/**
	 * Get the value of the "AdditionalField_<Dimension-External-Id>" parameter
	 * for this submission with the given id.
	 * 
	 * @param id
	 *            the id of the additional field
	 * @return the value
	 */
	public String getAdditionalField(String id) {
		return additionalField != null ? additionalField.get(id) : null;
	}

	/**
	 * Get the "ProductId" parameter for this submission if it has been set.
	 * 
	 * @return the product id
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * Set the "ProductId" parameter for this submission.
	 * 
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void addTagDim(String type, String value) {
		tagDim = addToMap(tagDim, type, value);
	}

	public String getTagDim(String type) {
		return tagDim != null ? tagDim.get(type) : null;
	}

	/**
	 * Get the "Title" parameter for this submission if it has been set.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the "Title" parameter for this submission.
	 * 
	 * @param title
	 *            a title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the "CategoryId" parameter for this submission if it has been set.
	 * 
	 * @return the category id
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * Set the "CategoryId" parameter for this submission.
	 * 
	 * @param categoryId
	 *            a category id
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Get the value of the "IsRecommended" parameter for this submission if it
	 * has been set.
	 * 
	 * @return the value
	 */
	public Boolean isRecommended() {
		return isRecommended;
	}

	/**
	 * Set the value of the "IsRecommended" parameter for this submission.
	 * 
	 * @param recommended
	 *            true for yes, false for no
	 */
	public void setRecommended(boolean recommended) {
		isRecommended = recommended;
	}

	/**
	 * Get the "NetPromoterComment" parameter for this submission if it has been
	 * set.
	 * 
	 * @return the net promoter comment
	 */
	public String getNetPromoterComment() {
		return netPromoterComment;
	}

	/**
	 * Set the "NetPromoterComment" parameter for this submission.
	 * 
	 * @param netPromoterComment
	 *            a net promoter comment
	 */
	public void setNetPromoterComment(String netPromoterComment) {
		this.netPromoterComment = netPromoterComment;
	}

	/**
	 * Get the "NetPromoterScore" parameter for this submission if it has been
	 * set.
	 * 
	 * @return the net promoter score
	 */
	public Integer getNetPromoterScore() {
		return netPromoterScore;
	}

	/**
	 * Set the "NetPromoterScore" parameter for this submission.
	 * 
	 * @param netPromoterScore
	 *            a net promoter score
	 */
	public void setNetPromoterScore(int netPromoterScore) {
		this.netPromoterScore = netPromoterScore;
	}

	/**
	 * Get the "Rating" parameter for this submission if it has been set.
	 * 
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * Set the "Rating" parameter for this submission.
	 * 
	 * @param rating
	 *            a rating
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * Add a "Rating_<Dimension-External-Id>" parameter to this submission for
	 * the given dimension id.
	 * 
	 * @param id
	 *            the dimension id
	 * @param value
	 *            a rating
	 */
	public void addRatingDim(String id, String value) {
		ratingDim = addToMap(ratingDim, id, value);
	}

	/**
	 * Get the "Rating_<Dimension-External-Id>" parameter for this submission
	 * for the given dimension id if it has been set.
	 * 
	 * @param id
	 *            the dimension id
	 * @return the rating
	 */
	public String getRatingDim(String id) {
		return ratingDim != null ? ratingDim.get(id) : null;
	}

	/**
	 * Get the "ReviewText" parameter for this submission if it has been set.
	 * 
	 * @return the review
	 */
	public String getReviewText() {
		return reviewText;
	}

	/**
	 * Set the "ReviewText" parameter for this submission.
	 * 
	 * @param reviewText
	 *            a review
	 */
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	/**
	 * Get the value of the "IsUserAnonymous" parameter for this submission if
	 * it has been set.
	 * 
	 * @return the value
	 */
	public Boolean isUserAnonymous() {
		return isUserAnonymous;
	}

	/**
	 * Set the value of the "IsUserAnonymous" parameter for this submission.
	 * 
	 * @param userAnonymous
	 *            true for yes, false for no
	 */
	public void setUserAnonymous(boolean userAnonymous) {
		isUserAnonymous = userAnonymous;
	}

	/**
	 * Get the "QuestionSummary" parameter for this submission if it has been
	 * set.
	 * 
	 * @return the question summary
	 */
	public String getQuestionSummary() {
		return questionSummary;
	}

	/**
	 * Set the "QuestionSummary" parameter for this submission.
	 * 
	 * @param questionSummary
	 *            a question summary (single line of text)
	 */
	public void setQuestionSummary(String questionSummary) {
		this.questionSummary = questionSummary;
	}

	/**
	 * Get the "QuestionDetails" parameter for this submission if it has been
	 * set.
	 * 
	 * @return the question details
	 */
	public String getQuestionDetails() {
		return questionDetails;
	}

	/**
	 * Set the "QuestionDetails" parameter for this submission.
	 * 
	 * @param questionDetails
	 *            the question details
	 */
	public void setQuestionDetails(String questionDetails) {
		this.questionDetails = questionDetails;
	}

	/**
	 * Get the "QuestionId" for this submission if it has been set.
	 * 
	 * @return the question id
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * Set the "QuestionId" parameter for this submission.
	 * 
	 * @param questionId
	 *            a question id
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	/**
	 * Get the value of the "SendEmailAlertWhenCommented" parameter of this
	 * submission if it has been set.
	 * 
	 * @return the value
	 */
	public Boolean isSendEmailAlertWhenCommented() {
		return sendEmailAlertWhenCommented;
	}

	/**
	 * Set the value of the "SendEmailAlertWhenCommented" parameter of this
	 * submission.
	 * 
	 * @param sendEmailAlertWhenCommented
	 *            true for yes, false for no
	 */
	public void setSendEmailAlertWhenCommented(
			boolean sendEmailAlertWhenCommented) {
		this.sendEmailAlertWhenCommented = sendEmailAlertWhenCommented;
	}

	/**
	 * Get the "StoryText" parameter for this submission if it has been set.
	 * 
	 * @return the story text
	 */
	public String getStoryText() {
		return storyText;
	}

	/**
	 * Set the "StoryText" parameter for this submission.
	 * 
	 * @param storyText
	 *            a story
	 */
	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}

	/**
	 * Get the "ReviewId" parameter for this submission if it has been set.
	 * 
	 * @return the review id
	 */
	public String getReviewId() {
		return reviewId;
	}

	/**
	 * Set the "ReviewId" parameter for this submission.
	 * 
	 * @param reviewId
	 *            a review id
	 */
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	/**
	 * Get the "StoryId" parameter for this submission if it has been set.
	 * 
	 * @return the story id
	 */
	public String getStoryId() {
		return storyId;
	}

	/**
	 * Set the "StoryId" parameter for this submission.
	 * 
	 * @param storyId
	 *            a story id
	 */
	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	/**
	 * Get the "CommentText" parameter for this submission if it has been set.
	 * 
	 * @return the comment text
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * Set the "CommentText" parameter for this submission.
	 * 
	 * @param commentText
	 *            a comment
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * Add the parameters set in this instance to the given url string.
	 * 
	 * @param url
	 *            the base url to append to
	 * @return the url with parameters
	 */
	@Override
	public String toURL(String url) {
		if (action != null) {
			url = addURLParameter(url, "action", action.toString());
		}
		url = addURLParameter(url, "agreedToTermsAndConditions",
				agreedToTermsAndConditions);
		url = addURLParameter(url, "answerText", answerText);
		url = addURLParameter(url, "campaignId", campaignId);
		url = addURLParameter(url, "contextDataValue", contextDataValue);
		url = addURLParameter(url, "locale", locale);
		url = addNthURLParamsFromList(url, "photoCaption", photoCaptions);
		url = addNthURLParamsFromList(url, "photoUrl", photoUrls);
		url = addNthURLParamsFromList(url, "productRecommendationId",
				productRecommendationIds);
		url = addURLParameter(url, "sendEmailAlertWhenPublished",
				sendEmailAlertWhenPublished);
		url = addURLParameter(url, "userEmail", userEmail);
		url = addURLParameter(url, "userId", userId);
		url = addURLParameter(url, "userLocation", userLocation);
		url = addURLParameter(url, "userNickname", userNickname);
		url = addNthURLParamsFromList(url, "videoCaption", videoCaptions);
		url = addNthURLParamsFromList(url, "videoUrl", videoUrls);
		url = addURLParameter(url, "additionalField", additionalField);
		url = addURLParameter(url, "productId", productId);
		url = addURLParameter(url, "tagDim", tagDim);
		url = addURLParameter(url, "title", title);
		url = addURLParameter(url, "categoryId", categoryId);
		url = addURLParameter(url, "isRecommended", isRecommended);
		url = addURLParameter(url, "netPromoterComment", netPromoterComment);
		url = addURLParameter(url, "netPromoterScore", netPromoterScore);
		url = addURLParameter(url, "rating", rating);
		url = addURLParameter(url, "ratingDim", ratingDim);
		url = addURLParameter(url, "reviewText", reviewText);
		url = addURLParameter(url, "isUserAnonymous", isUserAnonymous);
		url = addURLParameter(url, "questionSummary", questionSummary);
		url = addURLParameter(url, "questionDetails", questionDetails);
		url = addURLParameter(url, "questionId", questionId);
		url = addURLParameter(url, "sendEmailAlertWhenCommented",
				sendEmailAlertWhenCommented);
		url = addURLParameter(url, "storyText", storyText);
		url = addURLParameter(url, "reviewId", reviewId);
		url = addURLParameter(url, "storyId", storyId);
		url = addURLParameter(url, "CommentText", commentText);

		return url;
	}

}
