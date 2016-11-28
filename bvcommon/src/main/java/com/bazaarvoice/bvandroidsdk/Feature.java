package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 4/8/16.
 */

public enum Feature {
    INVIEW("InView"), SCROLLED("Scrolled"), SWIPE("Swipe"), CONTENT_CLICK("ContentClick");

    private String value;

    Feature(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
