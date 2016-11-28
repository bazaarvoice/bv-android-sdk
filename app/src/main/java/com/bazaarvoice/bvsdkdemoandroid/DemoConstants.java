/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid;

import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;

import java.util.ArrayList;
import java.util.Arrays;

public class DemoConstants {

    /******************* MAIN CONFIG OPTIONS **********************/
    // Bazaarvoice client id
    public static final String BV_CLIENT_ID = "REPLACE_ME";

    public static final String PASSKEY_SHOPPER_AD = "REPLACE_ME";
    public static final String PASSKEY_CONVERSATIONS = "REPLACE_ME";
    public static final String PASSKEY_CONVERSATIONS_STORES = "REPLACE_ME";
    public static final String PASSKEY_CURATIONS = "REPLACE_ME";
    public static final String PASSKEY_LOCATION = "REPLACE_ME";
    public static final String PASSKEY_PIN = "REPLACE_ME";

    // Bazaarvoice environments, NOTE! Turn to PRODUCTION for production apps
    public static final BazaarEnvironment ENVIRONMENT = BazaarEnvironment.PRODUCTION;
    /**************************************************************/


    /******************* OTHER CONFIG OPTIONS *********************/
    // General SDK Log Level
    public static final BVLogLevel LOG_LEVEL = BVLogLevel.VERBOSE;

    // Ad/Recommendations Other Config Options
    // User auth string pre-populated with a small profile
    // interested in men's and women's apparel -- for testing and demonstration purposes
    public static final String BV_USER_AUTH_STRING = "0ce436b29697d6bc74f30f724b9b0bb6646174653d31323334267573657269643d5265636f6d6d656e646174696f6e7353646b54657374";

    // Ad Other Config Options
    public static final String DFP_TEST_DEVICE_ID = "REPLACE_ME";
    public static final String BANNER_AD_UNIT_ID = "/6449/example/banner";
    public static final String INTERSTITIAL_AD_UNIT_ID = "/6449/example/interstitial";
    public static final String NATIVE_CONTENT_AD_UNIT_ID = "/6449/example/native";

    // Curations Other Config Options
    public static final ArrayList<String> CURATIONS_GROUPS = new ArrayList<>(Arrays.asList("__all__"));

    // Production Environment Other Config Options
    public static final boolean PREVENT_ANALYTICS_IN_PROD = false;
    /**************************************************************/

    public static boolean isSet(String constant) {
        return constant != null && !constant.equals("REPLACE_ME");
    }

}
