package com.bazaarvoice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * The base class for adding parameters to a BazaarRequest. This
 * class should not be used to add parameters. Instead, use one of the derived
 * classes: {@link DisplayParams}, {@link SubmissionParams}, {@link SubmissionMediaParams}.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public abstract class BazaarParams {

	protected Media media;
	
	/**
	 * Get the media file associated with these parameters.
	 * 
	 * @return the media file or null if there is none
	 */
	public Media getMedia() {
		return media;
	}
	
	
	/**
	 * URL encode the value
	 * 
	 * @param value
	 *            the value to encode
	 * @return encoded value
	 */
	@SuppressWarnings("deprecation")
	protected static String encode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return URLEncoder.encode(value);
		}
	}
	
	
	/**
	 * Helper method for ensuring a map exists and adding a value to it.
	 * 
	 * @param map
	 *            the map
	 * @param type
	 *            the key
	 * @param value
	 *            the value
	 * @return the original map if map is not null, a new map otherwise
	 */
	protected Map<String, String> addToMap(Map<String, String> map,
			String type, String value) {
		if (map == null) {
			map = new HashMap<String, String>();
		}

		String oldValue = map.get(type);
		if (oldValue != null) {
			value = oldValue + ":" + value;
		}
		map.put(type, value);
		return map;
	}

}
