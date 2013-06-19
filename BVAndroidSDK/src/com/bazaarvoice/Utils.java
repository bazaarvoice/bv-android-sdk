package com.bazaarvoice;

import java.io.File;
import java.io.FilenameFilter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Environment;


/**
 * Contains various utilities; some are used within the SDK, others
 * are intended to help developers with tasks relevant to SDK features.
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class Utils {

	/**
	 * Creates an instance of Utils. Not needed as all functions are static.
	 */
	public Utils() {
	}

	/**
	 * Holds the directory of the camera store folder on the user's device.
	 */
	public static final String CAMERA_IMAGE_BUCKET_NAME = Environment
			.getExternalStorageDirectory().toString() + "/DCIM";

	/**
	 * This method reads the SD card and gets a list of all jpegs found on it
	 * under the DCIM directory.
	 * 
	 * @return the list of jpg files
	 */
	public static List<File> getCameraImages() {
		return findFiles(CAMERA_IMAGE_BUCKET_NAME, ".jpg");
	}

	/**
	 * This method reads the SD card and gets a list of all 3gp videos found on
	 * it under the DCIM directory.
	 * 
	 * @return the list of 3gp files
	 */
	public static List<File> getCameraVideos() {
		return findFiles(CAMERA_IMAGE_BUCKET_NAME, ".3gp");
	}

	/**
	 * Get a list of files by extension on a given path.
	 * 
	 * @param path
	 *            the path to a folder to search under
	 * @param extension
	 *            the extension of the files desired
	 * @return the list of files
	 */
	public static ArrayList<File> findFiles(String path, final String extension) {
		File parentDir = new File(path);
		File[] directories = parentDir.listFiles();
		if (directories == null)
			return null;

		FilenameFilter extensionFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(extension);
			}
		};

		ArrayList<File> files = new ArrayList<File>();
		for (File directory : directories) {
			File[] fileArray = directory.listFiles(extensionFilter);
			Collections.addAll(files, fileArray);
		}
		return files;
	}

}
