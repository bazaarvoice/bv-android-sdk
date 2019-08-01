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


import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Metadata for the error for a specific field in a form
 */
public class FieldError {
    @SerializedName(value = "Field", alternate = "field") private String field;
    @SerializedName(value = "Message", alternate = "message") private String message;
    @SerializedName(value = "Code", alternate = "code") private String code;
    private transient SubmissionErrorCode errorCode;
    private transient FormField formField;

    public FieldError(){}

    public FieldError(String field, String message, String code) {
        this.field = field;
        this.message = message;
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public SubmissionErrorCode getErrorCode() {
        if (errorCode == null) {
            try {
                errorCode = SubmissionErrorCode.valueOf(getCode());
            } catch (IllegalArgumentException e) {
                errorCode = SubmissionErrorCode.ERROR_UNKNOWN;
            }
        }
        return errorCode;
    }

    void setFormField(FormField formField) {
        this.formField = formField;
    }

    @Nullable
    public FormField getFormField() {
        return formField;
    }
}