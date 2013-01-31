package com.bazaarvoice.example.reviewsubmission;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
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
	 * Creates a temporary file for storing a photo that will be taken with the
	 * camera.
	 * 
	 * @param context
	 *            the application's context
	 * @return a Uri that points to the file
	 */
	public static Uri getPhotoUri(Context context) {
		File photo;
		try {
			// place where to store camera taken picture
			photo = createTemporaryFile("picture", ".jpg", context);
			photo.delete();
		} catch (Exception e) {
			Log.v(TAG, "Can't create file to take picture!");
			return null;
		}

		Uri mImageUri = Uri.fromFile(photo);
		return mImageUri;
	}

	/**
	 * Creates a temporary file with the specified prefix and extension.
	 * 
	 * @param part
	 *            the prefix for the file
	 * @param ext
	 *            the extension for the file
	 * @param context
	 *            the application's context
	 * @return the created File
	 * @throws Exception
	 */
	private static File createTemporaryFile(String part, String ext,
			Context context) throws Exception {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/bv-temp/");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}

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
		File file = new File(imageUri.getPath());
		FileInputStream optionStream = new FileInputStream(file);
		FileInputStream photoStream = new FileInputStream(file);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(optionStream, null, options);

		// Calculate inSampleSize
		options.inSampleSize = 2;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap photo = BitmapFactory.decodeStream(photoStream, null, options);

		optionStream.close();
		photoStream.close();
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

	/**
	 * Clears any leftover photos in our temporary folder.
	 */
	public static void clearPhotos() {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/bv-temp/");
		if (tempDir.listFiles() != null) {
			for (File photo : tempDir.listFiles()) {
				photo.delete();
			}
		}
	}

}
