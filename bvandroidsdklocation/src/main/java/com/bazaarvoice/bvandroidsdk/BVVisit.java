/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Class containing information about a users current retail location visit
 */
public class BVVisit {
    private final String name;
    private final String address;
    private final String city;
    private final String state;
    private final String zip;
    private final String storeId;

    public BVVisit(String name, String address, String city, String state, String zip, String storeId) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getStoreId() {
        return storeId;
    }
}