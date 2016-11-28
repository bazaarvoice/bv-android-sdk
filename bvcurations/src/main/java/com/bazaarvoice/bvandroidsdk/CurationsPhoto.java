package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 3/31/16.
 */
public class CurationsPhoto {

    protected CurationsPhoto(){

    }

    public CurationsPhoto(String remoteUrl){
        this.remote_url = remoteUrl;
    }

    protected Integer id;
    protected String origin;
    protected String permalink;
    protected String token;
    protected String display_url;
    protected String image_service_url;
    protected String role;
    protected String url;
    protected String local_url;

    protected String remote_url;

    public Integer getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getToken() {
        return token;
    }

    public String getDisplayUrl() {
        return display_url;
    }

    public String getImageServiceUrl() {
        return image_service_url;
    }

    public String getRole() {
        return role;
    }

    public String getUrl() {
        return url;
    }

    public String getLocalUrl() {
        return local_url;
    }

    public String getRemote_url() {
        return remote_url;
    }

}
