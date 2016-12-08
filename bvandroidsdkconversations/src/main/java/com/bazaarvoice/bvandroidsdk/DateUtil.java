/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateUtil {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    static Date dateFromString(String dateStr) {
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
        }

        return date;
    }
}