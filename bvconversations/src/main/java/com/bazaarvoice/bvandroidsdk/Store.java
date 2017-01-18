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