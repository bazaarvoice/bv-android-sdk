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

import java.util.Map;

/**
 * Metadata for a video and its link
 */
public class Video {

    @SerializedName("Caption")
    private String caption;
    @SerializedName("Id")
    private String id;
    @SerializedName("Sizes")
    private Video.Content content;


    public String getCaption() {
        return caption;
    }
    void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public Video.Content getContent() {

        return content;
    }

    public class Content {
        private static final String kURL = "Url";

        @SerializedName("thumbnail")
        private Map<String, Object> thumbnail;
        @SerializedName("normal")
        private Map<String, Object> normal;

        private transient String thumbnailUrl;
        private transient String normalUrl;

        public String getThumbnailUrl() {
            return (String) thumbnail.get(kURL);
        }

        public String getNormalUrl() {
            return (String) normal.get(kURL);
        }
    }
}