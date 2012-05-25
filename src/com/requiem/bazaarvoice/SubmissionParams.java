package com.requiem.bazaarvoice;

import java.util.List;
import java.util.Map;

/**
 * User: gary
 * Date: 5/6/12
 * Time: 9:30 PM
 */
public class SubmissionParams extends BazaarParams {

    //used by all (except photo, video - handled in SubmissionMediaParams
    private Action action;
    private Boolean agreedToTermsAndConditions;
    private String answerText;
    private String campaignId;
    private Map<String, String> contextDataValue;
    private String locale;
    private String photoCaption;
    private String photoUrl;
    private Integer productRecommendationId;
    private Boolean sendEmailAlertWhenPublished;
    private String userEmail;
    private String userId;
    private String userLocation;
    private String userNickname;
    private String videoCaption;
    private String videoUrl;


    //used by review, question, answer, story
    private Map<String, String>  additionalField;

    //used by review, question, story
    private String productId;

    private Map<String, String>  tagDim;

    //used by review, story, comment
    private String title;

    //used by question, story
    private String categoryId;

    //used by review
    private Boolean isRecommended;
    private String netPromoterComment;
    private Integer netPromoterScore;
    private Integer rating;
    private Map<String, String> ratingDim;
    private String reviewText;

    //used by question
    private Boolean isUserAnonymous;
    private String questionSummary;
    private String questionDetails;


    //used by answer
    private String questionId;


    //used by story
    private Boolean sendEmailAlertWhenCommented;
    private String storyText;


    //used by comment
    private String reviewId;
    private String storyId;
    private String commentText;


    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Boolean isAgreedToTermsAndConditions() {
        return agreedToTermsAndConditions;
    }

    public void setAgreedToTermsAndConditions(boolean agreedToTermsAndConditions) {
        this.agreedToTermsAndConditions = agreedToTermsAndConditions;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getContextDataValue(String type) {
        return contextDataValue != null?contextDataValue.get(type):null;
    }

    public void addContextDataValue(String type, String value) {
        contextDataValue = addToMap(contextDataValue, type, value);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPhotoCaption() {
        return photoCaption;
    }

    public void setPhotoCaption(String photoCaption) {
        this.photoCaption = photoCaption;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getProductRecommendationId() {
        return productRecommendationId;
    }

    public void setProductRecommendationId(int productRecommendationId) {
        this.productRecommendationId = productRecommendationId;
    }

    public Boolean isSendEmailAlertWhenPublished() {
        return sendEmailAlertWhenPublished;
    }

    public void setSendEmailAlertWhenPublished(boolean sendEmailAlertWhenPublished) {
        this.sendEmailAlertWhenPublished = sendEmailAlertWhenPublished;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getVideoCaption() {
        return videoCaption;
    }

    public void setVideoCaption(String videoCaption) {
        this.videoCaption = videoCaption;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void addAdditionalField(String type, String value) {
        additionalField = addToMap(additionalField, type, value);
    }

    public String getAdditionalField(String type) {
        return additionalField != null?additionalField.get(type):null;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void addTagDim(String type, String value) {
        tagDim = addToMap(tagDim, type, value);
    }

    public String getTagDim(String type) {
        return tagDim != null?tagDim.get(type):null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public String getNetPromoterComment() {
        return netPromoterComment;
    }

    public void setNetPromoterComment(String netPromoterComment) {
        this.netPromoterComment = netPromoterComment;
    }

    public Integer getNetPromoterScore() {
        return netPromoterScore;
    }

    public void setNetPromoterScore(int netPromoterScore) {
        this.netPromoterScore = netPromoterScore;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void addRatingDim(String type, String value) {
        ratingDim = addToMap(ratingDim, type, value);
    }

    public String getRatingDim(String type) {
        return ratingDim != null?ratingDim.get(type):null;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Boolean isUserAnonymous() {
        return isUserAnonymous;
    }

    public void setUserAnonymous(boolean userAnonymous) {
        isUserAnonymous = userAnonymous;
    }

    public String getQuestionSummary() {
        return questionSummary;
    }

    public void setQuestionSummary(String questionSummary) {
        this.questionSummary = questionSummary;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(String questionDetails) {
        this.questionDetails = questionDetails;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Boolean isSendEmailAlertWhenCommented() {
        return sendEmailAlertWhenCommented;
    }

    public void setSendEmailAlertWhenCommented(boolean sendEmailAlertWhenCommented) {
        this.sendEmailAlertWhenCommented = sendEmailAlertWhenCommented;
    }

    public String getStoryText() {
        return storyText;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    /**
     * Convert the class into a url parameters string
     * @param url the base url to append to
     * @return the url with parameters
     */
    @Override
    public String toURL(String url) {
        if (action != null) {
            url = addURLParameter(url,"action", action.toString());
        }
        url = addURLParameter(url,"agreedToTermsAndConditions", agreedToTermsAndConditions);
        url = addURLParameter(url,"answerText", answerText);
        url = addURLParameter(url,"campaignId", campaignId);
        url = addURLParameter(url,"contextDataValue", contextDataValue);
        url = addURLParameter(url,"locale", locale);
        url = addURLParameter(url,"photoCaption", photoCaption);
        url = addURLParameter(url,"photoUrl", photoUrl);
        url = addURLParameter(url,"productRecommendationId", productRecommendationId);
        url = addURLParameter(url,"sendEmailAlertWhenPublished", sendEmailAlertWhenPublished);
        url = addURLParameter(url,"userEmail", userEmail);
        url = addURLParameter(url,"userId", userId);
        url = addURLParameter(url,"userLocation", userLocation);
        url = addURLParameter(url,"userNickname", userNickname);
        url = addURLParameter(url,"videoCaption", videoCaption);
        url = addURLParameter(url,"videoUrl", videoUrl);
        url = addURLParameter(url,"additionalField", additionalField);
        url = addURLParameter(url,"productId", productId);
        url = addURLParameter(url,"tagDim", tagDim);
        url = addURLParameter(url,"title", title);
        url = addURLParameter(url,"categoryId", categoryId);
        url = addURLParameter(url,"isRecommended", isRecommended);
        url = addURLParameter(url,"netPromoterComment", netPromoterComment);
        url = addURLParameter(url,"netPromoterScore", netPromoterScore);
        url = addURLParameter(url,"rating", rating);
        url = addURLParameter(url,"ratingDim", ratingDim);
        url = addURLParameter(url,"reviewText", reviewText);
        url = addURLParameter(url,"isUserAnonymous", isUserAnonymous);
        url = addURLParameter(url,"questionSummary", questionSummary);
        url = addURLParameter(url,"questionDetails", questionDetails);
        url = addURLParameter(url,"questionId", questionId);
        url = addURLParameter(url,"sendEmailAlertWhenCommented", sendEmailAlertWhenCommented);
        url = addURLParameter(url,"storyText", storyText);
        url = addURLParameter(url,"reviewId", reviewId);
        url = addURLParameter(url,"storyId", storyId);
        url = addURLParameter(url,"CommentText", commentText);

        return url;
    }

}
