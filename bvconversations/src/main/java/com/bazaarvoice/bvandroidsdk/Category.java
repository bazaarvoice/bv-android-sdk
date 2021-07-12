package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public final class Category extends IncludedContentBase {

    @SerializedName("ParentId") private String parentId;
    @SerializedName("Name") private String name;
    @SerializedName("CategoryPageUrl") private String categoryPageUrl;

    @Nullable
    public String getCategoryParentId() {
        return parentId;
    }

    @Nullable
    public String getCategoryName() {
        return name;
    }

    @Nullable
    public String getCategoryPageUrl() {
        return categoryPageUrl;
    }

}