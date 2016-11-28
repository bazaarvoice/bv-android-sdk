/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("VideoHost")
    private String videoHost;
    @SerializedName("Caption")
    private String caption;
    @SerializedName("VideoThumbnailUrl")
    private String videoThumbnailUrl;
    @SerializedName("VideoId")
    private String videoId;
    @SerializedName("VideoUrl")
    private String videoUrl;
    @SerializedName("VideoIframeUrl")
    private String videoIframeUrl;

    public String getVideoHost() {
        return videoHost;
    }

    public String getCaption() {
        return caption;
    }

    public String getVideoThumbnailUrl() {
        return videoThumbnailUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoIframeUrl() {
        return videoIframeUrl;
    }
}