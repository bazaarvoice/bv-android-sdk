package com.bazaarvoice.example.browseproducts;

import java.util.ArrayList;
import java.util.Arrays;

import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.DisplayParams;
import com.bazaarvoice.Equality;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.RequestType;
import com.bazaarvoice.example.browseproducts.ProductViewer.OnBazaarProductResponse;

public class BazaarFunctions {
	
	/*
	 * OnBazaarProductResponse must live in ProductViewer because it makes
	 * changes to the UI. So to use it, we must have a reference.
	 */
	private static ProductViewer viewer;
	
	public static final String API_URL = "reviews.apitestcustomer.bazaarvoice.com/bvstaging";
	public static final String API_KEY = "kuy3zj9pr3n7i0wxajrzj04xo";
	public static final String API_VERSION = "5.1";
	
	public static void setViewer(ProductViewer pv){
		viewer = pv;
	}
	
	public static void runProductSearchQuery(String searchPhrase, String sortType, boolean ascending) {
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		DisplayParams params = new DisplayParams();
		
		//Add search terms to params
		String[] tokens = searchPhrase.split("\\s+");
		if(tokens.length > 0){
			ArrayList<String> searchTerms = new ArrayList<String>(Arrays.asList(tokens));
			params.setSearch(searchTerms);
		}
		
		//Add sort to params if applicable
		if (!"".equals(sortType)){
			params.addSort(sortType, ascending);
		}
		
		OnBazaarProductResponse response = viewer.new OnBazaarProductResponse();
		request.sendDisplayRequest(RequestType.PRODUCTS, params, response);
	}
	
	public static void runReviewQuery(String prodId, OnBazaarResponse listener){
		BazaarRequest request = new BazaarRequest(API_URL, API_KEY, API_VERSION);
		DisplayParams params = new DisplayParams();
		
		params.addFilter("ProductId", Equality.EQUAL, prodId);
		params.setLimit(15);
		
		//false => descending order
		params.addSort("Rating", false);
		
		OnBazaarResponse response = listener;
		request.sendDisplayRequest(RequestType.REVIEWS, params, response);
	}

}
