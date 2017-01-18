/*
 * Copyright 2017
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
 *
 */

package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Metadata for a video and its link
 */
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