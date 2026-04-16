/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * API URL keeper for supported BV products.
 */
class BVRootApiUrls {

    final String shopperMarketingApiRootUrl;
    final String bazaarvoiceApiRootUrl;
    final String bazaarvoiceReviewHighlightUrl;
    final String notificationConfigUrl;
    final String bazaarvoiceMediaApiRootUrl;

    public BVRootApiUrls(String shopperMarketingApiRootUrl, String bazaarvoiceApiRootUrl, String notificationConfigUrl, String bazaarvoiceReviewHighlightUrl, String bazaarvoiceMediaApiRootUrl) {
        this.shopperMarketingApiRootUrl = shopperMarketingApiRootUrl;
        this.bazaarvoiceApiRootUrl = bazaarvoiceApiRootUrl;
        this.notificationConfigUrl = notificationConfigUrl;
        this.bazaarvoiceReviewHighlightUrl = bazaarvoiceReviewHighlightUrl;
        this.bazaarvoiceMediaApiRootUrl = bazaarvoiceMediaApiRootUrl;
    }

    /**
     * Backwards-compatible constructor: if no media URL is provided, default to the standard
     * Bazaarvoice API root URL.
     */
    public BVRootApiUrls(String shopperMarketingApiRootUrl, String bazaarvoiceApiRootUrl, String notificationConfigUrl, String bazaarvoiceReviewHighlightUrl) {
        this(shopperMarketingApiRootUrl, bazaarvoiceApiRootUrl, notificationConfigUrl, bazaarvoiceReviewHighlightUrl, bazaarvoiceApiRootUrl);
    }

    public String getShopperMarketingApiRootUrl() {
        return shopperMarketingApiRootUrl;
    }

    public String getBazaarvoiceApiRootUrl() {
        return bazaarvoiceApiRootUrl;
    }

    public String  getBazaarvoiceReviewHighlightApiUrl(){
       return bazaarvoiceReviewHighlightUrl;
    }

    public String getNotificationConfigUrl() {
        return notificationConfigUrl;
    }

    public String getBazaarvoiceMediaApiRootUrl() {
        return bazaarvoiceMediaApiRootUrl;
    }
}