/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid;

import com.bazaarvoice.bvandroidsdk.Action;
import com.bazaarvoice.bvandroidsdk.BVLogLevel;
import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;

import java.util.ArrayList;
import java.util.Arrays;

public class DemoConstants {

    /******************* MAIN CONFIG OPTIONS **********************/
    // Bazaarvoice environments, NOTE! Turn to PRODUCTION for production apps
    public static final BazaarEnvironment ENVIRONMENT = BazaarEnvironment.STAGING;
    /**************************************************************/


    /******************* OTHER CONFIG OPTIONS *********************/
    // General SDK Log Level
    public static final BVLogLevel LOG_LEVEL = BVLogLevel.VERBOSE;

    // Ad/Recommendations Other Config Options
    // User auth string pre-populated with a small profile
    // interested in men's and women's apparel -- for testing and demonstration purposes
    public static final String BV_USER_AUTH_STRING = "auth_token";

    // Ad Other Config Options
    public static final String DFP_TEST_DEVICE_ID = "REPLACE_ME";
    public static final String BANNER_AD_UNIT_ID = "/6449/example/banner";
    public static final String INTERSTITIAL_AD_UNIT_ID = "/6449/example/interstitial";
    public static final String NATIVE_CONTENT_AD_UNIT_ID = "/6449/example/native";

    // Curations Other Config Options
    public static final ArrayList<String> CURATIONS_GROUPS = new ArrayList<>(Arrays.asList("__all__"));

    public static ArrayList<String> TEST_BULK_PRODUCT_IDS = new ArrayList<>(Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6"));
    public static Action SUBMIT_ACTION = Action.Preview;
    /**************************************************************/

    public static boolean isSet(String constant) {
        return constant != null && !constant.equals("REPLACE_ME");
    }

}
