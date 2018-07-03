package com.bazaarvoice.bvandroidsdk;

@Deprecated
public enum PlaceType {

    Geofence("geofence");

    private final String value;

    PlaceType(String value){
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
