package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bazaarvoice on 3/31/16.
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

        public Builder(String alias, String token) {
            this.alias = alias;
            this.token = token;
        }

        public Builder profileUrl(String profileUrl){
            this.profileUrl = profileUrl;
            return this;
        }

        public Builder avatarUrl(String avatarUrl){
            this.avatarUrl = avatarUrl;
            return this;
        }

        public CurationsAuthor build(){
            return new CurationsAuthor(this);
        }
    }
}
