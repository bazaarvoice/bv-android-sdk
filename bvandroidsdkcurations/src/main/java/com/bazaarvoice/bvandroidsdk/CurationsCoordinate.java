package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 3/31/16.
 */
public class CurationsCoordinate {

    protected Double latitude;
    protected Double longitude;

    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}

    protected CurationsCoordinate(){

    }

    public CurationsCoordinate(double latitude, double longitude){
        this.latitude  =latitude;
        this.longitude = longitude;
    }
}
