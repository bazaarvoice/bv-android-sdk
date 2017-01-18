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

import com.google.gson.annotations.SerializedName;

/**
 * Icons that allow content and/or contributors to be recognized,
 * or flagged, as having an additional meaning. Badges can support
 * such concepts as 'featured' content, indicating a contributor's
 * expertise on a product, acknowledge incentivized content, and
 * also top content providers. Badges are placed on user generated
 * content (reviews, questions, answers) or on contributors
 */
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

    public Type getType() {
        return Type.fromString(badgeType);
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