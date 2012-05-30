package com.requiem.bazaarvoice;

import org.apache.http.entity.ByteArrayEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * User: COLIN
 * Date: 5/7/12
 * Time: 8:34 PM
 *
 * This class handles the parameters for media submissions.  Only 1 media object per request!
 */
public class SubmissionMediaParams extends BazaarParams {
    private String locale;
    private String userId;
    private String contentType;

    public SubmissionMediaParams(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @param url the base url to append to
     * @return the url with the parameter list on it
     */
    public String toURL(String url) {
        url = addURLParameter(url, "contentType", contentType);
        url = addURLParameter(url, "locale", locale);
        url = addURLParameter(url, "userId", userId);
        return url;
    }

    /**
     * Set a photo as the media object
     * @param bytes the raw photo data (jpeg, png etc)
     * @param filename the filename of the object.  This is important for determining the MIME type.  Make sure
     *                 you use the right extension for the data, ie .jpg .avi etc
     */
    public void setPhoto(byte[] bytes, String filename) {
        media =  new Media(bytes, Media.MediaType.PHOTO, filename);
    }

    /**
     * Read a file and set it as the media object
     * @param file the file to read the data from
     * @throws FileNotFoundException  thrown if the file does not exist
     * @throws IOException thrown if the file cannot be read
     */
    public void setPhoto(File file) throws FileNotFoundException, IOException {
        setMedia(file, Media.MediaType.PHOTO);
    }

    /**
     * Set a video as the media object
     * @param bytes the raw video data (avi, etc)
     * @param filename the filename of the object.  This is important for determining the MIME type.  Make sure
     *                 you use the right extension for the data, ie .jpg .avi etc
     */
    public void setVideo(byte[] bytes, String filename) {
        media =  new Media(bytes, Media.MediaType.VIDEO, filename);
    }

    /**
     * Read a file and set it as the media object
     * @param file the file to read the data from
     * @throws FileNotFoundException  thrown if the file does not exist
     * @throws IOException thrown if the file cannot be read
     */
    public void setVideo(File file) throws FileNotFoundException, IOException {
        setMedia(file, Media.MediaType.VIDEO);
    }

    /**
     * Reads the file into a Media object
     * @param file the file to read
     * @param mediaType the type of media being read
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void setMedia(File file, Media.MediaType mediaType) throws FileNotFoundException, IOException {
        RandomAccessFile f = new RandomAccessFile(file, "r");
        byte[] b = new byte[(int)f.length()];
        f.read(b);
        media = new Media(b, mediaType, file.getName());
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
