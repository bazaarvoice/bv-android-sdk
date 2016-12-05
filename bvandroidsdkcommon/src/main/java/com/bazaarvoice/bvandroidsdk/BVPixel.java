/**
 * Copyright 2017 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * Class used to send Transaction and Non-commerce Conversion Events
 */
public class BVPixel {

    /**
     * Event when a transaction occurs
     *
     * @param transaction
     */
    public static void sendConversionTransactionEvent(Transaction transaction) {
        if (transaction.getItems() == null || transaction.getItems().size() == 0){
            Logger.w("BVSDK", "Could not track Transaction. Transaction items are required");
            return;
        }

        BVSDK.getInstance().sendConversionTransactionEvent(transaction);
    }

    /**
     * Event when a non-commerce conversion occurs
     *
     * @param conversion
     */
    public static void sendNonCommerceConversionEvent(Conversion conversion) {
        BVSDK.getInstance().sendNonCommerceConversionEvent(conversion);
    }
}