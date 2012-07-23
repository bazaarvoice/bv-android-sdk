package com.example.productwidgetexample;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bazaarvoice.OnBazaarResponse;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.ViewFlipper;

public class ProductWidgetProvider extends AppWidgetProvider {

	public static final String TRIGGER = "trigger";
	private static final String TAG = "Widget";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i(TAG, "onUpdate() called");

		// Use asynctask to avoid ANR timeout
		AsyncDownloadProducts task = new AsyncDownloadProducts();
		task.execute(new Context[] { context });
	}

	/**
	 * Handles incoming intents. If it is from a navigation action, calls
	 * onNavigate().
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Bundle extras = intent.getExtras();
		Integer id = (Integer) (extras == null ? null : extras.get(TRIGGER));
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)
				&& id != null) {
			onNavigate(context, id);
		} else {
			super.onReceive(context, intent);
		}
	}

	/**
	 * Moves the ViewFlipper based on an intent sent by clicking one of the
	 * buttons.
	 * 
	 * @param context
	 * @param id
	 */
	protected void onNavigate(Context context, Integer id) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		RemoteViews root = new RemoteViews(context.getPackageName(),
				R.layout.browse_widget);
		
		if (id == R.id.back) {
			root.showPrevious(R.id.productFlipper);
		} else {
			root.showNext(R.id.productFlipper);
		}
		ComponentName thisWidget = new ComponentName(context,
				ProductWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(thisWidget, root);
	}

	private class AsyncDownloadProducts extends
			AsyncTask<Context, Void, RemoteViews> {

		private Context context;

		private class Counter {
			int value;

			public Counter(int i) {
				value = i;
			}

			public void increment() {
				value++;
			}

			public void decrement() {
				value--;
			}

			public int getValue() {
				return value;
			}

			public void setValue(int val) {
				value = val;
			}
		}

		@Override
		protected RemoteViews doInBackground(Context... params) {
			context = params[0];
			final Counter counter = new Counter(1);
			final RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.browse_widget);
			views.setOnClickPendingIntent(R.id.forward,
					getNavigationIntent(R.id.forward));
			views.setOnClickPendingIntent(R.id.back,
					getNavigationIntent(R.id.back));

			BazaarFunctions.runProductQuery(new OnBazaarResponse() {

				@Override
				public void onException(String message, Throwable exception) {
					Log.e("Product Query", message);
					counter.setValue(0);
				}

				@Override
				public void onResponse(JSONObject json) {
					Log.i("Product Query", json.toString());
					try {
						JSONArray results = json.getJSONArray("Results");
						counter.setValue(results.length());
						for (int i = 0; i < results.length(); i++) {
							final BazaarProduct newProduct = new BazaarProduct(
									results.getJSONObject(i));
							newProduct
									.downloadImage(new OnImageDownloadComplete() {

										@Override
										public void onFinish(Bitmap image) {
											counter.decrement();
											RemoteViews product = new RemoteViews(
													context.getPackageName(),
													R.layout.product_widget_item);
											product.setImageViewBitmap(
													R.id.productImage, image);
											product.setTextViewText(
													R.id.productTitle,
													newProduct.getName());

											Intent intent = new Intent(context,
													ReviewsActivity.class);
											intent.putExtra("product",
													newProduct);
											PendingIntent pendingIntent = PendingIntent
													.getActivity(context,
															intent.hashCode(),
															intent, 0);
											product.setOnClickPendingIntent(
													R.id.productImage,
													pendingIntent);
											views.addView(R.id.productFlipper,
													product);
										}

									});
						}
					} catch (JSONException e) {
						Log.e("Product Query", e.getStackTrace().toString());
					}
				}

			});

			while (counter.getValue() > 0) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return views;
		}

		@Override
		protected void onPostExecute(RemoteViews updateViews) {
			ComponentName thisWidget = new ComponentName(context,
					ProductWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			manager.updateAppWidget(thisWidget, updateViews);
		}

		protected PendingIntent getNavigationIntent(final int id) {
			Intent clickIntent = new Intent(context,
					ProductWidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(TRIGGER, id);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			return pendingIntent;
		}

	}
}
