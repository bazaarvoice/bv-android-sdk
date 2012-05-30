package com.requiem.bazaarvoice;

/**
 * Author: Gary Pezza
 * Created: 5/13/12 10:44 PM
 *
 * This class contains byte data for a media object such as a photo or video
 */
public class Media implements Cloneable {
    private byte[] media;
    public static enum MediaType {PHOTO, VIDEO}
    private MediaType mediaType;
    private String filename;

    /**
     * Construct a media object
     *
     * @param media the media data
     * @param type the type of object
     * @param filename the filename of the object.  This is important for determining the MIME type.  Make sure
     *                 you use the right extension for the data, ie .jpg .avi etc
     */
    public Media(byte[] media, MediaType type, String filename) {
        this.media = media;
        this.mediaType = type;
        this.filename = filename;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();

        byte[] copyBytes = new byte[media.length];
        System.arraycopy(media, 0, copyBytes, 0, media.length);
        return new Media(copyBytes, mediaType, filename);
    }


    public String getName() {
        return mediaType==MediaType.PHOTO?"photo":"video";
    }

    public String getFilename() {
        return filename;
    }

    byte[] getBytes() {
        return media;
    }
}
