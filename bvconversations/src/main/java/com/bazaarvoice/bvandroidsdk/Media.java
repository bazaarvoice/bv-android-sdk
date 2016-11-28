/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.io.File;

/**
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
