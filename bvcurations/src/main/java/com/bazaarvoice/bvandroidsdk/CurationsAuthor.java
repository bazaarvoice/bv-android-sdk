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

import androidx.annotation.NonNull;

/**
 * Author of a {@link CurationsFeedItem}
 */
public class CurationsAuthor {

    @SerializedName("profile")
    protected String profileUrl;
    protected String username;
    protected String alias;
    protected String token;
    @SerializedName("avatar")
    protected String avatarUrl;
    protected String channel;

    public String getProfileUrl(){return profileUrl;}
    public String getUsername(){return username;}
    public String getAlias(){return alias;}
    public String getToken(){return token;}
    public String getAvatarUrl(){return avatarUrl;}
    public String getChannel(){return channel;}

    protected CurationsAuthor(){}

    private CurationsAuthor(Builder builder){
        this.alias = builder.alias;
        this.token = builder.token;
        this.profileUrl = builder.profileUrl;
        this.avatarUrl = builder.avatarUrl;
    }

    public static final class Builder{

        private String alias;
        private String token;
        private String profileUrl;
        private String avatarUrl;

        public Builder(@NonNull String alias, @NonNull String token) {
            this.alias = alias;
            this.token = token;
        }

        public Builder profileUrl(@NonNull String profileUrl){
            this.profileUrl = profileUrl;
            return this;
        }

        public Builder avatarUrl(@NonNull String avatarUrl){
            this.avatarUrl = avatarUrl;
            return this;
        }

        public CurationsAuthor build(){
            return new CurationsAuthor(this);
        }
    }
}
