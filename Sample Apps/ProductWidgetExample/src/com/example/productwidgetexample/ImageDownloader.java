package com.example.productwidgetexample;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * ImageDownloader.java <br>
 * ProductWidgetExample<br>
 * 
 * <p>
 * This is a simple class that allows for downloading an image from a given URL.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
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
