/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.text.SimpleDateFormat;
import java.util.Date;

class DateUtil {
    static SimpleDateFormat dateFormat = null;

    static Date dateFromString(String dateStr) {
        Date date = null;
        try {
            initDateFormat();
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    private static void initDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }
    }
}