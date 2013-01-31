package com.bazaarvoice.example.browseproducts;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * ProductAdapter.java <br>
 * BrowseProductExample<br>
 * 
 * <p>
 * This is an adapter tailored for displaying BazaarProduct objects
 * 
 * <p>
 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
class ProductAdapter extends BaseAdapter {

	private final int RATING_BAR = 0;
	private final int NO_RATING = 1;

	private ArrayList<BazaarProduct> products;
	private LayoutInflater inflater;
	private Context context;

	/**
	 * Internalizes the list of products and sets up the adapter.
	 * 
	 * @param c
	 *            the application context
	 * @param reviewList
	 *            the list of products
	 */
	public ProductAdapter(Context c, ArrayList<BazaarProduct> productList) {
		context = c;
		products = productList;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 */
	@Override
	public int getCount() {
		return products.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 */
	@Override
	public Object getItem(int position) {
		return products.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 * 
	 * <p>
	 * Not implemented.
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * Get a View that displays the data at the specified position in the data
	 * set.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.product_item, null);
			holder = new ViewHolder();
			holder.productTitle = (TextView) convertView
					.findViewById(R.id.productTitle);
			holder.productDescription = (TextView) convertView
					.findViewById(R.id.productDescription);
			holder.ratingSwitcher = (ViewSwitcher) convertView
					.findViewById(R.id.ratingSwitcher);
			holder.productRating = (RatingBar) convertView
					.findViewById(R.id.productRating);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.productTitle.setText(products.get(position).getName());

		if (products.get(position).getAverageRating() == -1.0) {
			holder.ratingSwitcher.setDisplayedChild(NO_RATING);
		} else {
			holder.ratingSwitcher.setDisplayedChild(RATING_BAR);
			holder.productRating.setRating((float) products.get(position)
					.getAverageRating());
		}

		holder.productDescription.setText(products.get(position)
				.getDescription());

		return convertView;
	}

	/**
	 * ViewHolder
	 * 
	 * <p>
	 * Holds the Views that are contained in an item of the list.
	 * 
	 * <p>
	 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
	 * 
	 * @author Bazaarvoice Engineering
	 */
	class ViewHolder {
		TextView productTitle;
		ViewSwitcher ratingSwitcher;
		RatingBar productRating;
		TextView productDescription;
	}

}
