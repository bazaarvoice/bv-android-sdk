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
package com.bazaarvoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bazaarvoice.types.Action;
import com.bazaarvoice.types.FeedbackContentType;
import com.bazaarvoice.types.FeedbackType;
import com.bazaarvoice.types.FeedbackVoteType;

/**
 * 
 * Handles the parameters for content submission. <br>
 * Use of this class will rely on knowledge of the <a
 * href="http://developer.bazaarvoice.com/">Bazaarvoice API</a>. You should use
 * this site as a reference for which parameters to pass using this class.
 */
public class SubmissionParams extends BazaarParams {

	// used by all (except photo, video - handled in SubmissionMediaParams
	private Action action;
	private Boolean agreedToTermsAndConditions;
	private String campaignId;
	private String locale; 
	private Boolean sendEmailAlertWhenPublished;
	private String userEmail;
	private String userId;
	private String userLocation;
	private String userNickname;
	
	private Map<String, String> contextDataValue;
	private List<String> photoCaptions;
	private List<String> photoUrls;
	private List<String> productRecommendationIds;
	private List<String> videoCaptions;
	private List<String> videoUrls;	

	// used by review, question, answer, story
	private Map<String, String> additionalField;

	// used by review, question, story
	private String productId;

	private Map<String, String> tagDim;
	private Map<String, String> tagIdDim;


	// used by review, story, comment
	private String title;

	// used by question, story
	private String categoryId;

	// used by review
	private Boolean isRecommended;
	private String netPromoterComment;
	private Integer netPromoterScore;
	private Integer rating;
	private String reviewText;

	private Map<String, String> ratingDim;

	// used by question
	private Boolean isUserAnonymous;
	private String questionSummary;
	private String questionDetails;

	// used by answer
	private String answerText;
	private String questionId;

	// used by story
	private Boolean sendEmailAlertWhenCommented;
	private String storyText;

	// used by comment
	private String reviewId;
	private String storyId;
	private String commentText;
	
	// used by feedback
	private String contentId;
	private FeedbackContentType contentType;
	private FeedbackType feedbackType;
	private String reasonText;
	private FeedbackVoteType vote;
	
	/**
	 * Creates a SubmissionParams instance.
	 */
	public SubmissionParams() {
	}

	/***** Used by All Types of Submission Requests *******/
	
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
	 * The submission action to take -- either Preview or Submit. Preview will result in a draft of the content to be submitted; Submit will submit the content.
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
	public Boolean getAgreedToTermsAndConditions() {
		return agreedToTermsAndConditions;
	}
	
	/**
 	 * Boolean indicating whether or not the user agreed to the terms and conditions.  Required depending on the client's settings.
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
	 *  Arbitrary text that may be saved alongside content to indicate vehicle by which content was captured, e.g. �post-purchase email�.
	 * 
	 * @param campaignId
	 *            a campaign id
	 */
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
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
	 * Locale to display Labels, Configuration, Product Attributes and Category Attributes in. The default value is the locale defined in the display associated with the API key.
	 * 
	 * @param locale
	 *            a locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * Get the value of the "SendEmailAlertWhenPublished" parameter of this
	 * submission if it has been set.
	 * 
	 * @return the value
	 */
	public Boolean getSendEmailAlertWhenPublished() {
		return sendEmailAlertWhenPublished;
	}

	/**
	 *  Boolean indicating whether or not the user wants to be notified when his/her content is published.
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
	 * User's email address.
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
	 * User's external ID.
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
	 * User location text.
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
	 * User nickname display text.
	 * 
	 * @param userNickname
	 *            a user nickname
	 */
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	/**
	 * Sets ContextDataValue for a particular dimensionExternalId. In general, this parameter is used for custom parameters that a client may want to track.  Some examples of this parameter include the following:
	 * - dimensionExternalId:PurchaserRank value:"top10" -> ContextDataValue_PurchaserRank=top10
	 * - dimensionExternalId:Purchaser value:"yes" -> ContextDataValue_Purchaser=yes
	 * - dimensionExternalId:Age value:"35to44" -> ContextDataValue_Age=35to44
	 * - dimensionExternalId:Gender value:"male" -> ContextDataValue_Gender=male
	 * 
	 * @param dimensionExternalId
	 *            the dimensionExternalId of the context data value
	 * @param value
	 *            a parameter value
	 */
	public void addContextDataValue(String dimensionExternalId, String value) {
		contextDataValue = addToMap(contextDataValue, dimensionExternalId, value);
	}

	public Map<String, String> getContextDataValue() {
		return contextDataValue;
	}
	
	/**
	 * Associates a photo caption with this submission.
	 * 
	 * <p>
	 * <b>Usage:</b><br>
	 * For each photo added, the caption must be added in the same order. i.e.
	 * <p>
	 * addPhotoUrl(photo1);<br>
	 * addPhotoUrl(photo2);<br>
	 * addPhotoCaption(captionfor1);<br>
	 * addPhotoCaption(captionfor2);<br>
	 * 
	 * @param photoCaption
	 *            a photo caption
	 */
	public void addPhotoCaption(String photoCaption) {
		if (photoCaptions == null) {
			photoCaptions = new ArrayList<String>();
		}
		photoCaptions.add(photoCaption);
	}
	
	public List<String> getPhotoCaptions() {
		return photoCaptions;
	}

	/**
	 * Associates a photo url with this submission.
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
	
	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	/**
	 * Add a product id to the list of "ProductRecommendationId_{@literal <n>}"
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
	
	public List<String> getProductRecommendationIds() {
		return productRecommendationIds;
	}

	/**
	 *  Associates a video caption with this submission.
	 * 
	 * <p>
	 * <b>Usage:</b><br>
	 * For each video added, the caption must be added in the same order. i.e.
	 * <p>
	 * addVideoUrl(video1);<br>
	 * addVideoUrl(video2);<br>
	 * addVideoCaption(captionfor1);<br>
	 * addVideoCaption(captionfor2);<br>
	 * 
	 * @param videoCaption
	 *            a video caption
	 */
	public void addVideoCaption(String videoCaption) {
		if (videoCaptions == null) {
			videoCaptions = new ArrayList<String>();
		}
		videoCaptions.add(videoCaption);
	}

	public List<String> getVideoCaptions() {
		return videoCaptions;
	}
	/**
	 * Associates a video url with this submission.
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
	
	public List<String> getVideoUrls() {
		return videoUrls;
	}

	/***** Used by Review, Question, Answer, Story *******/

	/**
	 * Sets AdditionalField for a particular dimensionExternalId.  In general, this parameter is used to attach additional information to a submission.  
	 * A concrete example of the parameter might be a dimensionExternalId of'Seat' with a value of '24F' (describing the seat number at a stadium or on a 
	 * plane) resulting in a parameter AdditionalField_Seat=24F.
	 * 
	 * @param id
	 *            the id of the additional field
	 * @param value
	 *            a value
	 */
	public void addAdditionalField(String id, String value) {
		additionalField = addToMap(additionalField, id, value);
	}
	
	public Map<String, String> getAdditionalField() {
		return additionalField;
	}

	/***** Used by Review, Question, Story *******/

	/**
	 * Get the "ProductId" parameter for this submission if it has been set.
	 * 
	 * @return the product id
	 */
	public String getProductId() {
		return productId;
	}
	
	/**
	 *  The id of the product that this content is being submitted on.
	 * 
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * Adds a tag with a particular dimensionExternalId and value.  For example, a client might add the value 
	 * "comfort" for the dimensionExternalId of "Pro" and "expensive" with a dimensionExternalId of "Con."
	 * 
	 * @param dimensionExternalId
	 *            dimension external id for this tag
	 * @param value
	 *             tag value.
	 */
	public void addTagForDimensionExternalId(String dimensionExternalId, String value) {
		tagDim = addToMap(tagDim, dimensionExternalId, value);
	}
	
	public Map<String, String> getTagForDimensionExternalId() {
		return tagDim;
	}

	/**
	 * Adds a tag with a particular dimensionExternalId and value.  For example, a client might add the value 
	 * "comfort" for the dimensionExternalId of "Pro" and "expensive" with a dimensionExternalId of "Con."
	 * 
	 * @param dimensionExternalId
	 *            dimension external id for this tag
	 * @param value
	 *             tag value.
	 */
	public void addTagIdForDimensionExternalId(String dimensionExternalId, Boolean value) {
		tagIdDim = addToMap(tagIdDim, dimensionExternalId, value.toString());
	}
	
	public Map<String, String> getTagIdForDimensionExternalId() {
		return tagIdDim;
	}

	/***** Used by Review, Story, Comment *******/
	
	/**
	 * Get the "Title" parameter for this submission if it has been set.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Content title text.
	 * 
	 * @param title
	 *            a title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/***** Used by Question, Story *******/

	/**
	 * Get the "CategoryId" parameter for this submission if it has been set.
	 * 
	 * @return the category id
	 */
	public String getCategoryId() {
		return categoryId;
	}
	
	/**
	 * The id of the category that this content is being submitted on.
	 * 
	 * @param categoryId
	 *            a category id
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/***** Used by Review *******/
	
	/**
	 * Get the value of the "IsRecommended" parameter for this submission if it
	 * has been set.
	 * 
	 * @return the value
	 */
	public Boolean getRecommended() {
		return isRecommended;
	}
	
	/**
	 * Boolean answer to "I would recommend this to a friend".  Required dependent on client settings.
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
	 * Text representing a user comment to explain numerical Net Promoter score.
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
	 * Value is a positive integer between 1 and 10 representing a numerical rating in response to �How would you rate this company?�
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
	 * Value is positive integer between 1 and 5, and represents review overall rating.  Required depending on client settings.
	 * 
	 * @param rating
	 *            a rating
	 */
	public void setRating(int rating) {
		this.rating = rating;
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
	 * Contains the text of an review for a review submission.
	 * 
	 * @param reviewText
	 *            text of a review
	 */
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	
	/**
	 * Sets a rating for a particular dimensionExternalId.  A concrete example might be a dimensionExternalId of "Quality" where the value would 
	 * represent the user's opinion of the quality of the product.
	 * 
	 * @param dimensionExternalId
	 *            the dimension id
	 * @param value
	 *            a rating
	 */
	public void addRatingForDimensionExternalId(String dimensionExternalId, String value) {
		ratingDim = addToMap(ratingDim, dimensionExternalId, value);
	}

	public Map<String, String> getRatingForDimensionExternalId() {
		return ratingDim;
	}
	/***** Used by Question *******/

	/**
	 * Get the value of the "IsUserAnonymous" parameter for this submission if
	 * it has been set.
	 * 
	 * @return the value
	 */
	public Boolean getIsUserAnonymous() {
		return isUserAnonymous;
	}
	
	/**
	 * Indicates whether this submission should be displayed anonymously.
	 * 
	 * @param userAnonymous
	 *            true for yes, false for no
	 */
	public void setIsUserAnonymous(boolean userAnonymous) {
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
	 * Contains the text of the question summary for a question request. Only a single line of text is allowed.
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
	 * Contains the text of the question details for a question request. Multiple lines of text are allowed.
	 * 
	 * @param questionDetails
	 *            the question details
	 */
	public void setQuestionDetails(String questionDetails) {
		this.questionDetails = questionDetails;
	}

	/***** Used by Answer *******/
	/**
	 * Get the "AnswerText" parameter for this submission if it has been set.
	 * 
	 * @return the answer text
	 */
	public String getAnswerText() {
		return answerText;
	}
	
	/**
	 * Contains the text of an answer for an answer request.
	 * 
	 * @param answerText
	 *            an answer text
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
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
	 * The id of the category that this content is being submitted on for a BVPostTypeQuestion request. For such a request, one of productId or categoryId must be provided.
	 * 
	 * @param questionId
	 *            a question id
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	/***** Used by Story *******/
	/**
	 * Get the value of the "SendEmailAlertWhenCommented" parameter of this
	 * submission if it has been set.
	 * 
	 * @return the value
	 */
	public Boolean getSendEmailAlertWhenCommented() {
		return sendEmailAlertWhenCommented;
	}
	
	/**
	 * Boolean indicating whether or not the user wants to be notified when a comment is posted on the content.
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
	 *  The text of the story for a story request.
	 * 
	 * @param storyText
	 *            a story
	 */
	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}
	
	/***** Used by Comment *******/
	/**
	 * Get the "ReviewId" parameter for this submission if it has been set.
	 * 
	 * @return the review id
	 */
	public String getReviewId() {
		return reviewId;
	}
	
	/**
	 * The id of the review that a comment is being submitted on for a comment request.  This field is required for comment requests.
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
	 * The id of the story that a comment is being submitted on for a story comment request. This field is required for story comment requests.
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
	 *  The text of the comment for a comment request.
	 * 
	 * @param commentText
	 *            a comment
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/***** Used by Feedback *******/
	/**
	 * Get the "ContentId" parameter for this submission if it has been set
	 * 
	 * @return the contentId
	 */
	public String getContentId() {
		return contentId;
	}
	
	/**
	 * Sets ID of the content with which a feedback request is associated.
	 * 
	 * @param contentId
	 *            id to set
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	/**
	 * Get the "ContentType" parameter for this submission if it has been set
	 * 
	 * @return the contentType
	 */
	public FeedbackContentType getContentType() {
		return contentType;
	}
	
	/**
	 * Sets the type of content with which a feedback request is associated.
	 * 
	 * @param contentType
	 *            type of feedback content
	 */
	public void setContentType(FeedbackContentType contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * Get the "FeedbackType" parameter for this submission if it has been set
	 * 
	 * @return  the feedbackType
	 */
	public FeedbackType getFeedbackType() {
		return feedbackType;
	}
	
	/**
	 * Sets the type of feedback with which a feedback request is associated. (inappropriate or helpfulness)
	 * 
	 * @param feedbackType
	 *            type of feedback
	 */
	public void setFeedbackType(FeedbackType feedbackType) {
		this.feedbackType = feedbackType;
	}
	
	/**
	 * Get the "ReasonText" parameter for this submission if it has been set
	 * 
	 * @return the reasonText
	 */
	public String getReasonText() {
		return reasonText;
	}
	
	/**
	 * Reason that feedback content has been flagged as inappropriate.
	 * 
	 * @param reasonText
	 *            text reason
	 */
	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}
	
	/**
	 * Get the "Vote" parameter for this submission if it has been set
	 * 
	 * @param feedbackVoteType
	 *           the feedbackVoteType
	 */
	public FeedbackVoteType getVote() {
		return vote;
	}
	
	/**
	 *  Helpfulness vote for this content (positive or negative). This parameter is only required for feedback type of helpfulness.
	 * 
	 * @param feedbackVoteType
	 *            vote type
	 */
	public void setVote(FeedbackVoteType feedbackVoteType) {
		this.vote = feedbackVoteType;
	}

	@Override
	protected String toURL(String apiVersion, String passKey) {
		StringBuilder url = new StringBuilder();
		
		url.append(addURLParameter("apiversion", apiVersion));
		url.append(addURLParameter("passkey", passKey));
		
		if (getAction() != null) {
			url.append(addURLParameter("action", getAction().getActionName()));
		}
		url.append(addURLParameter("agreedToTermsAndConditions",
				getAgreedToTermsAndConditions()));
		url.append(addURLParameter("campaignId", getCampaignId()));
		url.append(addURLParameter("locale", getLocale()));
		url.append(addURLParameter("sendEmailAlertWhenPublished",
				getSendEmailAlertWhenPublished()));
		url.append(addURLParameter("userEmail", getUserEmail()));
		url.append(addURLParameter("userId", getUserId()));
		url.append(addURLParameter("userLocation", getUserLocation()));
		url.append(addURLParameter("userNickname", getUserNickname()));
		
		url.append(addURLParameter("contextDataValue", getContextDataValue()));
		url.append(addNthURLParamsFromList("photoCaption", getPhotoCaptions()));
		url.append(addNthURLParamsFromList("photoUrl", getPhotoUrls()));
		url.append(addNthURLParamsFromList("productRecommendationId",
				getProductRecommendationIds()));
		url.append(addNthURLParamsFromList("videoCaption", getVideoCaptions()));
		url.append(addNthURLParamsFromList("videoUrl", getVideoUrls()));
		
		url.append(addURLParameter("additionalField", getAdditionalField()));
		
		url.append(addURLParameter("productId", getProductId()));
		
		url.append(addURLParameter("tag", getTagForDimensionExternalId()));
		url.append(addURLParameter("tagid", getTagIdForDimensionExternalId()));
		
		url.append(addURLParameter("title", getTitle()));
		
		url.append(addURLParameter("categoryId", getCategoryId()));
		
		url.append(addURLParameter("isRecommended", getRecommended()));
		url.append(addURLParameter("netPromoterComment", getNetPromoterComment()));
		url.append(addURLParameter("netPromoterScore", getNetPromoterScore()));
		url.append(addURLParameter("rating", getRating()));
		url.append(addURLParameter("reviewText", getReviewText()));
		
		url.append(addURLParameter("rating", getRatingForDimensionExternalId()));
		
		url.append(addURLParameter("isUserAnonymous", getIsUserAnonymous()));
		url.append(addURLParameter("questionSummary", getQuestionSummary()));
		url.append(addURLParameter("questionDetails", getQuestionDetails()));
		
		url.append(addURLParameter("answerText", getAnswerText()));
		url.append(addURLParameter("questionId", getQuestionId()));
		
		url.append(addURLParameter("sendEmailAlertWhenCommented",
				getSendEmailAlertWhenCommented()));
		url.append(addURLParameter("storyText", getStoryText()));
		
		url.append(addURLParameter("reviewId", getReviewId()));
		url.append(addURLParameter("storyId", getStoryId()));
		url.append(addURLParameter("CommentText", getCommentText()));
		
		url.append(addURLParameter("contentId", getContentId()));
		if(getContentType() != null){
			url.append(addURLParameter("contentType", getContentType().getTypeString()));			
		}
		if(getFeedbackType() != null){
			url.append(addURLParameter("feedbackType", getFeedbackType().getTypeString()));
		}
		url.append(addURLParameter("reasonText", getReasonText()));
		if(getVote() != null){
			url.append(addURLParameter("vote", getVote().getTypeString()));
		}
		return url.toString();
	}

	@Override
	protected void addPostParameters(String apiVersion, String passKey,
			BazaarRequest request) {
		// just a method stub		
	}
}
