package com.example.productwidgetexample;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bazaarvoice.OnBazaarResponse;

/**
 * ProductWidgetProvider.java<br>
 * ProductWidgetExample<br>
 * 
 * <p>
 * This is the main class that defines the functionality of the widget. When it
 * updates, it downloads a new list of products asynchronously. To navigate
 * between products, the widget sends an intent to itself with the desired
 * direction of navigation.
 * 
 * <p>
 * Of mentionable note:
 * <ul>
 * <li>Every time you call
 * {@link AppWidgetManager#updateAppWidget(ComponentName, RemoteViews)} you need
 * to set all of the click listeners that you want because Android likes to
 * recycle old RemoteViews to save resources.</li>
 * 
 * <li>The flags set for the intent used to launch the ReviewsActivity are
 * deliberate to ensure that old activities are reused but are delivered new
 * intents.</li>
 * 
 * <li>The widget settings are set in res/xml/browse_widget_info.xml</li>
 * 
 * <p>
 * Created on 7/27/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public class ProductWidgetProvider extends AppWidgetProvider {

	public static final String TRIGGER = "trigger";
	@SuppressWarnings("unused")
	private static final String TAG = "Widget";

	private static ArrayList<BazaarProduct> products;
	private static int curProduct;

	/**
	 * Called when an instance of this widget is added to the home screen and
	 * whenever an update goes off as set in res/xml/browse_widget_info.xml.
	 */
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

	/**
	 * Sets all of the listeners needed to the RemoteViews object and updates
	 * the widget UI.
	 * 
	 * @param context
	 *            the application context
	 * @param root
	 *            the view representing the widget
	 */
	private void refreshWidget(Context context, RemoteViews root) {
		root.setOnClickPendingIntent(R.id.back,
				getNavigationIntent(context, R.id.back));
		root.setOnClickPendingIntent(R.id.forward,
				getNavigationIntent(context, R.id.forward));
		updateProductView(context, root);

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisWidget = new ComponentName(context,
				ProductWidgetProvider.class);
		appWidgetManager.updateAppWidget(thisWidget, root);
	}

	/**
	 * Returns the correct PendingIntent for the given button resource id. The
	 * back button and the forward button need to have different intents.
	 * 
	 * @param context
	 *            the application context
	 * @param id
	 *            the resource id of the button this PendingIntent is for
	 * @return the correct PendingIntent
	 */
	private PendingIntent getNavigationIntent(Context context, int id) {
		Intent clickIntent = new Intent(context, ProductWidgetProvider.class);
		clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		clickIntent.putExtra(TRIGGER, id);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
				clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	/**
	 * Sets the image, text, rating, and listener for the product information on the
	 * widget to match the current product in the list.
	 * 
	 * @param context
	 *            the application context
	 * @param root
	 *            the view representing the widget
	 */
	public void updateProductView(Context context, RemoteViews root) {
		if (products == null || products.size() == 0) {
			return;
		}
		BazaarProduct product = products.get(curProduct);
		root.setImageViewBitmap(R.id.productImage, product.getImageBitmap());
		root.setTextViewText(R.id.productTitle, product.getName());

		int resId = 0;
		switch ((int) product.getAverageRating()) {
		case (1):
			resId = R.drawable.one_star;
			break;
		case (2):
			resId = R.drawable.two_star;
			break;
		case (3):
			resId = R.drawable.three_star;
			break;
		case (4):
			resId = R.drawable.four_star;
			break;
		case (5):
			resId = R.drawable.five_star;
			break;
		default:
			resId = R.drawable.three_star;
			break;
		}
		root.setImageViewResource(R.id.productRating, resId);

		Intent intent = new Intent(context, ReviewsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("product", product);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		root.setOnClickPendingIntent(R.id.productImage, pendingIntent);
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
	 * Changes the current product index depending on which direction button was
	 * clicked and refreshes the widget.
	 * 
	 * @param context
	 *            the application context
	 * @param id
	 *            the resource id of the button pressed
	 */
	protected void onNavigate(Context context, Integer id) {
		if (products == null)
			return;

		RemoteViews root = new RemoteViews(context.getPackageName(),
				R.layout.browse_widget);

		if (id == R.id.back) {
			curProduct = (curProduct - 1) % products.size();
			if (curProduct < 0) {
				curProduct += products.size();
			}
		} else if (id == R.id.forward) {
			curProduct = (curProduct + 1) % products.size();
		}
		refreshWidget(context, root);
	}
	

	/**
	 * Handles the background download of a list of BazaarProducts.
	 * 
	 * <p>
	 * Created on 7/27/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
	 * 
	 * @author Bazaarvoice Engineering
	 */
	private class AsyncDownloadProducts extends
			AsyncTask<Context, Void, RemoteViews> {

		private Context context;

		/**
		 * A simple counter to make sure the download thread waits for all of
		 * the images to be downloaded.
		 * 
		 * <p>
		 * Created on 7/27/12. Copyright (c) 2012 BazaarVoice. All rights
		 * reserved.
		 * 
		 * @author Bazaarvoice Engineering
		 */
		private class Counter {
			int value;

			public Counter(int i) {
				value = i;
			}

			@SuppressWarnings("unused")
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

		/**
		 * Sends a request for products, downloads the images, and updates the
		 * widget UI.
		 */
		@Override
		protected RemoteViews doInBackground(Context... params) {
			context = params[0];
			curProduct = 0;
			products = new ArrayList<BazaarProduct>();
			final Counter counter = new Counter(1);
			final RemoteViews root = new RemoteViews(context.getPackageName(),
					R.layout.browse_widget);

			BazaarFunctions.runProductQuery(new OnBazaarResponse() {

				@Override
				public void onException(String message, Throwable exception) {
					Log.e("Product Query", "ERROR:" + message + exception.getMessage());
					counter.setValue(0);
				}

				@Override
				public void onResponse(JSONObject json) {
					Log.i("Product Query", "Query:" + json.toString());
					try {
						JSONArray results = json.getJSONArray("Results");
						
						/*
						 * Download the image for each product before updating the view
						 */
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

											/*
											 * Scale image down to work with
											 * less powerful phones.
											 */
											double factor = (image.getHeight() >= 200) ? image
													.getHeight() / 200 : 1;
											Bitmap smaller = Bitmap
													.createScaledBitmap(
															image,
															(int) (image
																	.getWidth() / factor),
															(int) (image
																	.getHeight() / factor),
															false);
											newProduct.setImageBitmap(smaller);
										}

									});
						}
					} catch (JSONException e) {
						Log.e("Product Query", "ERROR:" + e.getMessage() + " : " + e.getStackTrace().toString());
					}
				}

			});

			/*
			 * Wait for each product's image to be downloaded.
			 */
			while (counter.getValue() > 0) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			updateProductView(context, root);

			root.setViewVisibility(R.id.progressBar, View.GONE);
			root.setViewVisibility(R.id.productHolder, View.VISIBLE);
			root.setViewVisibility(R.id.bottomButtons, View.VISIBLE);
			return root;
		}

		/**
		 * Refreshes the newly created widget UI.
		 */
		@Override
		protected void onPostExecute(RemoteViews root) {
			refreshWidget(context, root);
		}
	}
}
