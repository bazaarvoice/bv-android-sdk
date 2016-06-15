/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DemoUtils {

    private static DemoUtils instance;

    public static final int MAX_IMAGE_WIDTH = 1440;
    public static final int MAX_IMAGE_HEIGHT = 2560;

    private Picasso picasso;
    private Context applicationContext;

    private DemoUtils(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    public static DemoUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DemoUtils(context);
        }
        return instance;
    }

    public Picasso picassoThumbnailLoader() {
        if (picasso == null) {
            picasso = new Picasso.Builder(applicationContext)
                    .defaultBitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        return picasso;
    }

    public static <ReturnType> ReturnType safeParse(String key, JSONObject jsonObject) {
        return safeParse(key, jsonObject, null);
    }

    public static <ReturnType> ReturnType safeParse(String key, JSONObject jsonObject, ReturnType defaultValue) {
        ReturnType value = null;
        try {
            value = (ReturnType) jsonObject.get(key);
        } catch (JSONException e) {}
        return value != null ? value : defaultValue;
    }

    public static String safeParseString(String key, JSONObject jsonObject) {
        return safeParseString(key, jsonObject, null);
    }

    public static String safeParseString(String key, JSONObject jsonObject, String defaultValue) {
        String value = null;
        try {
            value = jsonObject.getString(key);
        } catch (JSONException e) {}
        return value != null ? value : defaultValue;
    }


}
