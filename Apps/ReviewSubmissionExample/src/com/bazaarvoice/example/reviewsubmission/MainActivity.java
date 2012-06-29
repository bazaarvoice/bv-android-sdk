/**
 * MainActivity.java
 * ReviewSubmissionExample
 */
package com.bazaarvoice.example.reviewsubmission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String productId = "1000001";
	
	protected static final int CAMERA_REQUEST = 1337;
	private static final String TAG = "MainActivity";
	private Button rateButton;
	private Uri imageUri;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpRateButton();
        
        if(savedInstanceState != null){
        	imageUri = Uri.parse(savedInstanceState.getString("uri"));
        }
        else{
            imageUri = Uri.parse("");
        }
    }
    
    public void onSaveInstanceState(Bundle bundle){
    	super.onSaveInstanceState(bundle);
    	bundle.putString("uri", imageUri.toString());
    }

	private void setUpRateButton() {
		rateButton = (Button) findViewById(R.id.rateButton);
		rateButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				imageUri = CameraUtils.getPhotoUri(getBaseContext());
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CAMERA_REQUEST);
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		Log.i(TAG, "onActivityResultCode = " + resultCode);
		Bitmap orientedImage = null;
		try {
			orientedImage = CameraUtils.getOrientedBitmap(imageUri, this);
		} catch (IOException exception) {
			Log.e(TAG, "Error = " + exception.getMessage() + "\n" + Log.getStackTraceString(exception));
			Toast.makeText(getBaseContext(), "Error retrieving photo", Toast.LENGTH_LONG).show();
			return;
		}
		//Send image to next view
		Bitmap scaledImage = Bitmap.createScaledBitmap(orientedImage, 200, 200, true);
		Intent intent = new Intent(this, RatingActivity.class);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		scaledImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		intent.putExtra("capturedImage", byteArray);
		intent.putExtra("imageUri", imageUri.toString());
		startActivity(intent);
	}

}
