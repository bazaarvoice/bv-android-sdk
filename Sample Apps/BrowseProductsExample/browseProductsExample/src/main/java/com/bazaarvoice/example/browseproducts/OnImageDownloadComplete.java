package com.bazaarvoice.example.browseproducts;

import android.graphics.Bitmap;

/**
 * OnImageDownloadComplete.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is an interface used for passing callback functions for image downloads.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public interface OnImageDownloadComplete {
	
	/**
	 * Called when an image download has completed
	 * 
	 * @param image the downloaded image
	 */
	public void onFinish(Bitmap image);

}
