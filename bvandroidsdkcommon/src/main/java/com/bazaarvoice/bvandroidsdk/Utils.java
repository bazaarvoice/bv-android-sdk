/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Internal SDK API for common utility methods
 */
class Utils {

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

    static <Key, Value> void mapPutSafe(Map<Key, Object> map, Key key, Value value) {
        if (map != null && key != null && value != null) {
            map.put(key, value);
        }
    }

    static void printAnalytics(List<Map<String, Object>> analyticsBatch) {
        String tag = "BVAnalayticsVerify";

        for (Map<String, Object> event : analyticsBatch) {
            Logger.d(tag, "{");
            for (Map.Entry<String, Object> entry : event.entrySet()) {
                Logger.d(tag, "  " + entry.getKey() + ":" + entry.getValue());
            }
            Logger.d(tag, "}");
        }
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
}
