package com.bazaarvoice.intentexample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.widget.ImageView;

/**
 * CameraUtils.java <br>
 * ReviewSubmissionExample<br>
 * 
 * A few utilites that ease the process of saving a captured image to file and
 * retrieving it for viewing and upload.
 * 
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class CameraUtils {

	private final static String TAG = "CameraUtils";

	/**
	 * Pulls a bitmap out of the given Uri and does any necessary rotations to
	 * make it suitable for display.
	 * 
	 * @param imageUri
	 *            the Uri pointing to the image
	 * @param context
	 *            the applications's context
	 * @return a correctly oriented Bitmap
	 * @throws IOException
	 */
	public static Bitmap getOrientedBitmap(Uri imageUri, Context context)
			throws IOException {
		Bitmap photo = MediaStore.Images.Media.getBitmap(
				context.getContentResolver(), imageUri);
		Matrix matrix = new Matrix();
		float rotation = CameraUtils.rotationForImage(context, imageUri);
		if (rotation != 0f) {
			matrix.preRotate(rotation);
		}
		return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(),
				photo.getHeight(), matrix, true);
	}

	/**
	 * Calculates the amount of rotation needed for the image to look upright.
	 * 
	 * @param context
	 *            the application's context
	 * @param uri
	 *            the Uri pointing to the file
	 * @return the needed rotation as a <code>float</code>
	 */
	private static float rotationForImage(Context context, Uri uri) {
		if ("content".equals(uri.getScheme())) {
			String[] projection = { Images.ImageColumns.ORIENTATION };
			Cursor c = context.getContentResolver().query(uri, projection,
					null, null, null);
			if (c.moveToFirst()) {
				return c.getInt(0);
			}
		} else if ("file".equals(uri.getScheme())) {
			try {
				ExifInterface exif = new ExifInterface(uri.getPath());
				int rotation = (int) exifOrientationToDegrees(exif
						.getAttributeInt(ExifInterface.TAG_ORIENTATION,
								ExifInterface.ORIENTATION_NORMAL));
				return rotation;
			} catch (IOException e) {
				Log.e(TAG, "Error checking exif", e);
			}
		}
		return 0f;
	}

	/**
	 * Converts the given Exif Orientation to a degree for rotation.
	 * 
	 * @param exifOrientation
	 *            the ExifInterface code for the orientation
	 * @return the rotation as a <code>float</code>
	 */
	private static float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}
	
}
