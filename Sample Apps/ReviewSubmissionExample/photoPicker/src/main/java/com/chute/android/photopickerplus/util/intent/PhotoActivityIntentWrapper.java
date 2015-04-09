/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.util.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.chute.android.photopickerplus.app.GridActivity;
import com.chute.sdk.collections.GCAccountMediaCollection;

public class PhotoActivityIntentWrapper extends IntentWrapper {

	@SuppressWarnings("unused")
	private static final String TAG = PhotoActivityIntentWrapper.class
			.getSimpleName();

	public static final int ACTIVITY_FOR_RESULT_PHOTO_KEY = 115;
	private static final String KEY_ACCOUNT_ID = "accountId";
	private static final String KEY_ALBUM_ID = "albumId";
	private static final String KEY_PHOTO_COLLECTION = "photoCollection";
	private static final String KEY_PHOTO_MODEL = "photoModel";
	private static final String FLAG_MULTI_PICKER = "flagMultiPicker";
	private static final String KEY_CHUTE_ID = "key_chuteId";

	public PhotoActivityIntentWrapper(Context context) {
		super(context, GridActivity.class);
	}

	public PhotoActivityIntentWrapper(Intent intent) {
		super(intent);
	}

	public String getAccountId() {
		return getIntent().getExtras().getString(KEY_ACCOUNT_ID);
	}

	public void setAccountId(String accountId) {
		getIntent().putExtra(KEY_ACCOUNT_ID, accountId);
	}

	public String getAlbumId() {
		return getIntent().getExtras().getString(KEY_ALBUM_ID);
	}

	public void setAlbumId(String albumId) {
		getIntent().putExtra(KEY_ALBUM_ID, albumId);
	}

	public GCAccountMediaCollection getMediaCollection() {
		return getIntent().getExtras().getParcelable(KEY_PHOTO_COLLECTION);
	}

	public void setMediaCollection(GCAccountMediaCollection mediaCollection) {
		getIntent()
				.putExtra(KEY_PHOTO_COLLECTION, (Parcelable) mediaCollection);
	}

	public boolean getIsMultiPicker() {
		return getIntent().getExtras().getBoolean(FLAG_MULTI_PICKER);
	}

	public void setMultiPicker(boolean flag) {
		getIntent().putExtra(FLAG_MULTI_PICKER, flag);
	}

	public String getChuteId() {
		return getIntent().getExtras().getString(KEY_CHUTE_ID);
	}

	public void setChuteId(String id) {
		getIntent().putExtra(KEY_CHUTE_ID, id);
	}

	public void startActivityForResult(Activity context, int code) {
		context.startActivityForResult(getIntent(), code);
	}
}
