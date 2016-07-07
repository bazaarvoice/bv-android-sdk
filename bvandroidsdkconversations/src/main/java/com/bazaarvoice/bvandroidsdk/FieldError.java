/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Describe file here.
 */
class FieldError {

    @SerializedName("Field")
    private String field;
    @SerializedName("Message")
    private String message;
    @SerializedName("Code")
    private String code;
}