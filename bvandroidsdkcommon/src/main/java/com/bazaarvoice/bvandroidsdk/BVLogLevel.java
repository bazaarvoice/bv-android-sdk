/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * Log Level for the SDK to print to console
 */
public enum BVLogLevel {
    ERROR(1), WARNING(2), INFO(3), VERBOSE(4);

    private int value;

    BVLogLevel(int value) {
        this.value = value;
    }

    public boolean greaterOrEqualPriorityThan(BVLogLevel otherBvLogLevel) {
        return this.value >= otherBvLogLevel.value;
    }
}
