/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

import org.json.JSONException;
import org.json.JSONObject;

public class DemoUtils {

    public static final int MAX_IMAGE_WIDTH = 1440;
    public static final int MAX_IMAGE_HEIGHT = 2560;

    public static <ReturnType> ReturnType safeParse(String key, JSONObject jsonObject) {
        return safeParse(key, jsonObject, null);
    }

    public static <ReturnType> ReturnType safeParse(String key, JSONObject jsonObject, ReturnType defaultValue) {
        ReturnType value = null;
        try {
            value = (ReturnType) jsonObject.get(key);
        } catch (JSONException e) {
        }
        return value != null ? value : defaultValue;
    }

    public static String safeParseString(String key, JSONObject jsonObject) {
        return safeParseString(key, jsonObject, null);
    }

    public static String safeParseString(String key, JSONObject jsonObject, String defaultValue) {
        String value = null;
        try {
            value = jsonObject.getString(key);
        } catch (JSONException e) {
        }
        return value != null ? value : defaultValue;
    }

    public static Point getScreenDimensions(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

}
