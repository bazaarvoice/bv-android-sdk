package com.bazaarvoice.bvandroidsdk;

public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    private final String key;

    SortOrder(String key){
        this.key = key;
    }

    String getKey() {
        return this.key;
    }
}
