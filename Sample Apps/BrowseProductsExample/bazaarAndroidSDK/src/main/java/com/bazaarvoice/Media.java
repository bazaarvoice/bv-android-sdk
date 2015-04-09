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

/**
 * 
 * Contains byte data for a media object such as a photo or video; used internally for uploading media in a request.
 */
public class Media {
	
	//private static final String TAG = "Media";
	
	private byte[] mediaBytes;
	private File mediaFile;
	private MediaType mediaType;
	private String filename;
	
	/**
	 * An enum used for defining the type of media associated with an instance of Media.
	 */
	protected static enum MediaType {
		PHOTO, VIDEO
	}

	/**
	 * Construct a media object
	 * 
	 * @param media
	 *            the media data
	 * @param type
	 *            the type of object
	 * @param filename
	 *            the filename of the object. This is important for determining
	 *            the MIME type. Make sure you use the right extension for the
	 *            data, ie .jpg .avi etc
	 */
	protected Media(File media, MediaType type, String filename) {
		this.mediaFile = media;
		this.mediaType = type;
		this.filename = filename;
		this.mediaBytes = null;
	}

	/**
	 * Construct a media object
	 * 
	 * @param media
	 *            the media data
	 * @param type
	 *            the type of object
	 * @param filename
	 *            the filename of the object. This is important for determining
	 *            the MIME type. Make sure you use the right extension for the
	 *            data, ie .jpg .avi etc
	 */
	protected Media(byte[] media, MediaType type, String filename) {
		this.mediaBytes = media;
		this.mediaType = type;
		this.filename = filename;
		this.mediaFile = null;
	}
	
	/**
	 * Get the name of the media type.
	 * 
	 * @return "photo" or "video"
	 */
	protected String getName() {
		return mediaType == MediaType.PHOTO ? "photo" : "video";
	}

	/**
	 * Get the filename of the media object.
	 * 
	 * @return the filename
	 */
	protected String getFilename() {
		return filename;
	}

	/**
	 * Get the File representing the media object.
	 * 
	 * @return the media
	 */
	protected File getFile() {
		return mediaFile;
	}
	
	/**
	 * Get the byte array representing the media object.
	 * 
	 * @return the media
	 */
	protected byte[] getBytes() {
		return mediaBytes;
	}
}
