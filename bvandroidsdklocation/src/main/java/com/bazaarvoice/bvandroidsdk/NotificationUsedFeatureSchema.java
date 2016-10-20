package com.bazaarvoice.bvandroidsdk;

class NotificationUsedFeatureSchema extends UsedFeatureCanonicalSchema {

    private static final String source = "native-mobile-sdk";
    private static final String bvProduct = "RatingsAndReviews";
    private static final String name = "PushNotification";

    public NotificationUsedFeatureSchema(boolean inView, String productId, String detail1, String detail2, MagpieMobileAppPartialSchema magpieMobileAppPartialSchema) {
        super(source, name, bvProduct);

        if (inView){
            // overwrites the super name value, if inView is set.
            addKeyVal("name", "InView");
        }

        if (productId != null){
            addProductId(productId);
        }

        if (detail1 != null){
            addDetail1(detail1);
        }

        if (detail2 != null){
            addDetail2(detail2);
        }

        addPartialSchema(magpieMobileAppPartialSchema);
    }

}