package com.bazaarvoice.example.reviewsubmission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.chute.android.photopickerplus.util.intent.PhotoActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.chute.sdk.model.GCAccountMediaModel;

/**
 * MainActivity.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is a product information screen with an option to create a rating for
 * the product. The photo is taken from this activity. If there are any issues
 * retrieving the photo, they are caught here and the user is not allowed to
 * move on to the review.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class MainActivity extends Activity {
	public static final String productId = "1000001";

	protected static final int CAMERA_REQUEST = 1337;
	private static final String TAG = "MainActivity";
	private Button rateButton;
	private Uri imageUri;

	/**
	 * Called when the activity is first created. Using savedInstanceState is
	 * important in the case that the app gets recycled.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setUpRateButton();

		if (savedInstanceState != null) {
			imageUri = Uri.parse(savedInstanceState.getString("uri"));
		} else {
			imageUri = Uri.parse("");

			/*
			 * This is an attempt to get rid of any photos that weren't removed
			 * before. (i.e. the user closed the app after taking a photo, but
			 * before submitting a review)
			 * 
			 * We must be careful not to do this during any onCreate() call
			 * because the activity may have just been recycled to save memory.
			 * In which case we don't want to lose the photo. If there is no
			 * savedInstanceState, then this is a fresh launch.
			 */
			CameraUtils.clearPhotos();
		}
	}

	/**
	 * Saves the uri to the file for the photo.
	 */
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("uri", imageUri.toString());
	}

	/**
	 * Wires up the rate button to launch the camera for a photo for the review.
	 */
	private void setUpRateButton() {
		rateButton = (Button) findViewById(R.id.rateButton);
		rateButton.setOnClickListener(new OnClickListener() {

			/*
			 * EXTRA_OUTPUT allows us to capture a full sized image and access
			 * the file for uploading.
			 */
			@Override
			public void onClick(View v) {
				
				/*
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				imageUri = CameraUtils.getPhotoUri(getBaseContext());
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CAMERA_REQUEST);
				*/
				
				 PhotoPickerPlusIntentWrapper wrapper = new PhotoPickerPlusIntentWrapper(MainActivity.this);
			     wrapper.setMultiPicker(false);
			     wrapper.startActivityForResult(MainActivity.this, PhotoPickerPlusIntentWrapper.REQUEST_CODE);

			}

		});
	}

	/**
	 * Pulls the bitmap out of the saved uri and forwards everything to the next
	 * activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		

	    if (resultCode != Activity.RESULT_OK) {
	        return;
	    } else if(requestCode == PhotoPickerPlusIntentWrapper.REQUEST_CODE) {
	    	// Chute response...
		    final PhotoActivityIntentWrapper wrapper = new PhotoActivityIntentWrapper(data);
		    GCAccountMediaModel mediaModel = wrapper.getMediaCollection().get(0);
		    Intent intent = new Intent(this, RatingActivity.class);
		    intent.putExtra("chuteMediaModel", mediaModel);
		    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
	    } else {
	    	// Standard camera response...
			Bitmap orientedImage = null;	    	
			try {
				orientedImage = CameraUtils.getOrientedBitmap(imageUri, this);
			} catch (IOException exception) {
				Log.e(TAG,
						"Error = " + exception.getMessage() + "\n"
								+ Log.getStackTraceString(exception));
				Toast.makeText(getBaseContext(), "Error retrieving photo",
						Toast.LENGTH_LONG).show();
				return;
			}
			/*
			 * Send image to next activity: On most phones, the taken image will be
			 * very large. Scaling it down here should improve performance.
			 */
			Bitmap scaledImage = Bitmap.createScaledBitmap(orientedImage, 200, 200,
					true);
			Intent intent = new Intent(this, RatingActivity.class);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			scaledImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			intent.putExtra("capturedImage", byteArray);
			intent.putExtra("imageUri", imageUri.toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
	    }
	    
	    
	}

}
