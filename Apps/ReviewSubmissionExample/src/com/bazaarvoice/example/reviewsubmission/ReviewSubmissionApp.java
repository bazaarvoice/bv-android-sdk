package com.bazaarvoice.example.reviewsubmission;

import com.chute.android.photopickerplus.app.PhotoPickerPlusApp;
import com.chute.sdk.model.GCAccountStore;

public class ReviewSubmissionApp extends PhotoPickerPlusApp {
	
	@Override
	public void onCreate() {
		super.onCreate();
		GCAccountStore.setAppId(getApplicationContext(), "50ca0101018d165886000108");
	}

}
