package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateUtilTest {

    @Test
    public void dateParseSuccess() throws Exception {
        String serverTimeStamp = "2010-07-21T03:18:58.000+00:00";

        String expectedLocalizedTime = "Jul 20 08:18:58 PM -0700 2010";
        String expectedLocalizedTimeFormat = "MMM dd KK:mm:ss aa ZZZ yyyy";
        // Wed Jul 21 03:18:58 AM UTC 2010 // Time from server, always UTC standard
        // Tue Jul 20 10:18:58 PM PST 2010 // Localized Los Angeles Time
        // Both are the same time, just localized.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(expectedLocalizedTimeFormat, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date expectedDate = simpleDateFormat.parse(expectedLocalizedTime);
        dateParseExpectedDateWithoutError(serverTimeStamp, expectedDate);
    }

    private void dateParseExpectedDateWithoutError(String sampleTime, Date expectedDate) throws Exception {
        Date date = DateUtil.dateFromString(sampleTime);
        assertNotNull(date);
        assertEquals(expectedDate, date);
    }

}