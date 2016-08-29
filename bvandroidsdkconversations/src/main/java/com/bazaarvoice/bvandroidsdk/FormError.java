/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class FormError {

    @SerializedName("FieldErrors")
    private Map<String, FieldError> fieldErrorMap;

    public Map<String, FieldError> getFieldErrorMap() {
        return fieldErrorMap;
    }
}