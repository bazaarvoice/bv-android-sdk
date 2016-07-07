/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

/**
 * TODO: Describe file here.
 */
public enum Action {
    Submit("Submit"), Preview("Preview");

    private final String key;
    Action(String key) {
        this.key = key;
    }

    String getKey() {
        return key;
    }
}