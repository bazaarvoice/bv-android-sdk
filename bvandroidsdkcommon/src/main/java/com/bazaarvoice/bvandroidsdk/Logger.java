/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.util.Log;

/**
 * Internal SDK API for logging that can be toggled upon SDK creation
 */
class Logger{

    static BVLogLevel bvLogLevel;

    public static void i(String tag, String message){
        if(bvLogLevel.greaterOrEqualPriorityThan(BVLogLevel.INFO)){
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message){
        if(bvLogLevel.greaterOrEqualPriorityThan(BVLogLevel.VERBOSE)){
            Log.d(tag, message);
        }
    }

    public static void v(String tag, String message){
        if(bvLogLevel.greaterOrEqualPriorityThan(BVLogLevel.VERBOSE)){
            Log.v(tag, message);
        }
    }

    public static void w(String tag, String message){
        if(bvLogLevel.greaterOrEqualPriorityThan(BVLogLevel.WARNING)){
            Log.w(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable tr) {
        if(bvLogLevel.greaterOrEqualPriorityThan(BVLogLevel.WARNING)){
            Log.w(tag, message, tr);
        }
    }

    /// Log errors regardless of STAGING
    public static void e(String tag, String message){
        Log.e(tag, message);
    }

    public static void e(String tag, String message, Throwable tr){
        Log.e(tag, message, tr);
    }

    public static void setLogLevel(BVLogLevel bvLogLevel) {
        Logger.bvLogLevel = bvLogLevel;
    }
}
