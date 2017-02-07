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
        StringBuilder strBuilder = new StringBuilder(BVSDK.getInstance().getRootApiUrls().getBazaarvoiceApiRootUrl() );

        strBuilder.append("curations/content/add/");
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