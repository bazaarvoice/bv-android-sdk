/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Base64;

import com.bazaarvoice.bvandroidsdk.BazaarEnvironment;
import com.bazaarvoice.bvandroidsdk.BazaarRuntimeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Junk drawer of utility methods.
 */
public final class Utils {

    public static final String THREAD_PREFIX = "BVSDK-";

    public static boolean isStagingEnvironment(BazaarEnvironment environment) {
        return environment == BazaarEnvironment.STAGING;
    }

    public static String getPackageName(Context applicationContext) {
        return applicationContext.getPackageName();
    }

    public static String getVersionName(Context applicationContext) {
        try {
            return String.valueOf(applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getVersionCode(Context applicationContext) {
        try {
            return String.valueOf(applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static <Key, Value> void mapPutSafe(Map<Key, Value> map, Key key, Value value) {
        if (map != null && key != null && value != null) {
            map.put(key, value);
        }
    }

    public static URL toUrl(String urlStr) {
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
    @NonNull
    public static Integer getIntegerSafe(@Nullable Integer integer) {
        return integer == null ? Integer.valueOf(0) : integer;
    }

    /**
     * Helper method when we know it is safe to return 0 in null state.
     *
     * @param floatInput input value
     * @return if non-null then returns value, otherwise returns 0
     */
    @NonNull
    public static Float getFloatSafe(@Nullable Float floatInput) {
        return floatInput == null ? Float.valueOf(0) : floatInput;
    }

    private static final String BV_SHARED_PREFS_FILE_NAME = "_bazaarvoice_shared_prefs_";
    private static final String BV_SHARED_PREFS_KEY_UUID = "_bazaarvoice_shared_prefs_key_uuid_";

    public static UUID getUuid(Context applicationContext) {
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

    public static void checkNotMain() {
        if (isMain()) {
            throw new BazaarRuntimeException("Method call should not happen from the main thread.");
        }
    }

    public static void checkMain() {
        if (!isMain()) {
            throw new BazaarRuntimeException("Method call should happen from the main thread.");
        }
    }

    public static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFromBytes(byte[] bytes, int reqWidth, int reqHeight) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String toBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }
}
