/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.List;

/**
 * A Store object is equivalent to a Conversations Product but contains additional Geo-Location attributes
 * such as address, city, latitude, longitude.
 */
public class Store extends BaseProduct<StoreLocationAttributes> {

    public enum StoreAttributeType {
        COUNTRY("Country"),
        CITY("City"),
        STATE("State"),
        ADDRESS("Address"),
        PHONE("Phone"),
        POSTALCODE("PostalCode"),
        LATITUDE("Latitude"),
        LONGITUDE("Longitude"),
        ;

        String val;
        StoreAttributeType(String val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val;
        }
    }

    /**
     * Get a location attribute on a Store object by supplying one of the StoreAttributeType enumerations.
     * May return null if the attribute is not defined in the original store feed.
     * @param storeAttributeType
     * @return String value of the attribute, or null if not defined.
     */
    public String getLocationAttribute(StoreAttributeType storeAttributeType) {
        String value = null;

        if (getAttributes() != null) {
            StoreLocationAttributes attribs =  getAttributes().get(storeAttributeType.toString());
            if (attribs != null){
                value = attribs.getElements().get(0).getValue();
            }
        }
        return value;
    }

    public List<StoreReview> getStoreReviews() {
        return getReviewList();
    }
}