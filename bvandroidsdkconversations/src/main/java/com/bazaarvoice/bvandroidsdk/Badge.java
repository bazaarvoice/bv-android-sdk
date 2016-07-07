/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

public class Badge {

    @SerializedName("BadgeType")
    private String badgeType;
    @SerializedName("Id")
    private String id;
    @SerializedName("ContentType")
    private String contentType;

    private transient Type type;

    private void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
        this.type = Type.fromString(this.badgeType);
    }

    public enum Type {
        Merit,
        Custom,
        Affiliation(),
        Rank();

        private static Type fromString(String string) {
            Type type = null;
            try {
                type = Type.valueOf(string);
            }catch (IllegalArgumentException e) {
                type = Type.Custom;
            }

            return type;
        }
    }
}