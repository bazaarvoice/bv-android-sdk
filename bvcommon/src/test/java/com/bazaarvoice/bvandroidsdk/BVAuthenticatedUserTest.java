package com.bazaarvoice.bvandroidsdk;

import androidx.test.core.app.ApplicationProvider;

import com.google.gson.Gson;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowNetwork.class, BvSdkShadows.BvShadowAsyncTask.class, BaseShadows.ShadowAdIdClientNoLimit.class, BaseShadows.NetworkSecurityPolicyWorkaround.class})
public class BVAuthenticatedUserTest extends BVBaseTest {

    BVAuthenticatedUser subject;
    String baseurl;
    String shopperApiKey;
    List<Integer> profilePollTimes;

    @Before
    public void setup() throws Exception {
        super.setup();
        initMocks(this);
        baseurl = server.url("example.com/").toString();
        shopperApiKey = "testString123";
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        bvLogger = new BVLogger(BVLogLevel.VERBOSE);
        profilePollTimes = Arrays.asList(0, 10, 20, 30);
        subject = new BVAuthenticatedUser(ApplicationProvider.getApplicationContext(), baseurl, shopperApiKey, okHttpClient, bvLogger, gson, profilePollTimes, new BaseTestUtils.TestHandlerThread());
    }

    @Test
    public void setUserAuth() {
        String newUserAuth = "testUserAuth";
        subject.setUserAuthString(newUserAuth);
        assertEquals(newUserAuth, subject.getUserAuthString());
    }

    @Test
    public void updateUserEmptyResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(getJsonFile(BVAuthenticatedUserTest.class, "empty_profile.json")));

        subject.updateUser("testing");

        String expectedPath = String.format("/example.com/users/magpie_idfa_%s?passkey=%s", "00000000-0000-0000-0000-000000000000", shopperApiKey);

        RecordedRequest req1 = server.takeRequest(50, TimeUnit.MILLISECONDS);
        assertEquals(expectedPath, req1.getPath());
        TestCase.assertEquals(0, subject.getShopperProfile().getProfile().getTargetingKeywords().size());
    }

    @Test
    public void updateUserFullResponse() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(getJsonFile(BVAuthenticatedUserTest.class, "full_profile.json")));

        subject.updateUser("testing");

        String expectedPath = String.format("/example.com/users/magpie_idfa_%s?passkey=%s", "00000000-0000-0000-0000-000000000000", shopperApiKey);

        RecordedRequest req1 = server.takeRequest(50, TimeUnit.MILLISECONDS);
        assertEquals(expectedPath, req1.getPath());
        TestCase.assertEquals(2, subject.getShopperProfile().getProfile().getTargetingKeywords().size());
    }


    private static String getJsonFile(Class clazz, String fileName) throws IOException {
        InputStream in = clazz.getClassLoader().getResourceAsStream(fileName);
        BufferedSource bufferedSource = Okio.buffer(Okio.source(in));
        return bufferedSource.readString(Charset.defaultCharset());
    }

}