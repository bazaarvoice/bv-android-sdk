package com.bazaarvoice.example.browseproducts;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.Equality;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.RequestType;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RelativeLayout;

public class BazaarProduct {
	
	public class Brand {
		private String name;
		private String id;
		
		public Brand(String n, String i){
			name = n;
			id = i;
		}
		
		public Brand(JSONObject brand) throws JSONException{
			name = brand.getString("Name");
			id = brand.getString("Id");
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
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * @param id the id to set
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
	private Bitmap img;
	private String prodId;
	private ArrayList<BazaarReview> reviews;
	private int averageRating;
	
	public BazaarProduct(JSONObject json) throws JSONException{
		name = json.getString("Name");
		brand = new Brand(json.getJSONObject("Brand"));
		description = json.getString("Description");
		catId = json.getString("CategoryId");
		imgUrl = json.getString("ImageUrl");
		prodId = json.getString("Id");
		
		reviews = new ArrayList<BazaarReview>();
	}
	
	public BazaarProduct() {
		reviews = new ArrayList<BazaarReview>();
	}

	public void downloadImage(final OnImageDownloadComplete listener){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				ImageDownloader downloader = new ImageDownloader();
				img = downloader.download(imgUrl);
				listener.onFinish(img);
			}
			
		});
		t.start();
	}
	
	public void downloadReviews(final OnBazaarResponse listener){
		reviews.clear();
		BazaarFunctions.runReviewQuery(prodId, listener);
	}
	
	public void addReview(BazaarReview newReview) {
		reviews.add(newReview);
	}
	
	public void updateAvgRating() {
		int sum = 0;
		int amount = 0;
		for (int i = 0 ; i < reviews.size(); i++){
			if (reviews.get(i).getRating() > 0){
				sum += reviews.get(i).getRating();
				amount++;
			}
		}
		
		if(amount > 0)
			averageRating = sum / amount;
	}
	
	/**
	 * @return the averageRating
	 */
	public int getAverageRating() {
		return averageRating;
	}
	
	/**
	 * @return the reviews
	 */
	public ArrayList<BazaarReview> getReviews(){
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
	public Bitmap getImg() {
		return img;
	}
	
	/**
	 * @return the prodID
	 */
	public String getProdID() {
		return prodId;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param brand the brand to set
	 */
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param catID the catID to set
	 */
	public void setCatID(String catID) {
		this.catId = catID;
	}
	
	/**
	 * @param prodID the prodID to set
	 */
	public void setProdID(String prodID) {
		this.prodId = prodID;
	}
	
	

}
