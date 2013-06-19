package com.bazaarvoice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
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
	 * Convert the class into a url parameters string.
	 * 
	 * @param url
	 *            the base url to append to
	 * @return the url with parameters
	 */
	public abstract String toURL(String url);

	/**
	 * Get the media file associated with these parameters.
	 * 
	 * @return the media file or null if there is none
	 */
	public Media getMedia() {
		return media;
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

	/**
	 * Add a parameter to the current url.
	 * 
	 * <p>
	 * <b>Usage:</b><br>
	 * This is called internally by subclasses of BazaarParams and by
	 * BazaarRequest. This may be able to be used to add additional parameters,
	 * but is not recommended. For additional fields, see
	 * {@link SubmissionParams#addAdditionalField
	 * SubmissionParams.addAdditionalField()}.
	 * 
	 * @param url
	 *            the current url with or without parameters
	 * @param name
	 *            the name of the parameter to add
	 * @param value
	 *            the value
	 * @return the url with the parameter added
	 */
	public static String addURLParameter(String url, String name, String value) {
		if (value != null && value.length() > 0) {
			value = encode(value);
			char separator = url.contains("?") ? '&' : '?';
			url += separator + name + '=' + value;
		}
		return url;
	}

	/**
	 * Add a boolean parameter to the current url as long as the value is not
	 * null.
	 * 
	 * <p>
	 * <b>Usage:</b><br>
	 * This is called internally by subclasses of BazaarParams and by
	 * BazaarRequest. This may be able to be used to add additional parameters,
	 * but is not recommended. For additional fields, see
	 * {@link SubmissionParams#addAdditionalField
	 * SubmissionParams.addAdditionalField()}.
	 * 
	 * @param url
	 *            the current url with or without parameters
	 * @param name
	 *            the name of the parameter to add
	 * @param value
	 *            the value
	 * @return the url with the parameter added
	 */
	public static String addURLParameter(String url, String name, Boolean value) {
		if (value != null) {
			return addURLParameter(url, name, value.toString());
		}
		return url;
	}

	/**
	 * Add an integer parameter to the current url as long as the value is not
	 * null.
	 * 
	 * <p><b>Usage:</b><br> This is called internally by subclasses of BazaarParams and by
	 *        BazaarRequest. This may be able to be used to add additional
	 *        parameters, but is not recommended. For additional fields, see
	 *        {@link SubmissionParams#addAdditionalField
	 *        SubmissionParams.addAdditionalField()}.
	 * 
	 * @param url
	 *            the current url with or without parameters
	 * @param name
	 *            the name of the parameter to add
	 * @param value
	 *            the value
	 * @return the url with the parameter added
	 */
	public static String addURLParameter(String url, String name, Integer value) {
		if (value != null) {
			return addURLParameter(url, name, value.toString());
		}
		return url;
	}

	/**
	 * Add a map of parameters to the current url in the form
	 * "name_key1=value1&name_key2=value2".
	 * 
	 * <p><b>Usage:</b><br> This is called internally by subclasses of BazaarParams and by
	 *        BazaarRequest. This may be able to be used to add additional
	 *        parameters, but is not recommended. For additional fields, see
	 *        {@link SubmissionParams#addAdditionalField
	 *        SubmissionParams.addAdditionalField()}.
	 * 
	 * @param url
	 *            the current url with or without parameters
	 * @param name
	 *            the name of the parameter to add
	 * @param map
	 *            the map of values to add
	 * @return the url with the parameter added
	 */
	public static String addURLParameter(String url, String name,
			Map<String, String> map) {
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				url = addURLParameter(url, name + "_" + key, value);
			}
		}
		return url;
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
	 * Add a list parameters to the url from a list in the form
	 * "name=value1,value2,value3".
	 * 
	 * <p><b>Usage:</b><br> This is called internally by subclasses of BazaarParams and by
	 *        BazaarRequest. This may be able to be used to add additional
	 *        parameters, but is not recommended. For additional fields, see
	 *        {@link SubmissionParams#addAdditionalField
	 *        SubmissionParams.addAdditionalField()}.
	 * 
	 * @param url
	 *            the current url with or without parameters
	 * @param name
	 *            the name of the parameter to add
	 * @param values
	 *            the list of values that will be separated by commas
	 * @return the new url
	 */
	public static String addURLParamsFromList(String url, String name,
			List<String> values) {
		if (values != null) {
			String paramList = "";
			boolean first = true;
			for (String value : values) {
				if (first) {
					first = false;
				} else {
					paramList += ",";
				}

				paramList += value;
			}
			return addURLParameter(url, name, paramList);
		}
		return url;
	}

	/**
	 * Add a list parameters to the url from a list in the form
	 * "name_1=value1&name_2=value2&name_3=value3".
	 * 
	 * <p><b>Usage:</b><br> This is called internally by subclasses of BazaarParams and by
	 *        BazaarRequest. This may be able to be used to add additional
	 *        parameters, but is not recommended. For additional fields, see
	 *        {@link SubmissionParams#addAdditionalField
	 *        SubmissionParams.addAdditionalField()}.
	 * 
	 * @param url
	 *            the current url with or without parameters
	 * @param name
	 *            the name of the parameter to add
	 * @param values
	 *            the list of values that will be separated by commas
	 * @return the new url
	 */
	public static String addNthURLParamsFromList(String url, String name,
			List<String> values) {
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				// Start with param_1
				int key = i + 1;
				String value = values.get(i);

				url = addURLParameter(url, name + "_" + key, value);
			}
		}
		return url;
	}

}
