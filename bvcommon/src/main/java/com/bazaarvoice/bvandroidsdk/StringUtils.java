/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Internal Util class for common string operations
 */
class StringUtils {

    static boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }

    static String componentsSeparatedBy(@NonNull List list, String splitBy) {
        return componentsSeparatedBy(list, splitBy, false);
    }

    static String componentsSeparatedByWithEscapes(@NonNull List list, String splitBy) {
        return componentsSeparatedBy(list, splitBy, true);
    }

    // TODO The should escape option can be removed by refactoring our building/encoding of urls
    private static String componentsSeparatedBy(List list, String splitBy, boolean shouldEscape) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {

            Object obj = list.get(i);
            if(obj != null) {
                String value = obj.toString();
                if (shouldEscape) {
                    value = bvCustomEscape(value);
                }

                builder.append(value);

                if (i < list.size() - 1) {
                    builder.append(splitBy);
                }
            }
        }

        return builder.toString();
    }

    private static String bvCustomEscape(String string) {
        string = string.replace(",", "\\,");
        string = string.replace(":", "\\:");
        string = string.replace("&", "%26");

        return string;
    }
}