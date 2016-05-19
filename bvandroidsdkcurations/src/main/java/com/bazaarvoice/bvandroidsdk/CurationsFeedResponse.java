package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bazaarvoice on 3/31/16.
 */
public class CurationsFeedResponse {

    protected Integer code;
    protected List<CurationsUpdateContainer> updates;
    protected Map<String, CurationsProduct> productData;
    protected Map<String, String> options = new HashMap<>();

    private List<CurationsFeedItem> rawUpdates;

    public List<CurationsFeedItem> getUpdates(){

        if (rawUpdates == null) {
            rawUpdates = new ArrayList<>();

            if (updates != null) {
                for (CurationsUpdateContainer curationsUpdateContainer : updates) {

                    if (options.containsKey("externalId")){
                        curationsUpdateContainer.data.externalIdInQuery = options.get("externalId");
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
