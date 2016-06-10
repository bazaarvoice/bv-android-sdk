package com.bazaarvoice.bvandroidsdk;

enum PlaceAttribute {
    Type("type"),
    ClientId("clientId"),
    Name("name"),
    City("city"),
    State("state"),
    Zip("zip"),
    Address("address"),
    StoreId("storeId"),
    Id("id");

    private final String key;
    private PlaceAttribute(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
