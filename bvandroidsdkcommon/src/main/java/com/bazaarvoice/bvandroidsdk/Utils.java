/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * Internal SDK API for common utility methods
 */
class Utils {

    public static final String THREAD_PREFIX = "BVSDK-";

    static boolean isStagingEnvironment(BazaarEnvironment environment) {
        return environment == BazaarEnvironment.STAGING;
    }

    static String getPackageName(Context applicationContext) {
        return applicationContext.getPackageName();
    }

    static String getVersionName(Context applicationContext) {
        try {
            return String.valueOf(applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    static String getVersionCode(Context applicationContext) {
        try {
            return String.valueOf(applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }

    static <Key, Value> void mapPutSafe(Map<Key, Value> map, Key key, Value value) {
        if (map != null && key != null && value != null) {
            map.put(key, value);
        }
    }

    static URL toUrl(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Helper method when we know it is safe to return 0 in null state.
     *
     * @param integer input value
     * @return if non-null then returns value, otherwise returns 0
     */
    @NonNull static Integer getIntegerSafe(@Nullable Integer integer) {
        return integer == null ? Integer.valueOf(0) : integer;
    }

    /**
     * Helper method when we know it is safe to return 0 in null state.
     *
     * @param floatInput input value
     * @return if non-null then returns value, otherwise returns 0
     */
    @NonNull static Float getFloatSafe(@Nullable Float floatInput) {
        return floatInput == null ? Float.valueOf(0) : floatInput;
    }

    private static final String BV_SHARED_PREFS_FILE_NAME = "_bazaarvoice_shared_prefs_";
    private static final String BV_SHARED_PREFS_KEY_UUID = "_bazaarvoice_shared_prefs_key_uuid_";

    static UUID getUuid(Context applicationContext) {
        UUID uuid;
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(BV_SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        String uuidStr = sharedPreferences.getString(BV_SHARED_PREFS_KEY_UUID, "");

        if (uuidStr.isEmpty()) {
            uuid = UUID.randomUUID();
            sharedPreferences.edit().putString(BV_SHARED_PREFS_KEY_UUID, uuid.toString()).apply();
        } else {
            uuid = UUID.fromString(uuidStr);
        }

        return uuid;
    }

    static void checkNotMain() {
        if (isMain()) {
            throw new BazaarRuntimeException("Method call should not happen from the main thread.");
        }
    }

    static void checkMain() {
        if (!isMain()) {
            throw new BazaarRuntimeException("Method call should happen from the main thread.");
        }
    }

    static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
