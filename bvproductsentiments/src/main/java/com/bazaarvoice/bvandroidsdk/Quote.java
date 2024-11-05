package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class Quote  {
    @SerializedName("quoteId")
    private String quoteId;
    @SerializedName("id")
    private String id;
    @SerializedName("text")
    private String text;
    @SerializedName("emotion")
    private String emotion;
    @SerializedName("reviewRating")
    private int reviewRating;
    @SerializedName("reviewId")
    private String reviewId;
    @SerializedName("reviewedAt")
    private String reviewedAt;
    @SerializedName("translatedText")
    private String translatedText;
    @SerializedName("nativeLanguage")
    private String nativeLanguage;
    @SerializedName("incentivised")
    private boolean incentivised;
    @SerializedName("reviewType")
    private String reviewType;
    private final static long serialVersionUID = -6012454666753937341L;

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(String reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public boolean isIncentivised() {
        return incentivised;
    }

    public void setIncentivised(boolean incentivised) {
        this.incentivised = incentivised;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
