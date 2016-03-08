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
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 *
 * @author Bazaarvoice Engineering
 *
 */

package com.example.bazaarvoice.bv_android_sdk.conversations.browseproducts;

import com.bazaarvoice.bvandroidsdk.BazaarRequest;
import com.bazaarvoice.bvandroidsdk.DisplayParams;
import com.bazaarvoice.bvandroidsdk.OnBazaarResponse;
import com.bazaarvoice.bvandroidsdk.types.Equality;
import com.bazaarvoice.bvandroidsdk.types.IncludeStatsType;
import com.bazaarvoice.bvandroidsdk.types.RequestType;

/**
 * TODO: Description Here
 */
public class BazaarFunctions{

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

		BazaarRequest request = new BazaarRequest();
		DisplayParams params = new DisplayParams();

		if (!"".equals(searchPhrase.trim())) {
			// Add search terms to params
			String[] tokens = searchPhrase.split("\\s+");
			for (String term : tokens) {
				params.setSearch(term);
			}
		}

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

		BazaarRequest request = new BazaarRequest();
		DisplayParams params = new DisplayParams();

		params.addFilter("ProductId", Equality.EQUAL, prodId);
		params.setLimit(10);

		// false => descending order
		params.addSort("Rating", false);

		OnBazaarResponse response = listener;
		request.sendDisplayRequest(RequestType.REVIEWS, params, response);
	}

}
