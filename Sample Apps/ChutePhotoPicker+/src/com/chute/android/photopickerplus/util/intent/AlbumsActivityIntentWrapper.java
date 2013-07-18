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

import com.chute.android.photopickerplus.app.AlbumsActivity;

public class AlbumsActivityIntentWrapper {

	@SuppressWarnings("unused")
	private static final String TAG = AlbumsActivityIntentWrapper.class
			.getSimpleName();

	private static final String KEY_ACCOUNT_ID = "accountId";
	private static final String KEY_ACCOUNT_NAME = "accountName";
	private static final String KEY_MULTI_PICKER = "keyMultiPicker";

	private final Intent intent;

	public AlbumsActivityIntentWrapper(Intent intent) {
		super();
		this.intent = intent;
	}

	public AlbumsActivityIntentWrapper(Context packageContext, Class<?> cls) {
		super();
		intent = new Intent(packageContext, cls);
	}

	public AlbumsActivityIntentWrapper(Context packageContext) {
		super();
		intent = new Intent(packageContext, AlbumsActivity.class);
	}

	public Intent getIntent() {
		return intent;
	}

	public String getAccountId() {
		return intent.getExtras().getString(KEY_ACCOUNT_ID);
	}

	public void setAccountId(String accountId) {
		intent.putExtra(KEY_ACCOUNT_ID, accountId);
	}

	public String getAccountName() {
		return intent.getExtras().getString(KEY_ACCOUNT_NAME);
	}

	public void setAccountName(String accountName) {
		intent.putExtra(KEY_ACCOUNT_NAME, accountName);
	}

	public boolean getIsMultiPicker() {
		return intent.getExtras().getBoolean(KEY_MULTI_PICKER);
	}

	public void setMultiPicker(boolean flag) {
		intent.putExtra(KEY_MULTI_PICKER, flag);
	}

	public void startActivity(Activity context) {
		context.startActivity(intent);
	}

}
