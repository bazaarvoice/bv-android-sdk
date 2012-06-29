package com.bazaarvoice.example.reviewsubmission;

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

public class CameraUtils{
	
	private final static String TAG = "CameraUtils";

	public static Uri getPhotoUri(Context context){
	    File photo;
	    try
	    {
	        // place where to store camera taken picture
	        photo = createTemporaryFile("picture", ".jpg", context);
	        photo.delete();
	    }
	    catch(Exception e)
	    {
	        Log.v(TAG, "Can't create file to take picture!");
	        return null;
	    }
	    
	    Uri mImageUri = Uri.fromFile(photo);
	    return mImageUri;
	}
	
	private static File createTemporaryFile(String part, String ext, Context context) throws Exception {
		File tempDir = Environment.getExternalStorageDirectory();
	    tempDir = new File(tempDir.getAbsolutePath() + "/temp/");
	    if(!tempDir.exists()){
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}
	
	public static Bitmap getOrientedBitmap(Uri imageUri, Context context) throws IOException{
		File file = new File(imageUri.getPath());
    	FileInputStream stream = new FileInputStream(file);
		Bitmap photo = BitmapFactory.decodeStream(stream);
		stream.close();
		Matrix matrix = new Matrix();
		float rotation = CameraUtils.rotationForImage(context, imageUri);
		if (rotation != 0f) {
		     matrix.preRotate(rotation);
		}
		Log.i(TAG, "Rotation = " + rotation);
		return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
	}
	
	public static float rotationForImage(Context context, Uri uri){
        if ("content".equals(uri.getScheme())){
	        String[] projection = { Images.ImageColumns.ORIENTATION };
	        Cursor c = context.getContentResolver().query(
	                uri, projection, null, null, null);
	        if (c.moveToFirst()) {
	            return c.getInt(0);
	        }
        } 
        else if ("file".equals(uri.getScheme())) {
	        try {
	            ExifInterface exif = new ExifInterface(uri.getPath());
	            int rotation = (int)exifOrientationToDegrees(
	                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	                            ExifInterface.ORIENTATION_NORMAL));
	            Log.i(TAG, "Rotation = " + rotation);
	            return rotation;
	        } catch (IOException e) {
	            Log.e(TAG, "Error checking exif", e);
	        }
        }
        Log.i(TAG, "Rotation = 0.0");
        return 0f;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
	    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
	        return 90;
	    } 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
	        return 180;
	    } 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
	        return 270;
	    }
	    return 0;
    }
	
	public static void clearPhotos(){
		File tempDir = Environment.getExternalStorageDirectory();
	    tempDir = new File(tempDir.getAbsolutePath() + "/temp/");
	    if(tempDir != null){
		    for (File photo : tempDir.listFiles()){
		    	photo.delete();
		    }
	    }
	}

}
