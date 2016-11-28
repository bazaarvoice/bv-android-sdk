package com.bazaarvoice.bvandroidsdk;

class Include {

    private final PDPContentType type;
    private final Integer limit;

    Include(PDPContentType type, Integer limit) {
        this.type = type;
        this.limit = limit;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    String getLimitParamKey() {
        return String.format("Limit_%s", this.type.toString());
    }

    Integer getLimit() {
        return this.limit;
    }
}
