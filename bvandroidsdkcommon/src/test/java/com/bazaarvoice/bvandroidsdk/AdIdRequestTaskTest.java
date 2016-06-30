/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class, Shadows.BvShadowAsyncTask.class, Shadows.ShadowAdIdClient.class})
public class AdIdRequestTaskTest {

    @Test
    public void shouldFailWithNullCallback() {
        try {
            new AdIdRequestTask(RuntimeEnvironment.application.getApplicationContext(), null);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void shouldFailWithNullContext() {
        AdIdRequestTask.AdIdCallback adIdCallback = new AdIdRequestTask.AdIdCallback() {
            @Override
            public void onAdInfoComplete(AdIdResult result) {}
        };
        try {
            new AdIdRequestTask(null, adIdCallback);
            fail();
        } catch (IllegalArgumentException expected) {}
    }

}
