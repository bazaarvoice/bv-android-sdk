/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

/**
 * * An enum used for defining an action type for content submission.
 */
public enum Action {
    /**
     * Used when you want your submission to successfully send.
     * Requires all required fields to succeed.
     */
    Submit("Submit"),

    /**
     * Used when you want to see if your submission would successfully send.
     * Requires all required fields to succeed.
     */
    Preview("Preview"),

    /**
     * Used to obtain the form. Does not require all required fields to succeed.
     */
    Form("");

    private final String key;
    Action(String key) {
        this.key = key;
    }

    String getKey() {
        return key;
    }
}