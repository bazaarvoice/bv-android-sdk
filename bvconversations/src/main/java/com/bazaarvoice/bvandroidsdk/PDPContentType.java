package com.bazaarvoice.bvandroidsdk;

/**
 * Enum used to include certain content or statistics
 */
public enum PDPContentType {
    Reviews("Reviews"),
    Questions("Questions"),
    Answers("Answers");

    private final String key;

    PDPContentType(String key) {
        this.key = key;
    }

    public String toString() {
        return this.key;
    }
}
