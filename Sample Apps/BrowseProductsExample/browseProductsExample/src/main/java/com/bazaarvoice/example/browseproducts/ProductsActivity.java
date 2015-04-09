package com.bazaarvoice.example.browseproducts;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
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
			products = new ArrayList<BazaarProduct>();
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
					public void onUiResponse(JSONObject json) {
						Log.i(TAG, "Response = \n" + json);
						try {
							JSONArray results = json.getJSONArray("Results");

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
