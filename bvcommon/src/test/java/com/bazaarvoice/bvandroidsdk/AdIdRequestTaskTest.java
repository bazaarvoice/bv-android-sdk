/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.CountDownLatch;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowNetwork.class, BvSdkShadows.BvShadowAsyncTask.class, BaseShadows.ShadowAdIdClientNoLimit.class})
public class AdIdRequestTaskTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithNullCallback() {
        new AdIdRequestTask(RuntimeEnvironment.application.getApplicationContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithNullContext() {
        AdIdRequestTask.AdIdCallback adIdCallback = result -> {
        };
        new AdIdRequestTask(null, adIdCallback);
    }

    @Test
    public void shouldReturnAnAdId() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AdIdRequestTask.AdIdCallback adIdCallback = result -> {
            assertNotNull(result);
            assertNotNull(result.getAdId());
            latch.countDown();
        };
        AdIdRequestTask adIdRequestTask = new AdIdRequestTask(RuntimeEnvironment.application.getApplicationContext(), adIdCallback);
        adIdRequestTask.execute();
        latch.await();
    }

    @Test
    public void shouldReturnNonTrackingToken() {
        AdIdResult idResult = new AdIdResult(null, null);
        assertEquals("nontracking", idResult.getAdId());
    }

}
