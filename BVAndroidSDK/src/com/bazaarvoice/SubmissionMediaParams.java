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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;

import com.bazaarvoice.types.MediaParamsContentType;

/**
 * 
 * Handles the parameters for media submission. Only one media
 * object per submission, please! <br>
 * Use of this class may be eased with knowledge of the <a
 * href="http://developer.bazaarvoice.com/">Bazaarvoice API</a>. You might want
 * to use this site as a reference for which parameters to pass using this
 * class.
 */
public class SubmissionMediaParams extends BazaarParams {
	private MediaParamsContentType contentType;
	private String locale;
	private String userId;
	private String photoUrl;


	/**
	 * Create a new instance of SubmissionMediaParams with a given
	 * "Content Type" parameter.
	 * 
	 * @param contentType
	 *            the content for which the media will be submitted
	 */
	public SubmissionMediaParams(MediaParamsContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Get the "Content Type" parameter for the submission.
	 * 
	 * @return the content type
	 */
	public MediaParamsContentType getContentType() {
		return contentType;
	}
	
	/**
	 * The content type for which this media is being submitted. Review, question, answer or story.
	 * 
	 * @param contentType
	 *            a content type
	 */
	public void setContentType(MediaParamsContentType contentType) {
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
	 * Locale to display Labels, Configuration, Product Attributes and Category Attributes in. The default value is the locale defined in the display associated with the API key.
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
	 * User's external ID.
	 * 
	 * @param userId
	 *            a user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	* Sets photo directly from a bitmap
	* 
	* @param bitmap
	*            the bitmap representation of the image to upload
	* @param filename
	*            the filename of the photo to upload -- this is necessary to determine mime type
	*/
	public void setPhoto(Bitmap bitmap, String filename)  {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
		byte[] b = baos.toByteArray();  
		media = new Media(b, Media.MediaType.PHOTO, filename);
	}

	/**
	 * Get the "PhotoUrl" parameter for the submission.
	 * 
	 * @return the photo url
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}
	
	/**
	 * URL of the photo to be uploaded. Use either the photo or photoUrl parameter to define the photo to upload. An error is returned if both parameters are defined. HTTP and HTTPS are the only protocols supported for the photoUrl parameter.
	 * 
	 * @param photoUrl
	 *            a photo url
	 */
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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
		media = new Media(file, mediaType, file.getName());
	}

	@Override
	protected String toURL(String apiVersion, String passKey) {
		StringBuilder url = new StringBuilder();
		
		if(getContentType() != null){
			url.append(addURLParameter("contentType", getContentType().getTypeString()));			
		}
		url.append(addURLParameter("apiversion", apiVersion));
		url.append(addURLParameter("passkey", passKey));
		url.append(addURLParameter("locale", getLocale()));
		url.append(addURLParameter("userId", getUserId()));
		url.append(addURLParameter("photoUrl", getPhotoUrl()));
		return url.toString();
	}

	@Override
	protected void addPostParameters(String apiVersion, String passKey, BazaarRequest request) {
		if(getContentType() != null){
			addMultipartParameter("contentType", getContentType().getTypeString(), request);			
		}
		addMultipartParameter("apiversion", apiVersion, request);
		addMultipartParameter("passkey", passKey, request);
		addMultipartParameter("locale", getLocale(), request);
		addMultipartParameter("userId", getUserId(), request);
		addMultipartParameter("photoUrl", getPhotoUrl(), request);		
	}
}
