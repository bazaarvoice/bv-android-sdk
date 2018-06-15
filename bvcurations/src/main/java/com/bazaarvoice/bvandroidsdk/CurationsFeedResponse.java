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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Response for a list of {@link CurationsFeedItem}s
 */
public class CurationsFeedResponse {

    protected Integer code;
    protected List<CurationsUpdateContainer> updates;
    protected Map<String, CurationsProduct> productData;
    protected Map<String, Object> options = new HashMap<>();

    private List<CurationsFeedItem> rawUpdates;

    public List<CurationsFeedItem> getUpdates(){

        if (rawUpdates == null) {
            rawUpdates = new ArrayList<>();

            if (updates != null) {
                for (CurationsUpdateContainer curationsUpdateContainer : updates) {

                    if (options.containsKey("externalId") && options.get("externalId") instanceof String){
                        curationsUpdateContainer.data.externalIdInQuery = (String) options.get("externalId");
                    }
                    List<CurationsProduct> products = new ArrayList<>();
                    if (curationsUpdateContainer.data.tags != null && productData != null) {
                        for (String tag : curationsUpdateContainer.data.tags) {
                            if (productData.containsKey(tag)) {
                                products.add(productData.get(tag));
                            }
                        }
                    }
                    curationsUpdateContainer.data.setProducts(products);
                    rawUpdates.add(curationsUpdateContainer.data);
                }
            }
        }
        return rawUpdates;
    }
}
