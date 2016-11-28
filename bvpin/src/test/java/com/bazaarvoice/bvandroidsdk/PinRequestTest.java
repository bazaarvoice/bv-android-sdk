package com.bazaarvoice.bvandroidsdk;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PinRequestTest extends BVBaseTest {

    @Override
    void modifyPropertiesToInitSDK() {
        bazaarvoiceApiBaseUrl = "https://www.example.com/";
        pinApiKey = "test123";
        clientId = "someclient";
    }

    @Test
    public void bvPinRequestFormat() {
        PinRequest pinRequest = new PinRequest();
        String urlString = pinRequest.getUrlString("adId");
        String expectedString = "https://www.example.com/pin/toreview?passkey=test123&bvid=magpie_idfa_adId&client=someclient";
        Assert.assertEquals(expectedString, urlString);
    }

}
