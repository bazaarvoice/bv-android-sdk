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

	private static ArrayList<BazaarProduct> products;
	private static int curProduct;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews root = new RemoteViews(context.getPackageName(),
				R.layout.browse_widget);
		
		root.setViewVisibility(R.id.progressBar, View.VISIBLE);
		root.setViewVisibility(R.id.productHolder, View.GONE);
		root.setViewVisibility(R.id.bottomButtons, View.GONE);
		refreshWidget(context, root);

		// Use asynctask to avoid ANR timeout
		AsyncDownloadProducts task = new AsyncDownloadProducts();
		task.execute(new Context[] { context });
	}
	
	private PendingIntent getNavigationIntent(Context context, int id) {
		Intent clickIntent = new Intent(context,
				ProductWidgetProvider.class);
		clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		clickIntent.putExtra(TRIGGER, id);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				id, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	private void refreshWidget(Context context, RemoteViews root) {
		root.setOnClickPendingIntent(R.id.back, getNavigationIntent(context, R.id.back));
		root.setOnClickPendingIntent(R.id.forward,
				getNavigationIntent(context, R.id.forward));
		updateProductView(context, root);
		
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisWidget = new ComponentName(context,
				ProductWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(thisWidget, root);
	}

	/**
	 * Handles incoming intents. If it is from a navigation action, calls
	 * onNavigate().
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("onReceive", "intent received");
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

		RemoteViews root = new RemoteViews(context.getPackageName(),
				R.layout.browse_widget);

		if (id == R.id.back) {
			Log.i("Counter Back", "Size = " + products.size());
			Log.i("Counter Back", "Before = " + curProduct);
			curProduct = (curProduct - 1) % products.size();
			if (curProduct < 0) {
				curProduct += products.size();
			}
			Log.i("Counter Back", "After = " + curProduct);
		} else if (id == R.id.forward) {
			Log.i("Counter Forward", "Size = " + products.size());
			Log.i("Counter Forward", "Before = " + curProduct);
			curProduct = (curProduct + 1) % products.size();
			Log.i("Counter Forward", "After = " + curProduct);
		}

		refreshWidget(context, root);
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
			curProduct = 0;
			products = new ArrayList<BazaarProduct>();
			final Counter counter = new Counter(1);
			final RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.browse_widget);

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
							products.add(newProduct);
							newProduct
									.downloadImage(new OnImageDownloadComplete() {

										@Override
										public void onFinish(Bitmap image) {
											counter.decrement();
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

			updateProductView(context, views);

			views.setViewVisibility(R.id.progressBar, View.GONE);
			views.setViewVisibility(R.id.productHolder, View.VISIBLE);
			views.setViewVisibility(R.id.bottomButtons, View.VISIBLE);
			return views;
		}

		@Override
		protected void onPostExecute(RemoteViews updateViews) {
			refreshWidget(context, updateViews);
		}

		

	}

	public void updateProductView(Context context, RemoteViews views) {
		if (products == null){
			return;
		}
		BazaarProduct product = products.get(curProduct);
		views.setImageViewBitmap(R.id.productImage, product.getImageBitmap());
		views.setTextViewText(R.id.productTitle, product.getName());

		Intent intent = new Intent(context, ReviewsActivity.class);
		intent.putExtra("product", product);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				intent.hashCode(), intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		views.setOnClickPendingIntent(R.id.productImage, pendingIntent);
	}
}
