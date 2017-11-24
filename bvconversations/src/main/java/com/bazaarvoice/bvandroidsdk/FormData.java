package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Container for form field map
 */
public class FormData {
    @SerializedName("Fields")
    Map<String, FormField> formFieldMap;

    @SerializedName("Groups")
    Map<String, FormGroup> formGroupsMap;

    @SerializedName("FieldsOrder")
    List<String> formFieldsOrder;

    @SerializedName("GroupsOrder")
    List<String> formGroupsOrder;

    @Nullable
    public Map<String, FormField> getFormFieldMap() {
        return formFieldMap;
    }
}
