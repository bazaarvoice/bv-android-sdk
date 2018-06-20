package com.bazaarvoice.bvandroidsdk;

public enum ReportingGroup {
    RECYCLERVIEW("recyclerView"), CUSTOM("custom");

    private String value;

    ReportingGroup(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}