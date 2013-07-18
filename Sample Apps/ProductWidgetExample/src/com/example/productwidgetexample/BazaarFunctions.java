package com.example.productwidgetexample;

import com.bazaarvoice.types.*;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.OnBazaarResponse;

/**
 * BazaarFunctions.java <br>
 * ProductWidgetExample<br>
 * 
 * <p>
 * This is a suite of functions that leverage the BazaarvoiceSDK. This class
 * consolidates the usage of these functions for easier understanding of how to
 * use the SDK.
 * 
 * <p>
 * Created on 6/29/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 * 
 */
public class BazaarFunctions {

	public static final String API_URL = "reviews.apitestcustomer.bazaarvoice.com/bvstaging";
	public static final String API_KEY = "kuy3zj9pr3n7i0wxajrzj04xo";
	public static final String API_VERSION = "5.1";

	/**
	 * Sends off a product query with the search term provided.
	 * 
	 * @param searchPhrase
	 *            the search term(s)
	 * @param listener
	 *            the response listener
	 */
	public static void runProductQuery(OnBazaarResponse listener) {
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, ApiVersion.FIVE_FOUR);
		DisplayParams params = new DisplayParams();

		params.addSort("AverageOverallRating", false);
		params.addStats(IncludeStatsType.REVIEWS);

		request.sendDisplayRequest(RequestType.PRODUCTS, params, listener);
	}

	/**
	 * Sends off a reiview query for the given product Id. It sorts the results
	 * by rating from highest to lowest.
	 * 
	 * @param prodId
	 *            the product id
	 * @param listener
	 *            the response listener
	 */
	public static void runReviewQuery(String prodId, OnBazaarResponse listener) {
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, ApiVersion.FIVE_FOUR);
		DisplayParams params = new DisplayParams();

		params.addFilter("ProductId", Equality.EQUAL, prodId);
		params.setLimit(10);

		// false => descending order
		params.addSort("Rating", false);

		OnBazaarResponse response = listener;
		request.sendDisplayRequest(RequestType.REVIEWS, params, response);
	}

}
