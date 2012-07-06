package com.bazaarvoice.example.browseproducts;

import java.util.ArrayList;
import java.util.Arrays;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.Equality;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.RequestType;

/**
 * BazaarFunctions.java <br>
 * ReviewSubmissionExample<br>
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
	public static void runProductSearchQuery(String searchPhrase,
			OnBazaarResponse listener) {
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		DisplayParams params = new DisplayParams();

		// Add search terms to params
		String[] tokens = searchPhrase.split("\\s+");
		if (tokens.length > 0) {
			ArrayList<String> searchTerms = new ArrayList<String>(
					Arrays.asList(tokens));
			params.setSearch(searchTerms);
		}

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
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		DisplayParams params = new DisplayParams();

		params.addFilter("ProductId", Equality.EQUAL, prodId);
		params.setLimit(10);

		// false => descending order
		params.addSort("Rating", false);

		OnBazaarResponse response = listener;
		request.sendDisplayRequest(RequestType.REVIEWS, params, response);
	}

	/**
	 * Sends off a statistics query for the given products.
	 * 
	 * <p>
	 * Using "Reviews" returns statistics for all content, including syndicated
	 * content (if enabled on your API key). If you only want statistics for
	 * reviews you own that were written for the products specified, use
	 * "NativeReviews" instead.
	 * 
	 * @param products
	 * @param listener
	 */
	public static void runStatisticsQuery(String[] products,
			OnBazaarResponse listener) {
		if (products.length == 0)
			return;

		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		DisplayParams params = new DisplayParams();

		// Collapse product list into "item1,item2,item3..." format
		StringBuffer sb = new StringBuffer();
		sb.append(products[0]);
		for (int i = 1; i < products.length; i++) {
			sb.append(",");
			sb.append(products[i]);
		}
		String productString = sb.toString();

		params.addFilter("productid", productString);
		params.addStats("Reviews");

		OnBazaarResponse response = listener;
		request.sendDisplayRequest(RequestType.STATISTICS, params, response);
	}

}
