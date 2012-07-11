package com.bazaarvoice;

/**
 * Media.java <br>
 * Bazaarvoice Android SDK<br>
 * 
 * This class contains byte data for a media object such as a photo or video.
 * Used internally for uploading media in a request.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class Media implements Cloneable {
	private byte[] media;

	public static enum MediaType {
		PHOTO, VIDEO
	}

	private MediaType mediaType;
	private String filename;

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
		this.media = media;
		this.mediaType = type;
		this.filename = filename;
	}

	/**
	 * Gets a clone of the media object represented in this instance.
	 * 
	 * @return a clone of the media object
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		super.clone();

		byte[] copyBytes = new byte[media.length];
		System.arraycopy(media, 0, copyBytes, 0, media.length);
		return new Media(copyBytes, mediaType, filename);
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
	 * Get the byte array representing the media object.
	 * 
	 * @return the media
	 */
	byte[] getBytes() {
		return media;
	}
}
