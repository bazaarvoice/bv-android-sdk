package com.bazaarvoice.example.browseproducts;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * BazaarReview.java <br>
 * ReviewSubmissionExample<br>
 * 
 * <p>
 * This is a very basic class used to represent a review in the Bazaarvoice
 * system for the use of this example app.
 * 
 * When fetching images, this chooses the thumbnail over the normal size. This
 * results in blurry images when blown up but it reduces download times and
 * memory usage. You might want to try using normal sized images and see what
 * the usage is like.
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
public class BazaarReview implements Parcelable {
	private int rating;
	private String title;
	private String authorId;
	private String dateString;
	private String reviewText;
	private String imageUrl;
	private Bitmap imageBitmap;

	/**
	 * Parses the json response of a review query and builds the structure of
	 * the object.
	 * 
	 * @param review
	 *            a json object representing a review
	 * @throws NumberFormatException
	 *             if the rating is not formatted as an <code>int</code> (this
	 *             should never occur)
	 * @throws JSONException
	 *             if there is a missing field in the json response
	 */
	public BazaarReview(JSONObject review) throws NumberFormatException,
			JSONException {
		String ratingText = review.getString("Rating");
		if (!"null".equals(ratingText))
			rating = Integer.parseInt(ratingText);
		else
			rating = 0;

		title = review.getString("Title");
		authorId = review.getString("AuthorId");
		dateString = formatDateString(review.getString("SubmissionTime"));
		reviewText = review.getString("ReviewText");

		JSONArray photos = review.getJSONArray("Photos");
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
		// assuming "2011-12-09T08:55:35.000-06:00" format
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
	 * with it. It also allows for passing a callback function for when the
	 * download completes.
	 * 
	 * @param listener
	 *            the callback function, or null if not needed
	 */
	public boolean downloadImage(final OnImageDownloadComplete listener) {
		if ("".equals(imageUrl))
			return false;

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ImageDownloader downloader = new ImageDownloader();
				imageBitmap = downloader.download(imageUrl);
				// imageBitmap = Bitmap.createScaledBitmap(temp, 150, 150,
				// true);
				if (listener != null)
					listener.onFinish(imageBitmap);
			}

		});
		t.start();
		return true;
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

	/*
	 * Parcel methods and constructors
	 */

	/**
	 * Constructs a BazaarReview from a parcel that has another instance of this
	 * class in it.
	 * 
	 * @param parcel the parcel holding an instance of BazaarReview
	 */
	private BazaarReview(Parcel parcel) {
		readFromParcel(parcel);
	}

	/**
	 * A Creator object used by the android system when unpacking a parcelled
	 * instance of a BazaarReview object.
	 */
	public static final Parcelable.Creator<BazaarReview> CREATOR = new Parcelable.Creator<BazaarReview>() {

		@Override
		public BazaarReview createFromParcel(Parcel source) {
			return new BazaarReview(source);
		}

		@Override
		public BazaarReview[] newArray(int size) {
			return new BazaarReview[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Writes the object to a parcel. It writes the image as a byte array, or an
	 * empty one if it is null.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(rating);
		dest.writeString(title);
		dest.writeString(authorId);
		dest.writeString(dateString);
		dest.writeString(reviewText);
		dest.writeString(imageUrl);

		if (imageBitmap != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			dest.writeByteArray(byteArray);
		} else {
			dest.writeByteArray(new byte[0]);
		}
	}

	/**
	 * Reads the fields of the parcelled object into this instance.
	 * 
	 * <b>Note: </b>The fields must be read in the same order they were written.
	 * @param parcel
	 */
	public void readFromParcel(Parcel parcel) {
		rating = parcel.readInt();
		title = parcel.readString();
		authorId = parcel.readString();
		dateString = parcel.readString();
		reviewText = parcel.readString();
		imageUrl = parcel.readString();

		byte[] byteArray = parcel.createByteArray();
		if (byteArray.length == 0) {
			imageBitmap = null;
		} else {
			imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);
		}
	}

}
