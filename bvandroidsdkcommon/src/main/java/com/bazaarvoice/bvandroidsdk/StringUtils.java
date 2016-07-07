/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.util.List;

/**
 * TODO: Describe file here.
 */
class StringUtils {

    static String componentsSeparatedBy(List list, String splitBy) {
        return componentsSeparatedBy(list, splitBy, false);
    }

    static String componentsSeparatedByWithEscapes(List list, String splitBy) {
        return componentsSeparatedBy(list, splitBy, true);
    }

    private static String componentsSeparatedBy(List list, String splitBy, boolean shouldEscape) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {

            Object obj = list.get(i);
            String value = obj.toString();
            if (shouldEscape) {
                value = bvCustomEscape(value);
            }

            builder.append(value);

            if (i < list.size() - 1) {
                builder.append(splitBy);
            }
        }

        return builder.toString();
    }

    private static String bvCustomEscape(String string) {
        string.replace(",", "\\,");
        string.replace(":", "\\:");
        string.replace("&", "%26");

        return string;
    }
}