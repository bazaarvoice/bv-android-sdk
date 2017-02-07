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

import java.util.List;

/**
 * Social media item referencing a Bazaarvoice Product Id
 * with metadata including photos, video, links to the
 * original source of the media, etc.
 */
public class CurationsFeedItem {
    protected List<String> groups;
    public  List<String> getGroups(){return groups;}

    protected List<String> featured_groups;
    public  List<String> getFeaturedGroups(){return  featured_groups;}

    protected List<String> tags;
    public  List<String>  getTags(){return tags;}

    protected List<CurationsPhoto> photos;
    public  List<CurationsPhoto> getPhotos(){return photos;}

    protected List<CurationsVideo> videos;
    public  List<CurationsVideo> getVideos(){return videos;}

    protected List<CurationsLink> links;
    public  List<CurationsLink> getLinks(){return  links;}

    protected Long timestamp;
    public Long getTimestamp(){return timestamp;}

    protected Long id;
    public Long getId(){return id;}

    protected String sourceClient;
    public String getSourceClient(){return sourceClient;}

    protected String token;
    protected String language;
    protected String classification;
    protected String channel;
    protected String text;
    protected String permalink;
    protected String teaser;
    protected String rating;
    protected String place;
    protected String _platform;
    protected String explicit_permission_status;
    protected String product_id;

    public String getToken(){return token;}
    public String getLanguage(){return language;}
    public String getClassification(){return classification;}
    public String getChannel(){return channel;}
    public String getText(){return text;}
    public String getPermalink(){return  permalink;}
    public String getTeaser(){return teaser;}
    public String getRating(){return rating;}
    public String getPlace(){return place;}
    public String getPlatform(){return _platform;}
    public String getExplicitPermissionStatus(){return explicit_permission_status;}
    public String getProductId(){return product_id;}

    protected Integer praises;
    public Integer getPraises(){return praises;}

    protected CurationsAuthor author;
    public CurationsAuthor getAuthor(){return author;}
    protected CurationsAuthor reply_to;
    public CurationsAuthor getReplyTo(){return reply_to;}

    @SerializedName("coordinates")
    protected CurationsCoordinate coordinate;
    public CurationsCoordinate getCoordinate(){return coordinate;}

    protected List<CurationsProduct> products;
    public List<CurationsProduct> getProducts(){return products;}

    protected void setProducts(List<CurationsProduct> products){
        this.products = products;
    }

    boolean impressed;
    String externalIdInQuery;
}
