package com.bazaarvoice.bvandroidsdk;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BVRemoteConfigTest extends BVBaseTest {

    @Override
    protected void modifyPropertiesToInitSDK() {
        notificationConfigUrl = "baseurl/";
        clientId = "testclient";
    }

    @Test
    public void generateConvForStoresConfigUrl() {
        String convForStoresUrl = BVRemoteConfig.getFeatureConfigUrl("conversations-stores", "geofenceConfig.json");
        String expectedUrl = "baseurl/incubator-mobile-apps/sdk/v1/android/testclient/conversations-stores/geofenceConfig.json";
        Assert.assertEquals(expectedUrl, convForStoresUrl);
    }

    @Test
    public void generatePinConfigUrl() {
        String pinUrl = BVRemoteConfig.getFeatureConfigUrl("pin", "pinConfig.json");
        String expectedUrl = "baseurl/incubator-mobile-apps/sdk/v1/android/testclient/pin/pinConfig.json";
        Assert.assertEquals(expectedUrl, pinUrl);
    }

}
