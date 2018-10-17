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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import okhttp3.HttpUrl;

import static com.bazaarvoice.bvandroidsdk.StringUtils.componentsSeparatedBy;
import static com.bazaarvoice.bvandroidsdk.StringUtils.isEmpty;
import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * Request for a list of {@link CurationsFeedItem}s
 */
public final class CurationsFeedRequest {

    final List<String> groups;
    final List<String> tags; //content must contain at least one
    final String author;
    final String externalId;
    final String display;
    final Long after; //only content after this date
    final Long before; //only content before this date
    final Integer featured; //counts against limit; e.g limit 25, feature = 10; returns 10 featured and 15 others; If featured > available; available counts against limit
    final Integer limit;
    final Integer per_group_limit;
    final Boolean has_geotag;
    final Boolean withProductData;
    final Boolean has_link;
    final Boolean has_photo;
    final Boolean has_video;
    final Boolean has_photo_or_video;
    final Boolean include_comments;
    final Boolean explicit_permission; //true returns only content that is Rights Management approved.
    final CurationsMedia media;
    final Double latitude, longitude;

    private CurationsFeedRequest(Builder builder) {
        this.groups = builder.groups;
        this.tags = builder.tags;
        this.author = builder.author;
        this.externalId = builder.externalId;
        this.display = builder.display;
        this.after = builder.after;
        this.before = builder.before;
        this.featured = builder.featured;
        this.limit = builder.limit;
        this.per_group_limit = builder.per_group_limit;
        this.has_geotag = builder.has_geotag;
        this.withProductData = builder.withProductData;
        this.has_link = builder.has_link;
        this.has_photo = builder.has_photo;
        this.has_video = builder.has_video;
        this.has_photo_or_video = builder.has_photo_or_video;
        this.include_comments = builder.include_comments;
        this.explicit_permission = builder.explicit_permission;
        this.media = builder.media;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    String toUrlQueryString() {
        BVSDK bvsdk = BVSDK.getInstance();
        BVUserProvidedData bvUserProvidedData = bvsdk.getBvUserProvidedData();
        HttpUrl.Builder builder = HttpUrl.parse(bvsdk.getBvWorkerData().getRootApiUrls().getBazaarvoiceApiRootUrl()).newBuilder();

        builder.addEncodedPathSegments("curations/c3/content/get")
            .addEncodedQueryParameter("client", bvUserProvidedData.getBvConfig().getClientId())
            .addEncodedQueryParameter("passkey", bvUserProvidedData.getBvConfig().getApiKeyCurations());

        if (groups != null && !groups.isEmpty()) {
            for (String group : groups) {
                builder.addEncodedQueryParameter("groups", group);
            }
        }
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                builder.addEncodedQueryParameter("tags", tag);
            }
        }
        if (!isEmpty(author)) {
            builder.addEncodedQueryParameter("author", author);
        }
        if (!isEmpty(externalId)) {
            builder.addEncodedQueryParameter("externalId", externalId);
        }
        if(!isEmpty(display)) {
            builder.addEncodedQueryParameter("display", display);
        }
        if (after != null) {
            builder.addEncodedQueryParameter("after", String.valueOf(after));
        }
        if (before != null) {
            builder.addEncodedQueryParameter("before", String.valueOf(before));
        }
        if (featured != null) {
            builder.addEncodedQueryParameter("featured", String.valueOf(featured));
        }
        if (limit != null) {
            builder.addEncodedQueryParameter("limit", String.valueOf(limit));
        }
        if (per_group_limit != null) {
            builder.addEncodedQueryParameter("per_group_limit", String.valueOf(per_group_limit));
        }
        if (has_geotag != null) {
            builder.addEncodedQueryParameter("has_geotag", String.valueOf(has_geotag));
        }
        if (withProductData != null) {
            builder.addEncodedQueryParameter("withProductData", String.valueOf(withProductData));
        }
        if (has_link != null) {
            builder.addEncodedQueryParameter("has_link", String.valueOf(has_link));
        }
        if (has_photo != null) {
            builder.addEncodedQueryParameter("has_photo", String.valueOf(has_photo));
        }
        if (has_video != null) {
            builder.addEncodedQueryParameter("has_video", String.valueOf(has_video));
        }
        if (has_photo_or_video != null) {
            builder.addEncodedQueryParameter("has_photo_or_video", String.valueOf(has_photo_or_video));
        }
        if (include_comments != null) {
            builder.addEncodedQueryParameter("include_comments", String.valueOf(include_comments));
        }
        if (explicit_permission != null) {
            builder.addEncodedQueryParameter("explicit_permission", String.valueOf(explicit_permission));
        }
        if (media != null) {
            builder.addEncodedQueryParameter("media", getMediaFormatted(media));
        }
        if (latitude != null && longitude != null) {
            builder.addEncodedQueryParameter(
                "geolocation",
                componentsSeparatedBy(asList(latitude, longitude), ","));
        }
        if(bvsdk.getBvUserProvidedData().getBvMobileInfo().getBvSdkVersion()!= null){
            builder.addEncodedQueryParameter("_bvAndroidSdkVersion", bvsdk.getBvUserProvidedData().getBvMobileInfo().getBvSdkVersion());
        }

        HttpUrl httpUrl = builder.build();
        return httpUrl.toString();
    }

    private static String getMediaFormatted(@NonNull final CurationsMedia media) {
        if (isEmpty(media.getMediaType())) {
            throw new IllegalStateException("MediaType cannot be null");
        }
        return format("{'%1$s':{'width':%2$d,'height':%3$d}}", media.getMediaType(), media.getWidth(), media.getHeight());
    }

    public static final class Builder{
        List<String> groups;
        List<String> tags; //content must contain at least one
        String author;
        String externalId;
        String display;
        Long after; //only content after this date
        Long before; //only content before this date
        Integer featured; //counts against limit; e.g limit 25, feature = 10; returns 10 featured and 15 others; If featured > available; available counts against limit
        Integer limit;
        Integer per_group_limit;
        Boolean has_geotag;
        Boolean withProductData;
        Boolean has_link;
        Boolean has_photo;
        Boolean has_video;
        Boolean has_photo_or_video;
        Boolean include_comments;
        Boolean explicit_permission; //true returns only content that is Rights Management approved.
        CurationsMedia media;
        Double latitude, longitude;

        Builder(CurationsFeedRequest request) {
            this.groups = request.groups;
            this.tags = request.tags;
            this.author = request.author;
            this.externalId = request.externalId;
            this.after = request.after;
            this.before = request.before;
            this.featured = request.featured;
            this.limit = request.limit;
            this.per_group_limit = request.limit;
            this.has_geotag = request.has_geotag;
            this.withProductData = request.withProductData;
            this.has_link = request.has_link;
            this.has_photo = request.has_photo;
            this.has_video = request.has_video;
            this.has_photo_or_video = request.has_photo_or_video;
            this.include_comments = request.include_comments;
            this.explicit_permission = request.explicit_permission;
            this.media = request.media;
            this.latitude = request.latitude;
            this.longitude = request.longitude;
        }

        public Builder(@NonNull @Size(min = 1) List<String> groups){
            this.groups = groups;
        }

        public Builder authorTokenOrAlias(@NonNull String authorTokenOrAlias){
            this.author = authorTokenOrAlias;
            return this;
        }

        public Builder externalId(@NonNull String externalId){
            this.externalId = externalId;
            return this;
        }

        public Builder display(@NonNull String display){
            this.display = display;
            return this;
        }

        public Builder after(@NonNull Long after){
            this.after = after;
            return this;
        }

        public Builder before(@NonNull Long before){
            this.before = before;
            return this;
        }

        public Builder featured(@NonNull Integer featured){
            this.featured = featured;
            return this;
        }

        public Builder limit(@NonNull Integer limit){
            this.limit = limit;
            return this;
        }

        @Deprecated
        public Builder perGroupLimit(@NonNull Integer perGroupLimit){
            this.per_group_limit = perGroupLimit;
            return this;
        }

        public Builder hasGeoTag(@NonNull Boolean hasGeoTag){
            this.has_geotag = hasGeoTag;
            return this;
        }

        public Builder hasLink(@NonNull Boolean hasLink){
            this.has_link = hasLink;
            return this;
        }

        public Builder withProductData(@NonNull Boolean withProductData){
            this.withProductData = withProductData;
            return this;
        }

        public Builder hasPhoto(@NonNull Boolean hasPhoto){
            this.has_photo = hasPhoto;
            return this;
        }

        public Builder hasVideo(@NonNull Boolean hasVideo){
            this.has_video = hasVideo;
            return this;
        }

        public Builder hasPhotoOrVideo(@NonNull Boolean hasPhotoOrVideo) {
            this.has_photo_or_video = hasPhotoOrVideo;
            return this;
        }

        public Builder includeComments(@NonNull Boolean includeComments){
            this.include_comments = includeComments;
            return this;
        }

        @Deprecated
        public Builder explicitPermission(@NonNull Boolean explicitPermission){
            this.explicit_permission = explicitPermission;
            return this;
        }

        public Builder media(@NonNull CurationsMedia media){
            this.media = media;
            return this;
        }

        public Builder tags(@NonNull List<String> tags){
            this.tags = tags;
            return this;
        }

        public Builder location(@NonNull Double latitude, @NonNull Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            return this;
        }

        public CurationsFeedRequest build(){
            return new CurationsFeedRequest(this);
        }
    }
}
