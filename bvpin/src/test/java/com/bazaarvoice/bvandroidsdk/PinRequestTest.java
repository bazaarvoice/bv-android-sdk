package com.bazaarvoice.bvandroidsdk;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PinRequestTest extends BVBaseTest {

    @Override
    protected void modifyPropertiesToInitSDK() {
        bazaarvoiceApiBaseUrl = "https://www.example.com/";
    }

    @Test
    public void bvPinRequestFormat() {
        PinRequest pinRequest = new PinRequest();
        String urlString = pinRequest.getUrlString("adId");
        String expectedString = "https://www.example.com/pin/toreview?passkey=" + bvUserProvidedData.getBvApiKeys().getApiKeyPin() + "&bvid=magpie_idfa_adId&client=" + bvUserProvidedData.getClientId();
        Assert.assertEquals(expectedString, urlString);
    }

}
