package com.bazaarvoice.bvandroidsdk;

final class BVNotificationDisplayData {
    private String cgcId;
    private String positiveText;
    private String neutralText;
    private String negativeText;
    private String imageUrl;
    private String titleText;
    private String summaryText;
    private boolean isHeadsUpEnabled;

    String getCgcId() {
        return cgcId;
    }

    String getPositiveText() {
        return positiveText;
    }

    String getNeutralText() {
        return neutralText;
    }

    String getNegativeText() {
        return negativeText;
    }

    String getImageUrl() {
        return imageUrl;
    }

    String getTitleText() {
        return titleText;
    }

    String getSummaryText() {
        return summaryText;
    }

    boolean isHeadsUpEnabled() {
        return isHeadsUpEnabled;
    }

    BVNotificationDisplayData(String cgcId, String positiveText, String neutralText, String negativeText, String imageUrl, String titleText, String summaryText, boolean isHeadsUpEnabled) {
        this.cgcId = cgcId;
        this.positiveText = positiveText;
        this.neutralText = neutralText;
        this.negativeText = negativeText;
        this.imageUrl = imageUrl;
        this.titleText = titleText;
        this.summaryText = summaryText;
        this.isHeadsUpEnabled = isHeadsUpEnabled;
    }

}
