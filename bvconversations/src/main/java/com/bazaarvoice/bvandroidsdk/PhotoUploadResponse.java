/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

class PhotoUploadResponse extends ConversationsResponse {
    @SerializedName("Photo")
    private Photo photo;

    Photo getPhoto() {
        return this.photo;
    }
}