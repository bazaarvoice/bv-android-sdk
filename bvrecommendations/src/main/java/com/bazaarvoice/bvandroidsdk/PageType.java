package com.bazaarvoice.bvandroidsdk;

public enum PageType {
    HOME("home"),
    CATEGORY("category"),
    PRODUCT("product"),
    CART("cart"),
    THANKYOU("thankyou"),
    SEARCH("search");
    private String value;

    PageType(String value) {
        this.value = value;
    }
}