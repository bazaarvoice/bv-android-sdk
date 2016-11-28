/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Photo {

    @SerializedName("Caption")
    private String caption;
    @SerializedName("Id")
    private String id;
    @SerializedName("Sizes")
    private Content content;

    public String getCaption() {
        return caption;
    }
    void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public Content getContent() {
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