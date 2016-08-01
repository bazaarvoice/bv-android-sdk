/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ConversationsResponse {
    @SerializedName("HasErrors")
    private Boolean hasErrors;
    @SerializedName("Errors")
    private List<Error> errors;
    @SerializedName("FormErrors")
    private FormError formErrors;
    Boolean getHasErrors() {
        return hasErrors;
    }

    List<Error> getErrors() {
        return errors;
    }

    public FormError getFormErrors() {
        return formErrors;
    }
}