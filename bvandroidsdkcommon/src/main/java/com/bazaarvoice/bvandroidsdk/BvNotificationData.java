/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Model containing information necessary to display notification
 */
public final class BvNotificationData {

    @SerializedName("visitDuration")
    private int visitDuration;

    @SerializedName("notificationsEnabled")
    private boolean notificationsEnabled;

    @SerializedName("requestReviewOnAppOpen")
    private boolean requestReviewOnAppOpen;

    @SerializedName("notificationDelay")
    private int notificationDelay;

    @SerializedName("reviewRemindLaterDuration")
    private int reviewRemindLaterDuration;

    @SerializedName("reviewPromptYesReview")
    private String positiveText;

    @SerializedName("reviewPromptNoReview")
    private String negativeText;

    @SerializedName("reviewPromptRemindText")
    private String neutralText;

    @SerializedName("reviewPromptDisplayText")
    private String contentTitleText;

    @SerializedName("reviewPromptSubtitleText")
    private String summaryText;

    @SerializedName("defaultStoreImageUrl")
    private String defaultStoreImageUrl;

    @SerializedName("urlScheme")
    private String urlScheme;

    @SerializedName("headsUpEnabled")
    private boolean headsUpEnabled;

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

    public int getVisitDuration() {
        return visitDuration;
    }

    public long getVisitDurationMillis() {
        return visitDuration * 1000;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public boolean isRequestReviewOnAppOpen() {
        return requestReviewOnAppOpen;
    }

    public String getDefaultStoreImageUrl() {
        return defaultStoreImageUrl;
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
        return "BvNotificationData{" +
                "visitDuration=" + visitDuration +
                ", notificationsEnabled=" + notificationsEnabled +
                ", requestReviewOnAppOpen=" + requestReviewOnAppOpen +
                ", notificationDelay=" + notificationDelay +
                ", positiveText='" + positiveText + '\'' +
                ", negativeText='" + negativeText + '\'' +
                ", neutralText='" + neutralText + '\'' +
                ", contentTitleText='" + contentTitleText + '\'' +
                ", summaryText='" + summaryText + '\'' +
                ", defaultStoreImageUrl='" + defaultStoreImageUrl + '\'' +
                ", headsUpEnabled='" + headsUpEnabled + '\'' +
                ", urlScheme='" + urlScheme + '\'' +
                '}';
    }
}
