/**
 * ImageDownloader.java <br>
 * ReviewSubmissionExample<br>
 *
 * <p>
 * This is a simple class that allows for downloading an image from a given URL.
 *
 * <p>
 *
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 *
 * @author Bazaarvoice Engineering
 */

package com.example.bazaarvoice.bv_android_sdk.conversations.browseproducts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * TODO: Description Here
 */
public class ImageDownloader {
	
	/**
	 * Downloads the image at the given URL and returns it as a Bitmap.
	 * 
	 * @param url
	 *            the address of the image
	 * @return the image as a Bitmap
	 */
	public Bitmap download(String url){
		Bitmap bm = null;
	    try {
	        URL u = new URL(url);
	        URLConnection conn = u.openConnection();
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        BufferedInputStream bis = new BufferedInputStream(is);
	        bm = BitmapFactory.decodeStream(bis);
	        bis.close();
	        is.close();
	    } catch (IOException e) {
	        Log.e("Image Downloader", "Error getting bitmap", e);
	    }
	    
	    return bm;
	}

}
