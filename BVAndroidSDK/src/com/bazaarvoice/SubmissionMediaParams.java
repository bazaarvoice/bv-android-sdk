package com.bazaarvoice;

import org.apache.http.entity.ByteArrayEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * 
 * Handles the parameters for media submission. Only one media object per
 * submission, please! <br>
 * Use of this class may be eased with knowledge of the <a
 * href="http://developer.bazaarvoice.com/">Bazaarvoice API</a>. You might want
 * to use this site as a reference for which parameters to pass using this
 * class.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class SubmissionMediaParams extends BazaarParams {
	private String locale;
	private String userId;
	private String contentType;

	/**
	 * Create a new instance of SubmissionMediaParams with a given
	 * "Content Type" parameter.
	 * 
	 * @param contentType
	 *            the content for which the media will be submitted
	 */
	public SubmissionMediaParams(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Add the parameters set in this instance to the given url string.
	 * 
	 * @param url
	 *            the base url to append to
	 * @return the url with the parameter list on it
	 */
	public String toURL(String url) {
		url = addURLParameter(url, "contentType", contentType);
		url = addURLParameter(url, "locale", locale);
		url = addURLParameter(url, "userId", userId);
		return url;
	}

	/**
	 * Set a photo as the media object.
	 * 
	 * @param bytes
	 *            the raw photo data (jpeg, png etc)
	 * @param filename
	 *            the filename of the object. This is important for determining
	 *            the MIME type. Make sure you use the right extension for the
	 *            data, ie .jpg .avi etc
	 */
	public void setPhoto(byte[] bytes, String filename) {
		media = new Media(bytes, Media.MediaType.PHOTO, filename);
	}

	/**
	 * Read a file and set it as the media object.
	 * 
	 * @param file
	 *            the file to read the data from
	 * @throws FileNotFoundException
	 *             thrown if the file does not exist
	 * @throws IOException
	 *             thrown if the file cannot be read
	 */
	public void setPhoto(File file) throws FileNotFoundException, IOException {
		setMedia(file, Media.MediaType.PHOTO);
	}

	/**
	 * Set a video as the media object.
	 * 
	 * @param bytes
	 *            the raw video data (avi, etc)
	 * @param filename
	 *            the filename of the object. This is important for determining
	 *            the MIME type. Make sure you use the right extension for the
	 *            data, ie .jpg .avi etc
	 */
	public void setVideo(byte[] bytes, String filename) {
		media = new Media(bytes, Media.MediaType.VIDEO, filename);
	}

	/**
	 * Read a file and set it as the media object.
	 * 
	 * @param file
	 *            the file to read the data from
	 * @throws FileNotFoundException
	 *             thrown if the file does not exist
	 * @throws IOException
	 *             thrown if the file cannot be read
	 */
	public void setVideo(File file) throws FileNotFoundException, IOException {
		setMedia(file, Media.MediaType.VIDEO);
	}

	/**
	 * Reads the file into a Media object.
	 * 
	 * @param file
	 *            the file to read
	 * @param mediaType
	 *            the type of media being read
	 * @throws FileNotFoundException
	 *             if the file does not exist
	 * @throws IOException
	 *             if the file cannot be read
	 */
	private void setMedia(File file, Media.MediaType mediaType)
			throws FileNotFoundException, IOException {
		RandomAccessFile f = new RandomAccessFile(file, "r");
		byte[] b = new byte[(int) f.length()];
		f.read(b);
		media = new Media(b, mediaType, file.getName());
	}

	/**
	 * Get the "Content Type" parameter for the submission.
	 * 
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Set the "Content Type" parameter for the submission.
	 * 
	 * <p>
	 * <b>Usage:</b><br>
	 * This should be set in {@link #SubmissionMediaParams(String)} , but this method is available in
	 * case you want to change it after the fact.
	 * 
	 * @param contentType
	 *            a content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Get the "Locale" parameter for the submission.
	 * 
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Set the "Locale" parameter for the submission.
	 * 
	 * @param locale
	 *            a locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Get the "UserId" parameter for the submission.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the "UserId" parameter for the submission.
	 * 
	 * @param userId
	 *            a user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
