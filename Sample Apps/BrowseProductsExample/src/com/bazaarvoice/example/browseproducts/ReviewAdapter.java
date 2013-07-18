package com.bazaarvoice.example.browseproducts;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * ReviewAdapter.java <br>
 * BrowseProductExample<br>
 * 
 * <p>
 * This is an adapter tailored for displaying BazaarReview objects
 * 
 * <p>
 * Created on 7/3/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class ReviewAdapter extends BaseAdapter {

	private ArrayList<BazaarReview> reviews;
	private LayoutInflater inflater;
	private Context context;

	/**
	 * Internalizes the list of reviews and sets up the adapter.
	 * 
	 * @param c
	 *            the application context
	 * @param reviewList
	 *            the list of reviews
	 */
	public ReviewAdapter(Context c, ArrayList<BazaarReview> reviewList) {
		context = c;
		reviews = reviewList;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 */
	@Override
	public int getCount() {
		return reviews.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 */
	@Override
	public Object getItem(int position) {
		return reviews.get(position);
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
			convertView = inflater.inflate(R.layout.review_item, null);
			holder = new ViewHolder();
			holder.reviewImage = (ImageView) convertView
					.findViewById(R.id.reviewImage);
			holder.reviewTitle = (TextView) convertView
					.findViewById(R.id.reviewTitle);
			holder.byLine = (TextView) convertView.findViewById(R.id.byLine);
			holder.reviewRating = (RatingBar) convertView
					.findViewById(R.id.reviewRating);
			holder.reviewText = (TextView) convertView
					.findViewById(R.id.reviewText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (reviews.get(position).getImageBitmap() != null) {
			Bitmap scaledImage = Bitmap.createScaledBitmap(reviews
					.get(position).getImageBitmap(), 150, 150, true);
			holder.reviewImage.setImageBitmap(scaledImage);
		} else
			holder.reviewImage.setImageResource(R.drawable.camera_icon);

		holder.reviewTitle.setText(reviews.get(position).getTitle());
		holder.byLine.setText(Html.fromHtml("By <b>"
				+ reviews.get(position).getAuthorId() + "</b> on "
				+ reviews.get(position).getDateString()));
		holder.reviewRating.setRating(reviews.get(position).getRating());
		holder.reviewText.setText(reviews.get(position).getReviewText());

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
		ImageView reviewImage;
		TextView reviewTitle;
		TextView byLine;
		RatingBar reviewRating;
		TextView reviewText;
	}
}
