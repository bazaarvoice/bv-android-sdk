/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

class Sort {
    private final UGCOption option;
    private final SortOrder sortOrder;

    Sort(UGCOption option, SortOrder sortOrder) {
        this.option = option;
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", option.getKey(), sortOrder.getKey());
    }
}