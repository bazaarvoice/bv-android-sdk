package com.bazaarvoice.bvandroidsdk;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class PinClientTest extends BVBaseTest {

    @Override
    void modifyPropertiesToInitSDK() {

    }

    @Test
    public void testParsingFullResponse() {
        String respStr = jsonFileAsString("pin_response.json");
        Gson gson = new Gson();
        Pin[] pins = gson.fromJson(respStr, Pin[].class);
        assertNotNull(pins);
        assertEquals(4, pins.length);

        Pin firstPinItem = pins[0];
        assertEquals("http://www.endurancecycles.com/products/endurance-guard", firstPinItem.getProductPageUrl());
        assertEquals(4.3f, firstPinItem.getAverageRating(), 0.0f);
        assertEquals("http://cdn.shopify.com/s/files/1/0796/3917/files/helmet1.JPG?12529938024920340634", firstPinItem.getDisplayImageUrl());
        assertEquals("Endurance Guard", firstPinItem.getDisplayName());
        assertEquals("19-bv", firstPinItem.getId());
    }
}
