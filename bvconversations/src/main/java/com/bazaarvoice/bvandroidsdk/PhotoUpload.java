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

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Helper class to describe a photo to upload through the
 * submission API
 */
class PhotoUpload {
    static final String kCONTENT_TYPE = "contenttype";

    private final File photoFile;
    private final String caption;
    private final ContentType contentType;

    PhotoUpload(@NonNull File photoFile, @Nullable String caption, @NonNull ContentType contentType) {
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
        REVIEW("review"), QUESTION("question"), ANSWER("answer"), COMMENT("review_comment");

        private final String key;
        ContentType(String key) {
            this.key = key;
        }

        String getKey() {
            return this.key;
        }
    }
}