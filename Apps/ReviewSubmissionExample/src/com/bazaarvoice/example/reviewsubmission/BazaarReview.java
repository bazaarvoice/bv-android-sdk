package com.bazaarvoice.example.reviewsubmission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

public class BazaarReview {
	private int rating;
	private String title;
	private String authorId;
	private String userNickname;
	private String dateString;
	private String reviewText;
	private String imageUrl;
	private Bitmap imageBitmap;
	
	public BazaarReview(){
		rating = 0;
		title = "null";
		authorId= "null";
		userNickname = "null";
		dateString = "null";
		reviewText = "null";
		imageUrl = "null";
		imageBitmap = null;
	}
	
	public BazaarReview(JSONObject json) throws NumberFormatException, JSONException {
		String ratingText = json.getString("Rating");
		if(!"null".equals(ratingText))
			rating = Integer.parseInt(ratingText);
		else
			rating = 0;
		
		title = json.getString("Title");
		authorId = json.optString("AuthorId");
		userNickname = json.optString("UserNickname");
		dateString = formatDateString(json.getString("SubmissionTime"));
		reviewText = json.getString("ReviewText");
		
		JSONArray photos = json.optJSONArray("Photos");
		if (photos != null && photos.length() != 0){
			Log.i("Photo", photos.toString());
			JSONObject photo = photos.getJSONObject(0);
			JSONObject sizes = photo.getJSONObject("Sizes");
			JSONObject thumb = sizes.getJSONObject("thumbnail");
			imageUrl = thumb.getString("Url");
		}
		else{
			imageUrl = "";
		}
		imageBitmap = null;
	}

	private String formatDateString(String timestamp) {
		//assuming "2011-12-09T08:55:35.000-06:00" format
		Log.i("BazaarReview", timestamp);
		String year = timestamp.substring(0, 4);
		int monthNum = Integer.parseInt(timestamp.substring(5, 7));
		String day = timestamp.substring(8, 10);
		String month = "";
		
		switch(monthNum){
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
	
	public void downloadImage(final OnImageDownloadComplete listener){
		if("".equals(imageUrl))
			return;
		
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				ImageDownloader downloader = new ImageDownloader();
				imageBitmap = downloader.download(imageUrl);
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
	 * @param rating the rating to set
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
	 * @param title the title to set
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
	 * @param authorId the authorId to set
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
	 * @param dateString the dateString to set
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
	 * @param reviewText the reviewText to set
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
	 * @param imageBitmap the imageBitmap to set
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
	 * @param userNickname the userNickname to set
	 */
	public void setNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	
	/**
	 * Gets the correct name for display.
	 * 
	 * @return nickname if it exists, else the author ID
	 */
	public String getDisplayName(){
		if (userNickname == "")
			return authorId;
		else
			return userNickname;
	}
	
	
}
