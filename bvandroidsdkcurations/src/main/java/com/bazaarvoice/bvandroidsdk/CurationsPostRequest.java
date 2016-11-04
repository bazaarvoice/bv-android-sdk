/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;

import com.bazaarvoice.bvandroidsdk.internal.Utils;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to build up request parameters when submitting photo(s) to Curations.
 */

public class CurationsPostRequest {

    Builder builder;
    private CurationsPostRequest(Builder builder){
        this.builder = builder;
    }

    protected String toUrlQueryString() {
        StringBuilder strBuilder = new StringBuilder(BVSDK.getInstance().getRootApiUrls().getCurationsPostApiRootUrl());

        strBuilder.append("?client=");
        strBuilder.append(BVSDK.getInstance().getClientId());
        strBuilder.append("&passkey=");
        strBuilder.append(BVSDK.getInstance().getApiKeys().getApiKeyCurations());

        return strBuilder.toString();
    }

    protected String getJsonPayload(){
        Map<String, Object> payload = new HashMap<>();
        payload.put("author", builder.author);
        payload.put("groups", builder.groups);
        payload.put("text", builder.text);
        Utils.mapPutSafe(payload, "coordinates", builder.coordinate);
        Utils.mapPutSafe(payload, "permalink", builder.permalink);
        Utils.mapPutSafe(payload, "place", builder.place);
        Utils.mapPutSafe(payload, "tags", builder.tags);
        Utils.mapPutSafe(payload, "teaser", builder.teaser);
        Utils.mapPutSafe(payload, "timestamp", builder.timestampInSeconds);
        Utils.mapPutSafe(payload, "links", builder.links);
        Utils.mapPutSafe(payload, "photos", builder.photos);

        return  BVSDK.getInstance().getGson().toJson(payload);
    }

    public static final class Builder {

        protected CurationsAuthor author;
        protected List<String> groups;
        protected String text;
        protected Bitmap bitmap;
        protected CurationsCoordinate coordinate;
        protected String permalink;
        protected String place;
        protected List<String> tags;
        protected String teaser;
        protected Long timestampInSeconds;
        protected List<CurationsLink> links;
        protected List<CurationsPhoto> photos;

        private Builder(CurationsAuthor author, List<String> groups, String text) {
            assert (author != null);
            Assert.assertTrue("Author should not be null", author != null);
            Assert.assertTrue("Groups should contain at least 1 group", groups != null && groups.size() > 0);
            Assert.assertTrue("Text should not be null", text != null);

            this.author = author;
            this.groups = groups;
            this.text = text;
        }

        public Builder(CurationsAuthor author, List<String> groups, String text, Bitmap bitmap) {
            this(author, groups, text);
            this.bitmap = bitmap;
        }

        public Builder(CurationsAuthor author, List<String> groups, String text, List<CurationsPhoto> photos) {
            this(author, groups, text);
            this.photos = photos;
        }

        public Builder permalink(String permalink){
            this.permalink = permalink;
            return this;
        }

        public Builder place(String place){
            this.place = place;
            return this;
        }

        public Builder tags(List<String> tags){
            this.tags = tags;
            return this;
        }

        public Builder teaser(String teaser){
            this.teaser = teaser;
            return this;
        }

        public Builder timestampInSeconds(Long timestampInSeconds){
            this.timestampInSeconds = timestampInSeconds;
            return this;
        }

        public Builder links(List<CurationsLink> links){
            this.links = links;
            return this;
        }

        public Builder geoCoordinates(Double latitude, Double longititude){
            this.coordinate = new CurationsCoordinate(latitude, longititude);
            return this;
        }

        public CurationsPostRequest build(){
            return new CurationsPostRequest(this);
        }
    }
}