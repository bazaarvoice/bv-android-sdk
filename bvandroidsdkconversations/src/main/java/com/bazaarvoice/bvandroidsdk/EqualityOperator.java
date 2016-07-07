package com.bazaarvoice.bvandroidsdk;

public enum EqualityOperator {
    GT("gt"), GTE("gte"),LT("lt"), LTE("lte"), EQ("eq"), NEQ("neq");

    private String key;
    EqualityOperator(String key) {
        this.key = key;
    }

    String getKey() {
        return this.key;
    }
}
