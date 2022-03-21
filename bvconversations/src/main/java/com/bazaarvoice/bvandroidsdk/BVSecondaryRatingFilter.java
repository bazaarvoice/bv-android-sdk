package com.bazaarvoice.bvandroidsdk;

public final class BVSecondaryRatingFilter {

    private final String type;
    private final EqualityOperator equalityOperator;
    private final String value;

    public BVSecondaryRatingFilter(String type, EqualityOperator equalityOperator, String value) {
        this.type = type;
        this.equalityOperator = equalityOperator;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public EqualityOperator getEqualityOperator() {
        return equalityOperator;
    }

    public String getValue() {
        return value;
    }
}