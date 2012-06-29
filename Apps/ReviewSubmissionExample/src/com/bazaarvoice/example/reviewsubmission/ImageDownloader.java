package com.bazaarvoice.example.reviewsubmission;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {
	
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
