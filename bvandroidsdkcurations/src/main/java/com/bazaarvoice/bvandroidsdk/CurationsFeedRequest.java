package com.bazaarvoice.bvandroidsdk;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Bazaarvoice on 3/31/16.
 */
public class CurationsFeedRequest {

    protected Builder builder;
    private CurationsFeedRequest(Builder builder){
        this.builder = builder;
    }

    protected String toUrlQueryString(){
        StringBuilder strBuilder = new StringBuilder(BVSDK.getInstance().getRootApiUrls().getCurationsDisplayApiRootUrl());

        strBuilder.append("client=");
        strBuilder.append(BVSDK.getInstance().getClientId());
        strBuilder.append("&passKey=");
        strBuilder.append(BVSDK.getInstance().getApiKeys().getApiKeyCurations());
        for (Field f : builder.getClass().getDeclaredFields()) {

            Object object = null;
            try {
                object = f.get(builder);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (object != null){
                if (f.getName().equals("groups") || f.getName().equals("tags")){
                    strBuilder.append("&" + f.getName() + "=" + StringUtils.componentsSeparatedBy((List<String>)object, "&"+f.getName()+"="));
                } else if (f.getName().equals("media")) {
                    String mediaStr = "&media={'" + builder.media.mediaType + "':{'width':" + builder.media.width + ",'height':" + builder.media.height + "}}";
                    strBuilder.append(mediaStr);
                } else if (f.getName().equals("latitude")) {
                    if (builder.latitude != null && builder.longitude != null) {
                        String geolocationStr = "&geolocation=" + builder.latitude + "," + builder.longitude;
                        strBuilder.append(geolocationStr);
                    }
                } else if (f.getName().equals("longitude")) {
                    // no-op
                } else {
                    strBuilder.append("&" + f.getName() + "=" + object);
                }
            }
        }
        return strBuilder.toString();
    }

    public static final class Builder{

        protected List<String> groups;
        protected List<String> tags; //content must contain at least one
        protected String client; //provided by sdk
        protected String passKey; // provided by sdk
        protected String author;
        protected String externalId;
        protected Long after; //only content after this date
        protected Long before; //only content before this date
        protected Integer featured; //counts against limit; e.g limit 25, feature = 10; returns 10 featured and 15 others; If featured > available; available counts against limit
        protected Integer limit;
        protected Integer per_group_limit;
        protected Boolean has_geotag;
        protected Boolean withProductData;
        protected Boolean has_link;
        protected Boolean has_photo;
        protected Boolean has_video;
        protected Boolean include_comments;
        protected Boolean explicit_permission; //true returns only content that is Rights Management approved.
        protected CurationsMedia media;
        protected Double latitude, longitude;
        //TODO success

        public Builder(List<String> groups){
            this.groups = groups;
        }

        public Builder authorTokenOrAlias(String authorTokenOrAlias){
            this.author = authorTokenOrAlias;
            return this;
        }

        public Builder externalId(String externalId){
            this.externalId = externalId;
            return this;
        }

        public Builder after(Long after){
            this.after = after;
            return this;
        }

        public Builder before(Long before){
            this.before = before;
            return this;
        }

        public Builder featured(Integer featured){
            this.featured = featured;
            return this;
        }

        public Builder limit(Integer limit){
            this.limit = limit;
            return this;
        }

        @Deprecated
        public Builder perGroupLimit(Integer perGroupLimit){
            this.per_group_limit = perGroupLimit;
            return this;
        }

        public Builder hasGeoTag(Boolean hasGeoTag){
            this.has_geotag = hasGeoTag;
            return this;
        }

        public Builder hasLink(Boolean hasLink){
            this.has_link = hasLink;
            return this;
        }

        public Builder withProductData(Boolean withProductData){
            this.withProductData = withProductData;
            return this;
        }

        public Builder hasPhoto(Boolean hasPhoto){
            this.has_photo = hasPhoto;
            return this;
        }

        public Builder hasVideo(Boolean hasVideo){
            this.has_video = hasVideo;
            return this;
        }

        public Builder includeComments(Boolean includeComments){
            this.include_comments = includeComments;
            return this;
        }

        @Deprecated
        public Builder explicitPermission(Boolean explicitPermission){
            this.explicit_permission = explicitPermission;
            return this;
        }

        public Builder media(CurationsMedia media){
            this.media = media;
            return this;
        }

        public Builder tags(List<String> tags){
            this.tags = tags;
            return this;
        }

        public Builder location(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            return this;
        }

        public CurationsFeedRequest build(){
            return new CurationsFeedRequest(this);
        }
    }
}
