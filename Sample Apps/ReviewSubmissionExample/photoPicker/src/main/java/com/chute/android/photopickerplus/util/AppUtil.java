/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import com.chute.sdk.collections.GCAccountMediaCollection;
import com.chute.sdk.model.GCAccountMediaModel;
import com.chute.sdk.utils.GCUtils;

public class AppUtil {

    @SuppressWarnings("unused")
    private static final String TAG = AppUtil.class.getSimpleName();

    private static String SDCARD_FOLDER_CACHE = Environment.getExternalStorageDirectory()
	    + "/Android/data/%s/files/";

    public static String getThumbSmallUrl(String urlNormal) {
	return GCUtils.getCustomSizePhotoURL(urlNormal, 100, 100);
    }

    public static File getTempFile(Context context) {
	final File path = getAppCacheDir(context);
	if (!path.exists()) {
	    path.mkdirs();
	}
	File f = new File(path, "temp_image.jpg");
	if (f.exists() == false) {
	    try {
		f.createNewFile();
	    } catch (IOException e) {
		Log.w(TAG, e.getMessage(), e);
	    }
	}
	return f;
    }

    public static File getAppCacheDir(Context context) {
	return new File(String.format(SDCARD_FOLDER_CACHE, context.getPackageName()));
    }

    public static boolean hasImageCaptureBug() {
	// list of known devices that have the bug
	ArrayList<String> devices = new ArrayList<String>();
	devices.add("android-devphone1/dream_devphone/dream");
	devices.add("generic/sdk/generic");
	devices.add("vodafone/vfpioneer/sapphire");
	devices.add("tmobile/kila/dream");
	devices.add("verizon/voles/sholes");
	devices.add("google_ion/google_ion/sapphire");
	devices.add("SEMC/X10i_1232-9897/X10i");

	return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
		+ android.os.Build.DEVICE);
    }

    public static String getPath(Context context, Uri uri) throws NullPointerException {
	final String[] projection = { MediaColumns.DATA };
	final Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
	final int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	cursor.moveToFirst();
	return cursor.getString(column_index);
    }
    
    public final static String asUpperCaseFirstChar(final String target) {

        if ((target == null) || (target.length() == 0)) {
            return target; 
        }
        return Character.toUpperCase(target.charAt(0))
                + (target.length() > 1 ? target.substring(1) : "");
    }
    
    public static GCAccountMediaCollection getPhotoCollection(ArrayList<String> paths) {
		final GCAccountMediaCollection collection = new GCAccountMediaCollection();
		for (String path : paths) {
			final GCAccountMediaModel model = new GCAccountMediaModel();
			path = Uri.fromFile(new File(path)).toString();
			model.setLargeUrl(path);
			model.setThumbUrl(path);
			model.setUrl(path);
			collection.add(model);
		}
		return collection;
	}
    
    public static GCAccountMediaModel getMediaModel(String path) {
		final GCAccountMediaModel model = new GCAccountMediaModel();
		path = Uri.fromFile(new File(path)).toString();
		model.setLargeUrl(path);
		model.setThumbUrl(path);
		model.setUrl(path);
		return model;
	}
    
    public static String convertMediaUriToPath(Context context, Uri uri) {
    	String[] proj = { MediaStore.Images.Media.DATA };
    	Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
    	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	cursor.moveToFirst();
    	String path = cursor.getString(column_index);
    	cursor.close();
    	return path;
        }
}
