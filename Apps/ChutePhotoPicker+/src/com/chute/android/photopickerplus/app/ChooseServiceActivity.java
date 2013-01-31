/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.app;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.intent.AlbumsActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.account.GCAccounts;
import com.chute.sdk.api.authentication.GCAuthenticationFactory.AccountType;
import com.chute.sdk.collections.GCAccountsCollection;
import com.chute.sdk.model.GCAccountMediaModel;
import com.chute.sdk.model.GCAccountModel;
import com.chute.sdk.model.GCAccountStore;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.utils.GCPreferenceUtil;
import com.darko.imagedownloader.ImageLoader;


public class ChooseServiceActivity extends Activity {

	public static final String TAG = ChooseServiceActivity.class
			.getSimpleName();

	private TextView txtFacebook;
	private TextView txtPicasa;
	private TextView txtFlickr;
	private TextView txtInstagram;
	private LinearLayout linearLayoutServices;
	private LinearLayout take_photos;
	private LinearLayout facebook;
	private LinearLayout picasa;
	private LinearLayout instagram;
	private LinearLayout flickr;
	private LinearLayout allPhotos;
	private LinearLayout cameraPhotos;
	private LinearLayout lastPhoto;
	private ImageView img_all_photos;
	private ImageView img_camera_photos;
	private ImageView img_last_photo;
	private AccountType accountType;
	private ImageLoader loader;
	private PhotoPickerPlusIntentWrapper ppWrapper;

	private TextView textViewLabelUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.service_layout);

		loader = ImageLoader.getLoader(ChooseServiceActivity.this);

		ppWrapper = new PhotoPickerPlusIntentWrapper(getIntent());

		linearLayoutServices = (LinearLayout) findViewById(R.id.services_linear);
		textViewLabelUser = (TextView) findViewById(R.id.txt_user);
		PhotoPickerPlusIntentWrapper photoPickerPlusIntentWrapper = new PhotoPickerPlusIntentWrapper(
				getIntent());
		if (photoPickerPlusIntentWrapper.areServicesHidden()) {
			linearLayoutServices.setVisibility(View.GONE);
			textViewLabelUser.setVisibility(View.GONE);
		}
		txtFacebook = (TextView) findViewById(R.id.txt_facebook);
		txtFacebook.setTag(AccountType.FACEBOOK);
		txtPicasa = (TextView) findViewById(R.id.txt_picasa);
		txtPicasa.setTag(AccountType.PICASA);
		txtFlickr = (TextView) findViewById(R.id.txt_flickr);
		txtFlickr.setTag(AccountType.FLICKR);
		txtInstagram = (TextView) findViewById(R.id.txt_instagram);
		txtInstagram.setTag(AccountType.INSTAGRAM);

		facebook = (LinearLayout) findViewById(R.id.linear_fb);
		facebook.setTag(AccountType.FACEBOOK);
		flickr = (LinearLayout) findViewById(R.id.linear_flickr);
		flickr.setTag(AccountType.FLICKR);
		picasa = (LinearLayout) findViewById(R.id.linear_picasa);
		picasa.setTag(AccountType.PICASA);
		instagram = (LinearLayout) findViewById(R.id.linear_instagram);
		instagram.setTag(AccountType.INSTAGRAM);

		allPhotos = (LinearLayout) findViewById(R.id.all_photos_linear);
		allPhotos.setOnClickListener(new OnPhotoStreamListener());

		cameraPhotos = (LinearLayout) findViewById(R.id.camera_shots_linear);
		cameraPhotos.setOnClickListener(new OnCameraRollListener());

		lastPhoto = (LinearLayout) findViewById(R.id.last_photo_linear);
		lastPhoto.setOnClickListener(new OnLastPhotoClickListener());

		img_all_photos = (ImageView) findViewById(R.id.all_photos_icon);
		img_camera_photos = (ImageView) findViewById(R.id.camera_shots_icon);
		img_last_photo = (ImageView) findViewById(R.id.last_photo_icon);

		loader.displayImage(
				MediaDAO.getLastPhotoFromAllPhotos(getApplicationContext())
						.toString(), img_all_photos);

		Uri uri = MediaDAO
				.getLastPhotoFromCameraPhotos(getApplicationContext());
		if (uri != null) {
			loader.displayImage(uri.toString(), img_camera_photos);
			loader.displayImage(uri.toString(), img_last_photo);
		}
		take_photos = (LinearLayout) findViewById(R.id.album3_linear);
		take_photos.setOnClickListener(new OnCameraClickListener());

		facebook.setOnClickListener(new OnLoginClickListener());
		picasa.setOnClickListener(new OnLoginClickListener());
		flickr.setOnClickListener(new OnLoginClickListener());
		instagram.setOnClickListener(new OnLoginClickListener());
	}

	private final class OnLoginClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			accountType = (AccountType) v.getTag();
			if (GCPreferenceUtil.get().hasAccountId(accountType)) {
				accountClicked(
						GCPreferenceUtil.get().getAccountId(accountType),
						accountType.getName());
			} else {
				GCAccountStore.getInstance(getApplicationContext())
						.startAuthenticationActivity(
								ChooseServiceActivity.this, accountType,
								Constants.PERMISSIONS_SCOPE,
								Constants.CALLBACK_URL, Constants.CLIENT_ID,
								Constants.CLIENT_SECRET);
			}
		}
	}

	private final class AccountsCallback implements
			GCHttpCallback<GCAccountsCollection> {

		@Override
		public void onSuccess(GCAccountsCollection responseData) {
			if (accountType == null) {
				return;
			}
			for (GCAccountModel accountModel : responseData) {
				if (accountModel.getType().equalsIgnoreCase(
						accountType.getName())) {
					GCPreferenceUtil.get().setNameForAccount(accountType,
							accountModel.getUser().getName());
					GCPreferenceUtil.get().setIdForAccount(accountType,
							accountModel.getId());
					accountClicked(accountModel.getId(), accountType.getName());
				}
			}
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
		}

	}

	public void accountClicked(String accountId, String accountName) {
		AlbumsActivityIntentWrapper wrapper = new AlbumsActivityIntentWrapper(
				ChooseServiceActivity.this);
		wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
		wrapper.setAccountId(accountId);
		wrapper.setAccountName(accountName);
		wrapper.startActivity(ChooseServiceActivity.this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GCAccountStore.AUTHENTICATION_REQUEST_CODE) {
				GCAccounts.all(getApplicationContext(), new AccountsCallback())
						.executeAsync();
			}
			if (requestCode == PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY) {
				finish();
			} else if (requestCode == Constants.CAMERA_PIC_REQUEST) {
				// Bitmap image = (Bitmap) data.getExtras().get("data");

				String path = "";
				File tempFile = AppUtil.getTempFile(getApplicationContext());
				if (AppUtil.hasImageCaptureBug() == false
						&& tempFile.length() > 0) {
					try {
						android.provider.MediaStore.Images.Media.insertImage(
								getContentResolver(),
								tempFile.getAbsolutePath(), null, null);
						tempFile.delete();
						path = MediaDAO.getLastPhotoFromCameraPhotos(
								getApplicationContext()).toString();
					} catch (FileNotFoundException e) {
						Log.d(TAG, "", e);
					}
				} else {
					Log.e(TAG, "Bug " + data.getData().getPath());
					path = Uri.fromFile(
							new File(AppUtil.getPath(getApplicationContext(),
									data.getData()))).toString();
				}
				Log.d(TAG, path);
				final GCAccountMediaModel model = new GCAccountMediaModel();
				model.setLargeUrl(path);
				model.setThumbUrl(path);
				model.setUrl(path);

				IntentUtil.deliverDataToInitialActivity(this, model,
						ppWrapper.getChuteId());
			}
		}
	}

	private class OnCameraClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA)) {
				NotificationUtil.makeToast(getApplicationContext(),
						R.string.toast_feature_camera);
				return;
			}
			final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (AppUtil.hasImageCaptureBug() == false) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(AppUtil
						.getTempFile(ChooseServiceActivity.this)));
			} else {
				intent.putExtra(
						android.provider.MediaStore.EXTRA_OUTPUT,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			}
			startActivityForResult(intent, Constants.CAMERA_PIC_REQUEST);
		}
	}

	private final class OnPhotoStreamListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(
					ChooseServiceActivity.this);
			wrapper.setFilterType(PhotosIntentWrapper.TYPE_ALL_PHOTOS);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setChuteId(ppWrapper.getChuteId());
			wrapper.startActivityForResult(ChooseServiceActivity.this,
					PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);
		}
	}

	private final class OnCameraRollListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(
					ChooseServiceActivity.this);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setFilterType(PhotosIntentWrapper.TYPE_CAMERA_ROLL);
			wrapper.setChuteId(ppWrapper.getChuteId());
			wrapper.startActivityForResult(ChooseServiceActivity.this,
					PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);
		}

	}

	private final class OnLastPhotoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Uri uri = MediaDAO
					.getLastPhotoFromCameraPhotos(getApplicationContext());
			if (uri.toString().equals("")) {
				NotificationUtil.makeToast(getApplicationContext(),
						getResources().getString(R.string.no_camera_photos));
			} else {
				final GCAccountMediaModel model = new GCAccountMediaModel();
				model.setLargeUrl(uri.toString());
				model.setThumbUrl(uri.toString());
				model.setUrl(uri.toString());

				IntentUtil.deliverDataToInitialActivity(
						ChooseServiceActivity.this, model,
						ppWrapper.getChuteId());
			}
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setResult(Activity.RESULT_OK,
				new Intent().putExtras(intent.getExtras()));
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (GCPreferenceUtil.get().hasAccountId(AccountType.PICASA)) {
			if (GCPreferenceUtil.get().hasAccountName(AccountType.PICASA)) {
				txtPicasa.setText(GCPreferenceUtil.get().getAccountName(
						AccountType.PICASA));

			}
		}
		if (GCPreferenceUtil.get().hasAccountId(AccountType.FACEBOOK)) {
			if (GCPreferenceUtil.get().hasAccountName(AccountType.FACEBOOK)) {
				txtFacebook.setText(GCPreferenceUtil.get().getAccountName(
						AccountType.FACEBOOK));
			}
		}
		if (GCPreferenceUtil.get().hasAccountId(AccountType.FLICKR)) {
			if (GCPreferenceUtil.get().hasAccountName(AccountType.FLICKR)) {
				txtFlickr.setText(GCPreferenceUtil.get().getAccountName(
						AccountType.FLICKR));
			}
		}
		if (GCPreferenceUtil.get().hasAccountId(AccountType.INSTAGRAM)) {
			if (GCPreferenceUtil.get().hasAccountName(AccountType.INSTAGRAM)) {
				txtInstagram.setText(GCPreferenceUtil.get().getAccountName(
						AccountType.INSTAGRAM));
			}
		}
	}
}
