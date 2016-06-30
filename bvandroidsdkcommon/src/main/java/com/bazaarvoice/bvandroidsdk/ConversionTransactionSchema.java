package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.Utils.mapPutSafe;

/**
 * Created by Bazaarvoice on 3/29/16.
 */

class ConversionTransactionSchema extends BvAnalyticsSchema {

    private static final String eventClass = "Conversion";
    private static final String eventType = "Transaction";
    private static final String source = "native-mobile-sdk";
    private static final String orderID = "orderId";
    private static final String total = "total";
    private static final String items = "items";
    private static final String loadId = "loadId";
    private static final String hadPII = "hadPII";
    private static final String tax = "tax";
    private static final String city = "city";
    private static final String state = "state";
    private static final String shipping = "shipping";
    private static final String country = "country";
    private static final String currency = "currency";

    private static final String itemSku = "sku";
    private static final String itemName = "name";
    private static final String itemImage = "imageUrl";
    private static final String itemCategory = "category";
    private static final String itemQuantity = "quantity";
    private static final String itemPrice = "price";

    private Map<String,Object> pIIEvent;

    ConversionTransactionSchema(MagpieMobileAppPartialSchema magpieMobileAppPartialSchema, Transaction transaction){
        super(eventClass, eventType, source);
        addPartialSchema(magpieMobileAppPartialSchema);
        addKeyVal(loadId, ConversionUtils.generateLoadId());
        addKeyVal(total, transaction.getTotal());
        addKeyVal(tax, transaction.getTax());
        addKeyVal(state, transaction.getState());
        addKeyVal(shipping, transaction.getShipping());
        addKeyVal(country, transaction.getCountry());
        addKeyVal(city, transaction.getCity());
        addKeyVal(currency, transaction.getCurrency());

        List<Map<String,Object>> serializedItems = new ArrayList<>();

        for (TransactionItem item : transaction.getItems()){
            
            Map<String, Object> itemParams = new HashMap<>();
            mapPutSafe(itemParams, itemSku, item.getSku());
            mapPutSafe(itemParams, itemName, item.getName());
            mapPutSafe(itemParams, itemImage, item.getImageUrl());
            mapPutSafe(itemParams, itemCategory, item.getCategory());
            mapPutSafe(itemParams, itemQuantity, item.getQuantity());
            mapPutSafe(itemParams, itemPrice, item.getPrice());

            serializedItems.add(itemParams);
        }
        addKeyVal(items, serializedItems);
        addKeyVal(orderID, transaction.getOrderId());

        boolean hadPII = false;

        if (transaction.getOtherParams() != null){
            Map<String, Object> nonPIIParams = ConversionUtils.getNonPIIParams(transaction.getOtherParams());
            for (String key : nonPIIParams.keySet()){
                addKeyVal(key, nonPIIParams.get(key));
            }
            hadPII = nonPIIParams.size() != transaction.getOtherParams().size();
        }

        if (hadPII){
            addKeyVal(ConversionTransactionSchema.hadPII, "true");
            this.pIIEvent = ConversionUtils.generatePIIEvent(this, transaction.getOtherParams());
            setAllowAdId(false);
        }
    }

    Map<String, Object> getPIIEvent(){
        return pIIEvent;
    }

}
