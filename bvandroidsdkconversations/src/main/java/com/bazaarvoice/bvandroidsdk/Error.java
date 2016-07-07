/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("Message")
    private String message;
    @SerializedName("Code")
    private String code;

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}