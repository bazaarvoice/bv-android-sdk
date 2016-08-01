/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

abstract class ConversationsRequest {
    static final String kAPI_VERSION = "apiversion";
    static final String kPASS_KEY = "passkey";
    static final String kAPP_ID = "appId";
    static final String kAPP_VERSION = "appVersion";
    static final String kBUILD_NUM = "buildNumber";
    static final String kSDK_VERSION = "bvAndroidSdkVersion";

    static final String API_VERSION = "5.4";
    abstract String getEndPoint();

    /*
        Internal helper method. To be implemented in all classes inheriting from this.
        This method will be used mainly by BVConversations client to shortcut the network
        request if the request is in a known fail state. For example limit being to high or low.
        Return Throwable - If Request is misconfigured return Throwable with detailed message as to why the request will fail
        else null to indicate no error.
     */
    abstract BazaarException getError();

}