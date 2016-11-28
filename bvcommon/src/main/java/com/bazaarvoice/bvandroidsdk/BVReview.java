/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * Bazaarvoice Review
 */
public class BVReview {

    private String title;
    private String text;
    private String authorName;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return authorName;
    }

    @Override
    public String toString() {
        return "BVReview{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
