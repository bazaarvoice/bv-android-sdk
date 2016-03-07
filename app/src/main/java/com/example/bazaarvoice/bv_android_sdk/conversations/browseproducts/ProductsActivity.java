/**
 * ProductsActivity.java <br>
 * BrowseProductExample<br>
 *
 * <p>
 * This is a product display screen. It loads the products according to the
 * search term submitted in the previous activity and utilizes the Statistics
 * API to display average rating.
 *
 * <p>
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 *
 * @author Bazaarvoice Engineering
 */

package com.example.bazaarvoice.bv_android_sdk.conversations.browseproducts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bazaarvoice.bv_android_sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * TODO: Description Here
 */
public class ProductsActivity extends Activity {
	protected final String TAG = "ProductsActivity";

    private ListView productList;
	private ProductAdapter listAdapter;
	private ArrayList<BazaarProduct> products;
	private ProgressDialog progDialog;
	private TextView noResult;

	/**
	 * Queries for products if this is a new instance or retrieves the product
	 * list if this is a recycled instance.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);

		if (savedInstanceState == null) {
			products = new ArrayList<>();
			initializeViews();
			runSearchQuery();
		} else {
			products = savedInstanceState.getParcelableArrayList("products");
			initializeViews();
		}
	}

	/**
	 * Saves the list of products.
	 */
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putParcelableArrayList("products", products);
	}

	/**
	 * Sets up the views and sets an OnItemClickListener on the product list to
	 * launch the next activity.
	 */
	private void initializeViews() {
		productList = (ListView) findViewById(R.id.productList);
		listAdapter = new ProductAdapter(getBaseContext(), products);
		productList.setAdapter(listAdapter);

		productList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aView, View view,
					int position, long id) {
				BazaarProduct selectedProduct = products.get(position);
				Intent intent = new Intent(getBaseContext(),
						ReviewsActivity.class);
				intent.putExtra("product", selectedProduct);
				startActivity(intent);
			}

		});

		progDialog = new ProgressDialog(this);
		noResult = (TextView) findViewById(R.id.noResult);
	}

	/**
	 * Sends off a request to retrieve the product list depending on the user's
	 * search term. When the response comes, it makes a second request for
	 * statistics for the products.
	 */
	private void runSearchQuery() {
		Intent myIntent = getIntent();
		String searchTerm = myIntent.getStringExtra("searchTerm");
		BazaarFunctions.runProductSearchQuery(searchTerm,
				new BazaarUIThreadResponse(this) {

					@Override
					public void onUiResponse(JSONObject response) {

					}

					@Override
					public void onResponse(String url, JSONObject response) {
						Log.i(TAG, "Response = \n" + response);
						try {
							JSONArray results = response.getJSONArray("Results");

							if (results.length() == 0) {
								noResult.setVisibility(View.VISIBLE);
								progDialog.dismiss();
							}

							for (int i = 0; i < results.length(); i++) {
								BazaarProduct newProd = new BazaarProduct(
										results.getJSONObject(i));

								products.add(newProd);
								listAdapter.notifyDataSetChanged();

								// dismiss dialog on last iteration to
								// save another call to
								// runOnUiThread()
								if (products.size() == results.length())
									progDialog.dismiss();

							}
						} catch (JSONException exception) {
							Log.e(TAG, "Error = " + exception.getMessage()
									+ "\n" + Log.getStackTraceString(exception));
						}
					}

				});
		progDialog.setMessage("Loading products...");
		progDialog.show();
	}

}
