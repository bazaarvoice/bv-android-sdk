/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.io.File;

/**
 * TODO: Describe file here.
 */
class PhotoUpload {
    static final String kCONTENT_TYPE = "contenttype";

    private final File photoFile;
    private final String caption;
    private final ContentType contentType;

    PhotoUpload(File photoFile, String caption, ContentType contentType) {
        this.photoFile = photoFile;
        this.caption = caption;
        this.contentType = contentType;
    }

    String getEndPoint() {
        return "uploadphoto.json";
    }

    ContentType getContentType() {
        return this.contentType;
    }

    File getPhotoFile() {
        return this.photoFile;
    }

    public String getCaption() {
        return caption;
    }

    enum ContentType {
        Review("review"), Question("question"), Answer("answer");

        private final String key;
        ContentType(String key) {
            this.key = key;
        }

        String getKey() {
            return this.key;
        }
    }
}