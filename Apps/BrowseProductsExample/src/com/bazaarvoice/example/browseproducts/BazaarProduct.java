package com.bazaarvoice.example.browseproducts;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.bazaarvoice.OnBazaarResponse;

/**
 * BazaarProduct.java <br>
 * ReviewSubmissionExample<br>
 * 
 * <p>
 * This is a very basic class used to represent a product in the Bazaarvoice
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
public class BazaarProduct implements Parcelable {

	/**
	 * Brand
	 * 
	 * <p>
	 * This is a simple class that represents the brand of a BazaarProduct.
	 * 
	 * <p>
	 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
	 * 
	 * @author Bazaarvoice Engineering
	 */
	public class Brand {
		private String name;
		private String id;

		/**
		 * Creates a brand from a name and an ID.
		 * 
		 * @param n
		 *            the name
		 * @param i
		 *            the id
		 */
		public Brand(String n, String i) {
			name = n;
			id = i;
		}

		/**
		 * Creates a brand from a json object representing a brand.
		 * 
		 * @param brand
		 *            the json object representing a brand
		 * @throws JSONException
		 */
		public Brand(JSONObject brand) {
			try {
				name = brand.getString("Name");
			} catch (JSONException e) {
				Log.d("Parsing Error", "No JSON Value for name");
			}
			try {
				id = brand.getString("Id");
			} catch (JSONException e) {
				Log.d("Parsing Error", "No JSON Value for id");
			}
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
	}

	protected String TAG = "BazaarProduct";

	private String name;
	private Brand brand;
	private String description;
	private String catId;
	private String imgUrl;
	private Bitmap imageBitmap;
	private String prodId;
	private ArrayList<BazaarReview> reviews;
	private double averageRating;
	private int numReviews;

	/**
	 * Creates a BazaarProduct from a json object representing a product.
	 * 
	 * @param product
	 *            the json object representing a product
	 * @throws JSONException
	 */
	public BazaarProduct(JSONObject product) throws JSONException {
		name = product.getString("Name");
		brand = new Brand(product.getJSONObject("Brand"));
		description = product.getString("Description").replaceAll("  ", "").replaceAll("\n", "");
		catId = product.getString("CategoryId");
		imgUrl = product.getString("ImageUrl");
		prodId = product.getString("Id");
		extractStatistics(product);
		reviews = new ArrayList<BazaarReview>();
	}

	/**
	 * Creates an empty BazaarProduct, but instantiates the review list.
	 */
	public BazaarProduct() {
		reviews = new ArrayList<BazaarReview>();
	}

	/**
	 * Pulls statistics information out of the json product result only if they
	 * exist.
	 * 
	 * @param product
	 *            the json object representing a product
	 * @throws JSONException
	 *             if a parameter is missing
	 */
	private void extractStatistics(JSONObject product) throws JSONException {
		JSONObject reviewStats = product.optJSONObject("ReviewStatistics");
		if (reviewStats != null) {
			setAverageRating(reviewStats.optDouble("AverageOverallRating"));
			numReviews = reviewStats.getInt("TotalReviewCount");
		}
	}

	/**
	 * Downloads the product image and internalizes it.
	 * 
	 * @param listener
	 *            a callback for when the download completes
	 */
	public void downloadImage(final OnImageDownloadComplete listener) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ImageDownloader downloader = new ImageDownloader();
				imageBitmap = downloader.download(imgUrl);
				listener.onFinish(imageBitmap);
			}

		});
		t.start();
	}

	/**
	 * Downloads the reviews for this product. The responsibility of storing the
	 * reviews is on the caller.
	 * 
	 * @param listener
	 *            a callback for when the response is received
	 */
	public void downloadReviews(OnBazaarResponse listener) {
		reviews.clear();
		BazaarFunctions.runReviewQuery(prodId, listener);
	}

	/**
	 * Adds a review to the product's review list
	 * 
	 * @param newReview
	 *            the review to add
	 */
	public void addReview(BazaarReview newReview) {
		reviews.add(newReview);
	}

	/**
	 * @return the averageRating
	 */
	public double getAverageRating() {
		return averageRating;
	}

	/**
	 * @return the reviews
	 */
	public ArrayList<BazaarReview> getReviews() {
		return reviews;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the brand
	 */
	public Brand getBrand() {
		return brand;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the catID
	 */
	public String getCatID() {
		return catId;
	}

	/**
	 * @return the img
	 */
	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	/**
	 * @return the prodID
	 */
	public String getProdID() {
		return prodId;
	}

	/**
	 * @return the numReviews
	 */
	public int getNumReviews() {
		return numReviews;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param catID
	 *            the catID to set
	 */
	public void setCatID(String catID) {
		this.catId = catID;
	}

	/**
	 * @param prodID
	 *            the prodID to set
	 */
	public void setProdID(String prodID) {
		this.prodId = prodID;
	}

	/**
	 * @param numReviews
	 *            the numReviews to set
	 */
	public void setNumReviews(int numReviews) {
		this.numReviews = numReviews;
	}

	/**
	 * Handles NaN values correctly.
	 * 
	 * @param averageRating
	 *            the averageRating to set
	 */
	public void setAverageRating(double averageRating) {
		if (Double.isNaN(averageRating))
			this.averageRating = -1;
		else
			this.averageRating = averageRating;
	}

	/*
	 * Parcel methods and constructors
	 */

	/**
	 * Constructs a BazaarProduct from a parcel that has another instance of
	 * this class in it.
	 * 
	 * @param parcel
	 *            the parcel holding an instance of BazaarProduct
	 */
	private BazaarProduct(Parcel parcel) {
		readFromParcel(parcel);
	}

	/**
	 * A Creator object used by the android system when unpacking a parcelled
	 * instance of a BazaarProduct object.
	 */
	public static final Parcelable.Creator<BazaarProduct> CREATOR = new Parcelable.Creator<BazaarProduct>() {

		@Override
		public BazaarProduct createFromParcel(Parcel source) {
			return new BazaarProduct(source);
		}

		@Override
		public BazaarProduct[] newArray(int size) {
			return new BazaarProduct[size];
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
		dest.writeString(brand.getName());
		dest.writeString(brand.getId());
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(catId);
		dest.writeString(imgUrl);
		dest.writeString(prodId);
		dest.writeDouble(averageRating);
		dest.writeInt(numReviews);
		dest.writeList(reviews);

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
	 * 
	 * @param parcel
	 */
	public void readFromParcel(Parcel parcel) {
		String brandName = parcel.readString();
		String brandId = parcel.readString();
		brand = new Brand(brandName, brandId);
		name = parcel.readString();
		description = parcel.readString();
		catId = parcel.readString();
		imgUrl = parcel.readString();
		prodId = parcel.readString();
		averageRating = parcel.readDouble();
		numReviews = parcel.readInt();
		reviews = new ArrayList<BazaarReview>();
		parcel.readList(reviews, BazaarReview.class.getClassLoader());

		byte[] byteArray = parcel.createByteArray();
		if (byteArray.length == 0) {
			imageBitmap = null;
		} else {
			imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);
		}
	}

}
