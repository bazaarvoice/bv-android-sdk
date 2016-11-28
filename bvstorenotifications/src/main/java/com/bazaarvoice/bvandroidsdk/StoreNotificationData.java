/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Model containing information necessary to display notification
 */
final class StoreNotificationData extends BVNotificationData {

    @SerializedName("visitDuration")
    private int visitDuration;

    @SerializedName("requestReviewOnAppOpen")
    private boolean requestReviewOnAppOpen;

    @SerializedName("defaultStoreImageUrl")
    private String defaultStoreImageUrl;

    public int getVisitDuration() {
        return visitDuration;
    }

    public long getVisitDurationMillis() {
        return visitDuration * 1000;
    }

    public boolean isRequestReviewOnAppOpen() {
        return requestReviewOnAppOpen;
    }

    public String getDefaultStoreImageUrl() {
        return defaultStoreImageUrl;
    }

    @Override
    public String toString() {
        return "StoreNotificationData{" +
                "visitDuration=" + visitDuration +
                ", requestReviewOnAppOpen=" + requestReviewOnAppOpen +
                ", defaultStoreImageUrl='" + defaultStoreImageUrl + '\'' +
                ", parent='" + super.toString() + '\'' +
                '}';
    }
}
