package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

abstract class BVNotificationData {

    @SerializedName("notificationsEnabled")
    private boolean notificationsEnabled;

    @SerializedName("reviewPromptYesReview")
    private String positiveText;

    @SerializedName("reviewPromptNoReview")
    private String negativeText;

    @SerializedName("reviewPromptRemindText")
    private String neutralText;

    @SerializedName("headsUpEnabled")
    private boolean headsUpEnabled;

    @SerializedName("notificationDelay")
    private int notificationDelay;

    @SerializedName("reviewRemindLaterDuration")
    private int reviewRemindLaterDuration;

    @SerializedName("reviewPromptDisplayText")
    private String contentTitleText;

    @SerializedName("reviewPromptSubtitleText")
    private String summaryText;

    @SerializedName("urlScheme")
    private String urlScheme;

    public int getNotificationDelay() {
        return notificationDelay;
    }

    public long getNotificationDelayMillis() {
        return notificationDelay * 1000;
    }

    public String getPositiveText() {
        return positiveText;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public String getNeutralText() {
        return neutralText;
    }

    public String getContentTitleText() {
        return contentTitleText;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public String getUrlScheme() {
        return urlScheme;
    }

    public int getReviewRemindLaterDuration() {
        return reviewRemindLaterDuration;
    }

    public long getReviewRemindLaterDurationMillis() {
        return reviewRemindLaterDuration * 1000;
    }

    public boolean isHeadsUpEnabled() {
        return headsUpEnabled;
    }

    @Override
    public String toString() {
        return "BVNotificationData{" +
                "notificationsEnabled=" + notificationsEnabled +
                ", positiveText='" + positiveText + '\'' +
                ", negativeText='" + negativeText + '\'' +
                ", neutralText='" + neutralText + '\'' +
                ", headsUpEnabled=" + headsUpEnabled +
                ", notificationDelay=" + notificationDelay +
                ", reviewRemindLaterDuration=" + reviewRemindLaterDuration +
                ", contentTitleText='" + contentTitleText + '\'' +
                ", summaryText='" + summaryText + '\'' +
                ", urlScheme='" + urlScheme + '\'' +
                '}';
    }
}
