package com.bazaarvoice.example.reviewsubmission;

import android.graphics.Bitmap;

public interface OnImageDownloadComplete {
	
	/**
	 * Called when an image download has completed
	 * @param image
	 */
	public void onFinish(Bitmap image);

}
