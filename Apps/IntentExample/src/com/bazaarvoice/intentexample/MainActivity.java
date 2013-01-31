package com.bazaarvoice.intentexample;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * MainActivity.java <br>
 * IntentExample<br>
 * 
 * <p>
 * A simple view that shows you the image that will be shared, and allows you to
 * set the caption and the story information associated with the submission.<br>
 * 
 * This uses the Instagram model of uploading the photo while the user inputs
 * the submission info. This way the amount of time spent waiting by the user is
 * significantly decreased. <br>
 * 
 * Two different methods of uploading are implemented. If you set uploadType to
 * DIALOG, the Activity will stay open and show a dialog while uploading. If you
 * set uploadType to NOTIFICATION, the Activity will close when the user clicks
 * "Submit Photo" and progress will be shown in the notification bar.
 * 
 * <p>
 * OOM Issue:<br>
 * There is a known issue with running out of memory with an app that has a
 * large bitmap when it is rotated a few times. There are a lot of discussions
 * and recommendations about what to do about it on StackOverflow and other
 * sites. This example is not meant to show you the best practice for handling
 * such a situation but our recommendation is to force portrait mode (which is
 * what we do here) in the manifest file and to consider scaling the bitmap
 * down. Forcing portrait mode is not a flawless solution, though, because when
 * a physical keyboard is pulled out, the system will force the phone into
 * landscape.
 * 
 * <p>
 * Created on 7/19/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class MainActivity extends Activity {

	private final int DIALOG = 0;
	private final int NOTIFICATION = 1;
	private int uploadType = NOTIFICATION;

	protected final int STORY_FIELDS = 1337;

	protected Button addStory;
	protected Button submit;
	private ProgressDialog dialog;
	private Notification notification;
	private NotificationManager notificationManager;

	/**
	 * Called when the activity is first created. This pulls in the photo from
	 * wherever it is being shared and sets up the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		String action = intent.getAction();

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// if this is from the share menu
		if (Intent.ACTION_SEND.equals(action)) {
			if (extras.containsKey(Intent.EXTRA_STREAM)) {

				// Get resource path from intent callee
				Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
				File file = new File(CameraUtils.getRealPathFromURI(uri, this));
				final ImageView img = (ImageView) findViewById(R.id.imagePreview);
				Bitmap image;
				try {
					image = CameraUtils.getOrientedBitmap(uri,
							getApplicationContext(), img.getHeight(), img.getWidth());
					img.setImageBitmap(image);
					image = null;
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
					e.printStackTrace();
				}

				/*
				 * Go ahead and submit the photo. If the user actually clicks
				 * submit, then we'll submit the story.
				 */
				BazaarFunctions.doPhotoSubmission(file);

				initializeUI();
			}
		}
	}

	/**
	 * Puts listeners on the "Add Story" and "Submit Photo" buttons.
	 */
	private void initializeUI() {
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				EditText caption = (EditText) findViewById(R.id.caption);
				caption.setEnabled(false);
				BazaarFunctions.setStoryCaption(caption.getText().toString());
				BazaarFunctions.setSubmissionResponse(new SubmissionResponseHandler(MainActivity.this));
				BazaarFunctions.doStorySubmission();

				if (uploadType == NOTIFICATION) {
					notification = new Notification(R.drawable.notif_icon,
							"Uploading photo...", System.currentTimeMillis());

					PendingIntent intent = PendingIntent.getActivity(
							getApplicationContext(), 0, new Intent(), 0);
					notification.setLatestEventInfo(getApplicationContext(),
							"BV Photo Share", "Your photo is being uploaded.",
							intent);
					notificationManager.notify(1, notification);
					finish();

				} else if (uploadType == DIALOG) {
					dialog = new ProgressDialog(MainActivity.this);
					dialog.setCancelable(false);
					dialog.setMessage("Submitting...");
					dialog.show();
				}
			}
		});

		addStory = (Button) findViewById(R.id.addStory);
		addStory.setOnClickListener(new View.OnClickListener() {

			/*
			 * The preferred method here is now to use DialogFragment with
			 * FragmentManager.
			 */
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(STORY_FIELDS);
				addStory.setText("Edit Story");
			}

		});
	}

	/**
	 * Creates our custom dialog that has the forms to input story content.
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == STORY_FIELDS) {
			LayoutInflater factory = LayoutInflater
					.from(getApplicationContext());
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_story_fields, null);
			return new AlertDialog.Builder(MainActivity.this)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									verifyStoryInput(textEntryView);
								}

							}).setTitle("Add a Story").setView(textEntryView)
					.create();
		} else {
			return super.onCreateDialog(id);
		}
	}

	/**
	 * Verifies that the story content inputed in the given View is valid and
	 * then sets the content with the corresponding BazaarFunctions calls and
	 * updates the UI.
	 * 
	 * Does both client-side and server-side validation.
	 * 
	 * @param textEntryView
	 *            the View associated with the story content submission dialog
	 */
	protected void verifyStoryInput(View textEntryView) {
		final EditText title = (EditText) textEntryView
				.findViewById(R.id.title);
		final EditText story = (EditText) textEntryView
				.findViewById(R.id.story);

		/*
		 * Client-side validation
		 */
		if ("".equals(title.getText().toString())) {
			Toast.makeText(getApplicationContext(), "You must enter a title.",
					Toast.LENGTH_LONG).show();
		} else if ("".equals(story.getText().toString())) {
			Toast.makeText(getApplicationContext(), "You must enter a story.",
					Toast.LENGTH_LONG).show();
		} else {

			/*
			 * Server-side validation
			 */
			final ProgressDialog verifyDialog = new ProgressDialog(
					MainActivity.this);
			verifyDialog.setCancelable(false);
			verifyDialog.setMessage("Verifying story requirements.");
			verifyDialog.show();

			BazaarFunctions.doStoryPreview(story.getText().toString(), title
					.getText().toString(), new BazaarUIThreadResponse(this) {
				@Override
				public void onUiResponse(JSONObject json) {
					/*
					 * Basic error checking for form errors. This does not
					 * consider any other kinds of errors and should be more
					 * robust.
					 */
					try {
						if (json.getBoolean("HasErrors")) {
							JSONObject formErrors = json
									.getJSONObject("FormErrors");
							String error = formErrors.getJSONArray(
									"FieldErrorsOrder").getString(0);
							String message = formErrors
									.getJSONObject("FieldErrors")
									.getJSONObject(error).getString("Message");
							Toast.makeText(getApplicationContext(), message,
									Toast.LENGTH_LONG).show();
							addStory.setEnabled(true);
							submit.setEnabled(false);
						} else {
							addStory.setEnabled(false);
							submit.setEnabled(true);
							BazaarFunctions.setStoryTitle(title.getText()
									.toString());
							BazaarFunctions.setStoryText(story.getText()
									.toString());

						}
					} catch (JSONException e) {
						Log.e("Preview Submission", "Error = " + e.getMessage()
								+ "/n" + Log.getStackTraceString(e));
					}
					verifyDialog.dismiss();
				}

			});

		}
	}

	@SuppressWarnings("deprecation")
	public void notifyError() {
		/*
		 * With the NOTIFICATION method, the Activity has been closed, so we
		 * need to alert the user through notifications. Depending on your case,
		 * you may want the PendingIntent to take the user somewhere useful.
		 */
		if (uploadType == NOTIFICATION) {
			notification = new Notification(R.drawable.notif_icon,
					"Error in upload.", System.currentTimeMillis());
			PendingIntent intent = PendingIntent.getActivity(
					getApplicationContext(), 0, new Intent(), 0);
			notification.setLatestEventInfo(getApplicationContext(),
					"BV Photo Share",
					"Your photo failed to upload. Please try again.", intent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(1, notification);
		}

		/*
		 * With the DIALOG method, we can just dismiss the dialog and alert the
		 * user, letting them click "Submit" again.
		 */
		else if (uploadType == DIALOG) {
			runOnUiThread(new Runnable() {
				public void run() {
					dialog.dismiss();
					Toast.makeText(getApplicationContext(),
							"Upload failed. Try again.", Toast.LENGTH_LONG)
							.show();
				}
			});
		}
	}

	/**
	 * An implementation of OnBazaarResponse used for handling story submission.
	 * It checks the response to see if there are errors and alerts the user of
	 * them. If there are no errors, it closes the Activity.
	 * 
	 */
	private class SubmissionResponseHandler extends BazaarUIThreadResponse {
		
		public SubmissionResponseHandler(Activity context) {
			super(context);
		}

		private String TAG = "Submission response";

		@SuppressWarnings("deprecation")
		public void onUiResponse(JSONObject json) {
			Log.i(TAG, json.toString());
			try {
				if (json.getBoolean("HasErrors")) {

					/*
					 * Do further error checking here but note that when this
					 * code executes the Activity will not be active if
					 * (uploadType == NOTIFICATION).
					 */
					notifyError();

				} else {
					if (uploadType == DIALOG) {
						dialog.dismiss();
						finish();
					}

					/*
					 * Update the notification if we are using NOTIFICATION
					 * style, or display a new notification if we are using
					 * DIALOG style to show the user that the photo was uploaded
					 * when the Activity closes.
					 */
					notification = new Notification(R.drawable.notif_icon,
							"Photo Uploaded.", System.currentTimeMillis());
					PendingIntent intent = PendingIntent.getActivity(
							getApplicationContext(), 0, new Intent(), 0);
					notification.setLatestEventInfo(getApplicationContext(),
							"BV Photo Share", "Your photo has been uploaded.",
							intent);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notificationManager.notify(1, notification);
				}
			} catch (JSONException e) {
				Log.e(TAG,
						"Error = " + e.getMessage() + "/n"
								+ Log.getStackTraceString(e));
			}
		}

		public void onException(String message, Throwable exception) {
			Log.e(TAG,
					"Error = " + message + "\n"
							+ Log.getStackTraceString(exception));
			notifyError();
		}
	}

}
