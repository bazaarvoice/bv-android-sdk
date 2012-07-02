package com.bazaarvoice.example.reviewsubmission;

/**
 * OnImageUploadComplete.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is an interface used for passing callback functions for image uploads.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public interface OnImageUploadComplete {
	
	/**
	 * Called when an image upload has completed
	 */
	public void onFinish();

}