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
    final String notificationConfigUrl;

    public BVRootApiUrls(String shopperMarketingApiRootUrl, String bazaarvoiceApiRootUrl, String notificationConfigUrl) {
        this.shopperMarketingApiRootUrl = shopperMarketingApiRootUrl;
        this.bazaarvoiceApiRootUrl = bazaarvoiceApiRootUrl;
        this.notificationConfigUrl = notificationConfigUrl;
    }

    public String getShopperMarketingApiRootUrl() {
        return shopperMarketingApiRootUrl;
    }

    public String getBazaarvoiceApiRootUrl() {
        return bazaarvoiceApiRootUrl;
    }

    public String getNotificationConfigUrl() {
        return notificationConfigUrl;
    }
}