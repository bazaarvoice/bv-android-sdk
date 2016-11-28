package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/1/16.
 */
public class CurationsMedia {


    protected String mediaType;
    protected int width;
    protected int height;

    public CurationsMedia(String mediaType, int width, int height) {
        this.mediaType = mediaType;
        this.width = width;
        this.height = height;
    }

    public String getMediaType() {
        return mediaType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
