/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.adapter.PhotoSelectCursorAdapter;
import com.chute.android.photopickerplus.adapter.PhotosAdapter;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.account.GCAccounts;
import com.chute.sdk.collections.GCAccountMediaCollection;
import com.chute.sdk.model.GCHttpRequestParameters;

public class GridActivity extends Activity {

	public static final String TAG = GridActivity.class.getSimpleName();

	private GridView grid;
	private PhotoSelectCursorAdapter cursorAdapter;
	private PhotosAdapter socialAdapter;
	private PhotosIntentWrapper wrapper;
	private TextView selectPhotos;
	private View emptyView;

	private String accountId;
	private String albumId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.photos_select);

		selectPhotos = (TextView) findViewById(R.id.txt_select_photos);
		grid = (GridView) findViewById(R.id.gridView);
		emptyView = findViewById(R.id.empty_view_layout);
		grid.setEmptyView(emptyView);

		wrapper = new PhotosIntentWrapper(getIntent());

		Button ok = (Button) findViewById(R.id.btnOk);
		ok.setOnClickListener(new OkClickListener());
		Button cancel = (Button) findViewById(R.id.btnCancel);
		cancel.setOnClickListener(new CancelClickListener());

		if ((wrapper.getFilterType() == PhotosIntentWrapper.TYPE_ALL_PHOTOS)
				|| (wrapper.getFilterType() == PhotosIntentWrapper.TYPE_CAMERA_ROLL)) {
			new LoadCursorTask().execute();
		} else if (wrapper.getFilterType() == PhotosIntentWrapper.TYPE_SOCIAL_PHOTOS) {
			accountId = wrapper.getAccountId();
			albumId = wrapper.getAlbumId();
			GCAccounts.objectMedia(getApplicationContext(), accountId, albumId,
					new PhotoListCallback()).executeAsync();
		}

	}

	private class LoadCursorTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(final Void... arg0) {
			if (wrapper.getFilterType() == PhotosIntentWrapper.TYPE_ALL_PHOTOS) {
				return MediaDAO.getAllMediaPhotos(getApplicationContext());
			} else if (wrapper.getFilterType() == PhotosIntentWrapper.TYPE_CAMERA_ROLL) {
				return MediaDAO.getCameraPhotos(getApplicationContext());
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(final Cursor result) {
			super.onPostExecute(result);
			if (result == null) {
				return;
			}
			cursorAdapter = new PhotoSelectCursorAdapter(GridActivity.this,
					result);
			grid.setAdapter(cursorAdapter);

			if (cursorAdapter.getCount() == 0) {
				emptyView.setVisibility(View.GONE);
			}

			if (wrapper.getIsMultiPicker() == true) {
				selectPhotos.setText(getApplicationContext().getResources()
						.getString(R.string.select_photos));
				grid.setOnItemClickListener(new OnMultiSelectGridItemClickListener());
			} else {
				selectPhotos.setText(getApplicationContext().getResources()
						.getString(R.string.select_a_photo));
				grid.setOnItemClickListener(new OnSingleSelectGridItemClickListener());
			}
			NotificationUtil.showPhotosAdapterToast(getApplicationContext(),
					cursorAdapter.getCount());
		}
	}

	private final class OnMultiSelectGridItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view,
				final int position, final long id) {
			cursorAdapter.toggleTick(position);
		}
	}

	private final class OnSingleSelectGridItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view,
				final int position, final long id) {
			IntentUtil.deliverDataToInitialActivity(GridActivity.this,
					AppUtil.getMediaModel(cursorAdapter.getItem(position)),
					wrapper.getChuteId());
			setResult(RESULT_OK);
			finish();
		}
	}

	private final class CancelClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}

	}

	private final class PhotoListCallback implements
			GCHttpCallback<GCAccountMediaCollection> {

		@Override
		public void onSuccess(GCAccountMediaCollection responseData) {
			socialAdapter = new PhotosAdapter(GridActivity.this, responseData);
			grid.setAdapter(socialAdapter);

			if (socialAdapter.getCount() == 0) {
				emptyView.setVisibility(View.GONE);
			}

			if (wrapper.getIsMultiPicker() == true) {
				selectPhotos.setText(getApplicationContext().getResources()
						.getString(R.string.select_photos));
				grid.setOnItemClickListener(new OnMultiGridItemClickListener());
			} else {
				selectPhotos.setText(getApplicationContext().getResources()
						.getString(R.string.select_a_photo));
				grid.setOnItemClickListener(new OnSingleGridItemClickListener());
			}
			NotificationUtil.showPhotosAdapterToast(getApplicationContext(),
					socialAdapter.getCount());
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			NotificationUtil
					.makeConnectionProblemToast(getApplicationContext());
			toggleEmptyViewErrorMessage();
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			NotificationUtil.makeServerErrorToast(getApplicationContext());
			toggleEmptyViewErrorMessage();
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			NotificationUtil.makeParserErrorToast(getApplicationContext());
			toggleEmptyViewErrorMessage();
		}

		public void toggleEmptyViewErrorMessage() {
			findViewById(R.id.empty_view_layout).setVisibility(View.GONE);
		}
	}

	private final class OnSingleGridItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view,
				final int position, final long id) {
			IntentUtil.deliverDataToInitialActivity(GridActivity.this,
					socialAdapter.getItem(position), wrapper.getChuteId());
			setResult(RESULT_OK);
			finish();
		}
	}

	private final class OnMultiGridItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view,
				final int position, final long id) {
			socialAdapter.toggleTick(position);
		}
	}

	private final class OkClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (wrapper.getFilterType() == PhotosIntentWrapper.TYPE_SOCIAL_PHOTOS) {
				IntentUtil.deliverDataToInitialActivity(GridActivity.this,
						socialAdapter.getPhotoCollection(), null, null,
						wrapper.getChuteId());
				setResult(RESULT_OK);
			} else if ((wrapper.getFilterType() == PhotosIntentWrapper.TYPE_ALL_PHOTOS)
					|| (wrapper.getFilterType() == PhotosIntentWrapper.TYPE_CAMERA_ROLL)) {
				IntentUtil.deliverDataToInitialActivity(GridActivity.this,
						AppUtil.getPhotoCollection(cursorAdapter
								.getSelectedFilePaths()), null, null, wrapper
								.getChuteId());
				setResult(RESULT_OK);
			}
			finish();
		}
	}
}
