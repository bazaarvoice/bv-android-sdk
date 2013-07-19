/*******************************************************************************
 * Copyright 2013 Bazaarvoice
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bazaarvoice;

import java.io.File;
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
	 * Convert the class into a url parameters string.
	 * 
	 * @param url
	 *            the base url to append to
	 * @return the url with parameters
	 */
	protected abstract String toURL(String apiVersion, String passKey);
	
	
	/**
	 * Add multipart parameters to the Http request
	 * 
	 * @param name
	 *            the name of the parameter being added
	 * @param val
	 * 				The value of the parameter
	 */
	protected abstract void addPostParameters(String apiVersion, String passKey, BazaarRequest request);
	
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
	
	
	protected void addMultipartParameter(String name, Object val, BazaarRequest request) {
		if (name != null && val != null) {
			String value = val.toString();
			
			String topBoundary = "--" + request.boundary + "\r\n";
			String contentDisp = String.format("Content-Disposition: form-data; name=\"%s\"\r\n\r\n", name);
			String valueParam = String.format("%s\r\n", value);
			
			request.multiPartParams.add(topBoundary);
			request.multiPartParams.add(contentDisp);
			request.multiPartParams.add(valueParam);
			
			request.contentLength = request.contentLength + topBoundary.getBytes().length + contentDisp.getBytes().length + valueParam.getBytes().length;
        	
			request.multipart = true;    
		}
    }
	
	//adding a file object to request
	protected void addMultipartParameter(String name, String fileName, File mediaFile, BazaarRequest request) {
	    
        if ((name != null) && (fileName != null)) {
        	
        	String topBoundary = "--" + request.boundary + "\r\n";
			String contentDisp = String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream\r\n\r\n", 
    				name, fileName);
			String valueParam = "\r\n";
			
			request.mediaParam.add(topBoundary);
			request.mediaParam.add(contentDisp);
			request.mediaParam.add(valueParam);			
			
			request.contentLength = request.contentLength + topBoundary.getBytes().length + contentDisp.getBytes().length + valueParam.getBytes().length + (int) mediaFile.length();
        	
			request.multipart = true;
			request.media = true;
        }
    }
	
	//adding byte array to the request
	protected void addMultipartParameter(String name, String fileName, byte[] mediaFileBytes, BazaarRequest request) {
    	
        if ((name != null) && (fileName != null)) {
        	
        	String topBoundary = "--" + request.boundary + "\r\n";
			String contentDisp = String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: application/octet-stream\r\n\r\n", 
    				name, fileName);
			String valueParam = "\r\n";
			
			request.mediaParam.add(topBoundary);
			request.mediaParam.add(contentDisp);
			request.mediaParam.add(valueParam);			
			
			request.contentLength = request.contentLength + topBoundary.getBytes().length + contentDisp.getBytes().length + valueParam.getBytes().length + mediaFileBytes.length;
        	
			request.multipart = true;
			request.media = true;
        }
    }
	
	
	protected String addURLParameter(String name, Object val) {
		if (val != null) {
			String value = val.toString();
			
			if (value.length() > 0) {
				value = BazaarParams.encode(value);
				return "&" + name + '=' + value;
			}
		}
		return "";
	}	
	
	
	protected String addURLParameter(String name, Map<String, String> map) {
		StringBuilder url = new StringBuilder();
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				url.append(addURLParameter(name + "_" + key, value));
			}
		}
		return url.toString();
	}
	
	
	protected String addURLParameter(String name, List<String> values) {
		if (values != null) {
			StringBuilder paramList = new StringBuilder();
			boolean first = true;
			for (String value : values) {
				if (first) {
					first = false;
				} else {
					paramList.append(",");
				}

				paramList.append(value);
			}
			return addURLParameter(name, paramList);
		}
		return "";
	}

	
	protected String addNthURLParamsFromList(String name, List<String> values) {
		StringBuilder url = new StringBuilder();
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				// Start with param_1
				int key = i + 1;
				String value = values.get(i);

				url.append(addURLParameter(name + "_" + key, value));
			}
		}
		return url.toString();
	}

}
