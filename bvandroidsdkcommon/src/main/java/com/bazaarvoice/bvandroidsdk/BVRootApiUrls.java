/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * TODO: Describe file here.
 */
class BVRootApiUrls {

    final String shopperMarketingApiRootUrl;
    final String curationsDisplayApiRootUrl;
    final String curationsPostApiRootUrl;
    final String conversationsApiRootUrl;

    public BVRootApiUrls(String shopperMarketingApiRootUrl, String curationsDisplayApiRootUrl, String curationsPostApiRootUrl, String conversationsApiRootUrl) {
        this.shopperMarketingApiRootUrl = shopperMarketingApiRootUrl;
        this.curationsDisplayApiRootUrl = curationsDisplayApiRootUrl;
        this.curationsPostApiRootUrl = curationsPostApiRootUrl;
        this.conversationsApiRootUrl = conversationsApiRootUrl;
    }

    public String getShopperMarketingApiRootUrl() {
        return shopperMarketingApiRootUrl;
    }

    public String getCurationsDisplayApiRootUrl() {
        return curationsDisplayApiRootUrl;
    }

    public String getCurationsPostApiRootUrl() {
        return curationsPostApiRootUrl;
    }

    public String getConversationsApiRootUrl() {
        return conversationsApiRootUrl;
    }
}