/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.util.intent;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.chute.android.photopickerplus.app.ChooseServiceActivity;
import com.chute.sdk.collections.GCAccountMediaCollection;
import com.chute.sdk.model.GCAccountMediaModel;

public class IntentUtil {

    public static void deliverDataToInitialActivity(final Activity context,
	    final GCAccountMediaModel model, final String chuteId) {
	deliverDataToInitialActivity(context, model, null, null, chuteId);
    }

    public static void deliverDataToInitialActivity(final Activity context,
	    final GCAccountMediaModel model, final String albumId, final String accountId, final String chuteId) {
	GCAccountMediaCollection mediaCollection = new GCAccountMediaCollection();
	mediaCollection.add(model);
	deliverDataToInitialActivity(context, mediaCollection, chuteId);
    }

    public static void deliverDataToInitialActivity(final Activity context,
	    final GCAccountMediaCollection collection, final String chuteId) {
	deliverDataToInitialActivity(context, collection, null, null, chuteId);
    }

    public static void deliverDataToInitialActivity(final Activity context,
	    final GCAccountMediaCollection collection, final String albumId, final String accountId, final String chuteId) {
	final PhotoActivityIntentWrapper wrapper = new PhotoActivityIntentWrapper(new Intent(
		context, ChooseServiceActivity.class));
	if (!TextUtils.isEmpty(accountId)) {
	    wrapper.setAccountId(accountId);
	}
	if (!TextUtils.isEmpty(albumId)) {
	    wrapper.setAlbumId(albumId);
	}
	wrapper.setChuteId(chuteId);
	wrapper.setMediaCollection(collection);
	wrapper.getIntent().addFlags(
		Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	wrapper.startActivity(context);
    }
}
