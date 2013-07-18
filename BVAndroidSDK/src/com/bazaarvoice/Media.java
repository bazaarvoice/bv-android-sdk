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

import android.webkit.MimeTypeMap;

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
	private String mimeType;
	
	/**
	 * An enum used for defining the type of media associated with an instance of Media.
	 */
	public static enum MediaType {
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
	public Media(File media, MediaType type, String filename) {
		this.mediaFile = media;
		this.mediaType = type;
		this.filename = filename;
		this.mediaBytes = null;
		
		String[] fileParts = media.getName().split("\\.");
		
		mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileParts[fileParts.length - 1]);
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
	public Media(byte[] media, MediaType type, String filename) {
		this.mediaBytes = media;
		this.mediaType = type;
		this.filename = filename;
		this.mediaFile = null;
		
		String[] fileParts = filename.split("\\.");
		
		mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileParts[fileParts.length - 1]);
	}
	
	/**
	 * Get the name of the media type.
	 * 
	 * @return "photo" or "video"
	 */
	public String getName() {
		return mediaType == MediaType.PHOTO ? "photo" : "video";
	}

	/**
	 * Get the filename of the media object.
	 * 
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Get the File representing the media object.
	 * 
	 * @return the media
	 */
	public File getFile() {
		return mediaFile;
	}
	
	/**
	 * Get the byte array representing the media object.
	 * 
	 * @return the media
	 */
	public byte[] getBytes() {
		return mediaBytes;
	}
	
	/**
	 * Get the MimeType representing the media object.
	 * 
	 * @return the media
	 */
	public String getMimeType() {
		return mimeType;
	}
}
