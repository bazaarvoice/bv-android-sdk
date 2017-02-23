/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowNetwork.class, BvSdkShadows.BvShadowAsyncTask.class, BaseShadows.ShadowAdIdClientNoLimit.class})
public class AdIdRequestTaskTest {

    @Test(expected=IllegalArgumentException.class)
    public void shouldFailWithNullCallback() {
        new AdIdRequestTask(RuntimeEnvironment.application.getApplicationContext(), null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldFailWithNullContext() {
        AdIdRequestTask.AdIdCallback adIdCallback = new AdIdRequestTask.AdIdCallback() {
            @Override
            public void onAdInfoComplete(AdIdResult result) {}
        };
        new AdIdRequestTask(null, adIdCallback);
    }

}
