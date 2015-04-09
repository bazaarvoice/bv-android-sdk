package com.bazaarvoice.example.reviewsubmission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * BazaarReview.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is a very basic class used to represent a review in the Bazaarvoice
 * system for the use of this example app.
 * 
 * <p>
 * <b>Note: This does not include all functionality of the Bazaarvoice
 * system.</b>
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class BazaarReview {
	private int rating;
	private String title;
	private String authorId;
	private String userNickname;
	private String dateString;
	private String reviewText;
	private String imageUrl;
	private Bitmap imageBitmap;

	/**
	 * Sets all variables to 0, null, or "null"
	 */
	public BazaarReview() {
		rating = 0;
		title = "null";
		authorId = "null";
		userNickname = "null";
		dateString = "null";
		reviewText = "null";
		imageUrl = "null";
		imageBitmap = null;
	}

	/**
	 * Parses the json response of a review query and builds the structure of
	 * the object.
	 * 
	 * @param json
	 *            the response of a review query
	 * @throws NumberFormatException
	 *             if the rating is not formatted as an <code>int</code> (this
	 *             should never occur)
	 * @throws JSONException
	 *             if there is a missing field in the json response
	 */
	public BazaarReview(JSONObject json) throws NumberFormatException,
			JSONException {
		String ratingText = json.getString("Rating");
		if (!"null".equals(ratingText))
			rating = Integer.parseInt(ratingText);
		else
			rating = 0;

		title = json.getString("Title");
		authorId = json.optString("AuthorId");
		userNickname = json.optString("UserNickname");
		dateString = formatDateString(json.getString("SubmissionTime"));
		reviewText = json.getString("ReviewText");

		JSONArray photos = json.optJSONArray("Photos");
		if (photos != null && photos.length() != 0) {
			Log.i("Photo", photos.toString());
			JSONObject photo = photos.getJSONObject(0);
			JSONObject sizes = photo.getJSONObject("Sizes");
			JSONObject thumb = sizes.getJSONObject("thumbnail");
			imageUrl = thumb.getString("Url");
		} else {
			imageUrl = "";
		}
		imageBitmap = null;
	}

	/**
	 * Formats the date string from a json response into a readable format.
	 * 
	 * <p>
	 * Assumes "yyyy-mm-ddThh:mm:ss.000-06:00" format
	 * 
	 * @param timestamp
	 *            the timestamp string from the json response
	 * @return the formatted string
	 */
	private String formatDateString(String timestamp) {
		String year = timestamp.substring(0, 4);
		int monthNum = Integer.parseInt(timestamp.substring(5, 7));
		String day = timestamp.substring(8, 10);
		String month = "";

		switch (monthNum) {
		case 1:
			month = "January";
			break;
		case 2:
			month = "February";
			break;
		case 3:
			month = "March";
			break;
		case 4:
			month = "April";
			break;
		case 5:
			month = "May";
			break;
		case 6:
			month = "June";
			break;
		case 7:
			month = "July";
			break;
		case 8:
			month = "August";
			break;
		case 9:
			month = "September";
			break;
		case 10:
			month = "October";
			break;
		case 11:
			month = "November";
			break;
		case 12:
			month = "December";
			break;
		}

		return month + " " + day + ", " + year;

	}

	/**
	 * Downloads the image from the URL stored in the object. This can be set by
	 * constructing the object with a json response that has an image associated
	 * with it. It also allows for passing a callback function for when the download
	 * completes.
	 * 
	 * @param listener the callback function, or null if not needed
	 */
	public void downloadImage(final OnImageDownloadComplete listener) {
		if ("".equals(imageUrl))
			return;

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ImageDownloader downloader = new ImageDownloader();
				imageBitmap = downloader.download(imageUrl);
				if(listener != null)
					listener.onFinish(null);
			}

		});
		t.start();
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the authorId
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * @param authorId
	 *            the authorId to set
	 */
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * @return the dateString
	 */
	public String getDateString() {
		return dateString;
	}

	/**
	 * @param dateString
	 *            the dateString to set
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	/**
	 * @return the reviewText
	 */
	public String getReviewText() {
		return reviewText;
	}

	/**
	 * @param reviewText
	 *            the reviewText to set
	 */
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	/**
	 * @return the imageBitmap
	 */
	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	/**
	 * @param imageBitmap
	 *            the imageBitmap to set
	 */
	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	/**
	 * @return the userNickname
	 */
	public String getNickname() {
		return userNickname;
	}

	/**
	 * @param userNickname
	 *            the userNickname to set
	 */
	public void setNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	/**
	 * Gets the correct name for display.
	 * 
	 * @return nickname if it exists, else the author ID
	 */
	public String getDisplayName() {
		if (userNickname == "")
			return authorId;
		else
			return userNickname;
	}

}
