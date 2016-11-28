package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 3/31/16.
 */
public class CurationsLink {

    protected String domain;
    protected String display_url;
    protected String short_url;
    protected String favicon;
    protected String url;


    protected CurationsLink(){

    }

    public CurationsLink(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getDomain() {
        return domain;
    }

    public String getDisplayUrl() {
        return display_url;
    }

    public String getShortUrl() {
        return short_url;
    }

    public String getFavicon() {
        return favicon;
    }

}
