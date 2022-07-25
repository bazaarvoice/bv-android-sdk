package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class BVAnalyticsUtils {
  private static final String TAG = "BVAnalyticsUtils";
  private static DecimalFormat twoPlaceFormat = new DecimalFormat("0.00");
  static final String THREAD_PREFIX = "BVAnalytics-";

  /**
   * These are the approved non-pii params
   */
  static final List<String> NON_PII_PARAMS = Arrays.asList(
      "orderId", "affiliation", "total", "tax", "shipping", "city",
      "state", "currency", "items", "locale", "type", "label",
      "value", "partnerSource", "TestCase", "TestSession", "dc", "ref", "deploymentZone", "discount", "country", "proxy");

  static void warnShouldNotBeEmpty(String name, Object obj) {
    if (obj == null) {
      Log.e("BVAnalytics", name + " must not be empty");
    }
  }

  private static final String BV_SHARED_PREFS_FILE_NAME = "_bazaarvoice_shared_prefs_";
  private static final String BV_SHARED_PREFS_KEY_UUID = "_bazaarvoice_shared_prefs_key_uuid_";

  static UUID getUuid(Context applicationContext) {
    if (applicationContext == null) {
      return UUID.randomUUID();
    }
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

  static String generateLoadId() {
    final int length = 20;
    StringBuilder buf = new StringBuilder(length);
    while (buf.length() < length) {
      buf.append(Integer.toHexString((int) (Math.random() * 16)));
    }
    return buf.toString();
  }

  /**
   * @param map JsonObject to add PII params to
   * @param params All key-value pairs that a user provides as extra conversion info
   * @return
   */
  static boolean addPiiOnly(@NonNull Map<String, Object> map, @NonNull Map<String, Object> params) {
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      if (isPiiParam(entry.getKey())) {
        return true;
      }
    }
    return false;
  }

  static boolean isPiiParam(String param) {
    return !NON_PII_PARAMS.contains(param);
  }

  public static Map<String, Object> nonPiiOnly(Map<String, Object> params) {
    Map<String, Object> nonPiiParams = new HashMap<>();
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      if (!isPiiParam(entry.getKey())) {
        mapPutSafe(nonPiiParams, entry.getKey(), entry.getValue());
      }
    }
    return nonPiiParams;
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

  static void mapPutSafe(Map<String, Object> map, String key, Object value) {
    if (key == null || key.isEmpty() || value == null || value.toString().isEmpty()) {
      return;
    }
    map.put(key, value.toString());
  }

  static void mapPutSafe(Map<String, Object> map, String key, List<Map<String, Object>> value) {
    if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
      return;
    }
    map.put(key, value);
  }

  static void mapPutSafe(Map<String, Object> map, String key, String value) {
    if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
      return;
    }
    map.put(key, value);
  }

  static void mapPutSafe(Map<String, Object> map, String key, int value) {
    if (key == null || key.isEmpty()) {
      return;
    }
    map.put(key, String.valueOf(value));
  }

  static void mapPutSafe(Map<String, Object> map, String key, boolean value) {
    if (key == null || key.isEmpty()) {
      return;
    }
    map.put(key, String.valueOf(value));
  }

  static void mapPutSafe(Map<String, Object> map, String key, double value) {
    if (key == null || key.isEmpty()) {
      return;
    }
    map.put(key, String.valueOf(value));
  }

  static void mapPutSafe(Map<String, Object> map, String key, long value) {
    if (key == null || key.isEmpty()) {
      return;
    }
    map.put(key, String.valueOf(value));
  }

  static void mapPutAllSafe(Map<String, Object> map, Map<String, Object> value) {
    if (value == null || value.isEmpty()) {
      return;
    }
    for (Map.Entry<String, Object> entry : value.entrySet()) {
      if (entry.getValue() instanceof List) {
        List entryValueList = (List) entry.getValue();
        mapPutSafe(map, entry.getKey(), entryValueList);
      } else {
        mapPutSafe(map, entry.getKey(), entry.getValue());
      }
    }
  }
}
