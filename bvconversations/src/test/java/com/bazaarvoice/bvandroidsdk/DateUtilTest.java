package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class DateUtilTest {

    @Test
    public void dateFromString() throws Exception {
        dateParsesWithoutError("2010-07-21T03:18:58.000+00:00");
        dateParsesWithoutError("2011-06-30T21:17:48.000+00:00");
        dateParsesWithoutError("2015-11-17T06:30:02.000+00:00");
        dateParsesWithoutError("2016-12-09T19:28:54.000+00:00");
        dateParsesWithoutError("2015-02-05T05:20:02.000+00:00");
        dateParsesWithoutError("2016-12-13T20:00:02.000+00:00");
    }

    private void dateParsesWithoutError(String sampleTime) throws Exception {
        Date date = DateUtil.dateFromString(sampleTime);
        assertNotNull(date);
    }

}