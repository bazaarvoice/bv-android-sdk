/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Describe file here.
 */
class PhotoUploadResponse extends ConversationsResponseBase{
    @SerializedName("Photo")
    private Photo photo;

    Photo getPhoto() {
        return this.photo;
    }
}