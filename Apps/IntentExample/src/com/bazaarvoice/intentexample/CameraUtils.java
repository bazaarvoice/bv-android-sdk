package com.bazaarvoice.intentexample;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

/**
 * CameraUtils.java <br>
 * ReviewSubmissionExample<br>
 * 
 * A few utilites that ease the process of saving a captured image to file and
 * retrieving it for viewing and upload.
 * 
 * <p>
 * This class aids in importing photos as well sized Bitmaps as per <a
 * href="http://developer.android.com/training/displaying-bitmaps/index.html"
 * >Displaying Bitmaps Efficiently</a>.
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
	 * @param reqHeight
	 * @param reqWidth
	 * @return a correctly oriented Bitmap
	 * @throws IOException
	 */
	public static Bitmap getOrientedBitmap(Uri imageUri, Context context,
			int reqHeight, int reqWidth) throws IOException {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;

		// Decode bitmap with inSampleSize set
		Bitmap photo = BitmapFactory.decodeFile(
				getRealPathFromURI(imageUri, context), options);

		Matrix matrix = new Matrix();
		float rotation = CameraUtils.rotationForImage(context, imageUri);
		if (rotation != 0f) {
			matrix.preRotate(rotation);
		}
		Log.i("Scaled",
				"Height = " + photo.getHeight() + " Width = "
						+ photo.getWidth());
		return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(),
				photo.getHeight(), matrix, true);
	}

	/**
	 * Helper method to reslove a uri into a path.
	 * 
	 * @param contentURI
	 *            a uri path
	 * @return the path as a string
	 */
	public static String getRealPathFromURI(Uri contentURI, Context context) {
		Cursor cursor = context.getContentResolver().query(contentURI, null,
				null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		String path = cursor.getString(idx);
		cursor.close();
		return path;
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
