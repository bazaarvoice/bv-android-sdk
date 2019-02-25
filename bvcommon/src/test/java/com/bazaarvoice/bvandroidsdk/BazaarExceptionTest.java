package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class BazaarExceptionTest {

    @Test
    public void testBazaarExceptionMessage() {
        String message = "Error Message Test";
        BazaarException bazaarException = new BazaarException(message);
        assertEquals(message, bazaarException.getMessage());
    }

    @Test
    public void testBazaarExceptionWithThrowable() {
        String message = "Error Message Test";
        Throwable throwable = new Throwable();
        BazaarException bazaarException = new BazaarException(message, throwable);
        assertEquals(message, bazaarException.getMessage());
    }

    @Test
    public void testBazaarRuntimeExceptionMessage() {
        String message = "Error Message Test";
        BazaarRuntimeException bazaarException = new BazaarRuntimeException(message);
        assertEquals(message, bazaarException.getMessage());
    }

    //todo should have test with orgranically thrown error.

}
