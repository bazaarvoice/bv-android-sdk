package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(shadows = {Shadows.ShadowNetwork.class, Shadows.BvShadowAsyncTask.class, Shadows.ShadowAdIdClient.class})
public class CurationsUnitTest {

    String curationsApiKey;

    MockWebServer server = new MockWebServer();

    String versionName;
    String versionCode;
    String packageName;
    String clientId;
    String uuidTestStr = "0871bbf6-b73e-4841-99f2-5e3d887eaea2";
    UUID uuid;

    OkHttpClient okHttpClient;
    BazaarEnvironment environment;
    @Mock ScheduledExecutorService scheduledExecutorService;
    @Mock ExecutorService immediateExecutorService;
    String shopperAdvertisingApiKey;
    Gson gson = new GsonBuilder().create();
    String shopperMarketingApiBaseUrl;
    BVLogLevel bvLogLevel;

    ExecutorService executorService = mock(ExecutorService.class);
    AnalyticsManager analyticsManager = mock(AnalyticsManager.class);
    BVActivityLifecycleCallbacks bvActivityLifecycleCallbacks = mock(BVActivityLifecycleCallbacks.class);
    BVAuthenticatedUser bvAuthenticatedUser = mock(BVAuthenticatedUser.class);

    BVCurations curations = new BVCurations();
    CurationsFeedRequest request = new CurationsFeedRequest.Builder(new ArrayList<String>(){{add("__all_");}}).build();
    String curationsApiBaseUrl;
    String curationsPostApiBaseUrl;
    String baseSerializationExpectation;
    String curationsPostFullUrl;
    Bitmap testBitmap;

    @Before
    public void setup() {
        // arrange
        versionName = "3.0.0";
        versionCode = "56";
        packageName = "com.mypackagename.app";
        clientId = "pretendcompany";
        MockitoAnnotations.initMocks(this);
        okHttpClient = new OkHttpClient();
        environment = BazaarEnvironment.STAGING;
        curationsApiKey = "foobar-bvtestcurationskey";
        uuid = UUID.fromString(uuidTestStr);
        bvLogLevel = BVLogLevel.VERBOSE;
        testBitmap = null;

        // Builder used to initialize the Bazaarvoice SDKs

        curationsApiBaseUrl = server.url("/curations/").toString();
        baseSerializationExpectation = curationsApiBaseUrl + "client=" + clientId +"&passKey=" + curationsApiKey;

        curationsPostApiBaseUrl = server.url("/curationspost/").toString();
        curationsPostFullUrl  = curationsPostApiBaseUrl + "?client=" + clientId + "&passkey=" + curationsApiKey;
        BVSDK.singleton = new BVSDK(RuntimeEnvironment.application, clientId, environment, shopperAdvertisingApiKey, shopperAdvertisingApiKey, curationsApiKey, bvLogLevel, new OkHttpClient(), analyticsManager, bvActivityLifecycleCallbacks, bvAuthenticatedUser, gson, shopperMarketingApiBaseUrl, curationsApiBaseUrl, curationsPostApiBaseUrl);
    }

    @Test
    public void fetchCurationsFeed() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("curationsFeedTest1.json");

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(convertStreamToString(in));

        server.enqueue(response);

        curations.getCurationsFeedItems(request, new CurationsFeedCallback() {
            @Override
            public void onSuccess(List<CurationsFeedItem> feedItems) {
                assertTrue("Should have some feed items", feedItems.size() > 0);
            }

            @Override
            public void onFailure(Throwable throwable) {
                fail("Should not fail with proper curations response");
            }
        });
    }

    @Test
    public void malformedCurationsFeed() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("curationsMalformedFeedTest1.json");

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(convertStreamToString(in));

        server.enqueue(response);

        curations.getCurationsFeedItems(request, new CurationsFeedCallback() {
            @Override
            public void onSuccess(List<CurationsFeedItem> feedItems) {
                fail("Should not succeed with malformed curations response");
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    @Test
    public void curations500Feed() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("curations500Error.json");

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(convertStreamToString(in));

        server.enqueue(response);

        curations.getCurationsFeedItems(request, new CurationsFeedCallback() {
            @Override
            public void onSuccess(List<CurationsFeedItem> feedItems) {
                fail("Should not succeed with 500 code in curations response");
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    static String convertStreamToString(java.io.InputStream is) {
        Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Test
    public void internalServerError() throws Exception {

        MockResponse response = new MockResponse()
                .setResponseCode(500)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("");

        server.enqueue(response);

        curations.getCurationsFeedItems(request, new CurationsFeedCallback() {
            @Override
            public void onSuccess(List<CurationsFeedItem> feedItems) {
                fail("Should not succeed with 500 HTTP response code");
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }


    @Test
    public void emptyResponse() throws Exception {

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("");

        server.enqueue(response);

        curations.getCurationsFeedItems(request, new CurationsFeedCallback() {
            @Override
            public void onSuccess(List<CurationsFeedItem> feedItems) {
                fail("Should not succeed with empty response");
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    @Test
    public void requestSerialization1(){

        String expected = baseSerializationExpectation + "&groups=__all__";

        List<String> groups = new ArrayList<>();
        groups.add("__all__");

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).build();

        String actual = request.toUrlQueryString();
        assertTrue("", actual.equals(expected));
    }

    @Test
    public void requestSerialization2(){

        String expected = baseSerializationExpectation + "&groups=a&groups=b";

        List<String> groups = new ArrayList<>();
        groups.add("a");
        groups.add("b");

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).build();

        String actual = request.toUrlQueryString();
        assertTrue("", actual.equals(expected));
    }

    @Test
    public void requestSerialization3(){

        String expected = baseSerializationExpectation + "&groups=__all__&media={'video':{'width':480,'height':360}}";

        List<String> groups = new ArrayList<>();
        groups.add("__all__");

        CurationsMedia media = new CurationsMedia("video", 480, 360);
        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).media(media).build();


        String actual = request.toUrlQueryString();
        assertTrue("", actual.equals(expected));
    }

    @Test
    public void requestSerialization4(){

        String expected = baseSerializationExpectation + "&groups=__all__&tags=testTag&tags=other";

        List<String> groups = new ArrayList<>();
        groups.add("__all__");

        List<String> tags = new ArrayList<>();
        tags.add("testTag");
        tags.add("other");

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).tags(tags).build();

        String actual = request.toUrlQueryString();
        assertTrue("", actual.equals(expected));
    }

    CurationsAuthor getAuthor(){
        return new CurationsAuthor.Builder("Alias", "Token").build();
    }

    List<String> getGroups(){
        return Arrays.asList("TestGroup1", "TestGroup2");
    }

    @Test
    public void postFullUrlGeneratedFromRequestBuilderCorrectly(){
        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), "TEXT", testBitmap).build();

        assertEquals(request.toUrlQueryString(), curationsPostFullUrl);
    }

    @Test
    public void postFullPayload(){

        String authorAlias = "Alias";
        String authorToken = "Token";
        String text = "Text";
        String testGroup1 = "TestGroup1";
        String testGroup2 = "TestGroup2";
        String tag1 = "tag1";
        String tag2 = "tag2";
        String link1 = "Link1";
        String permalink = "permalink";
        String place = "Austin, TX";
        String teaser = "teaser";
        String photo1 = "photoUrl";
        String timeStamp = "123";

        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), text, Arrays.asList(new CurationsPhoto(photo1)))
                .tags(Arrays.asList(tag1, tag2))
                .links(Arrays.asList(new CurationsLink(link1)))
                .permalink(permalink)
                .place(place)
                .teaser(teaser)
                .timestampInSeconds(Long.parseLong(timeStamp)).build();

        String [] expectedComponents = new String[10];
        expectedComponents[0] = "\"author\":{\"alias\":\"" + authorAlias + "\",\"token\":\"" + authorToken + "\"}";
        expectedComponents[1] = "\"text\":\"" + text + "\"";
        expectedComponents[2] = "\"tags\":[\"" + tag1 + "\",\"" + tag2 + "\"]";
        expectedComponents[3] = "\"links\":[{\"url\":\"" + link1 + "\"}]";
        expectedComponents[4] = "\"permalink\":\"" + permalink + "\"";
        expectedComponents[5] = "\"place\":\"" + place + "\"";
        expectedComponents[6] = "\"teaser\":\"" + teaser + "\"";
        expectedComponents[7] = "\"photos\":[{\"remote_url\":\"" + photo1 + "\"}]";
        expectedComponents[8] = "\"timestamp\":" + timeStamp;
        expectedComponents[9] = "\"groups\":[\"" + testGroup1 + "\",\"" + testGroup2 + "\"]";

        String payload = request.getJsonPayload();
        for (String expected : expectedComponents){
            assertTrue("Payload mismatch\nExpected: " + expected, payload.contains(expected));
        }
    }

    @Test
    public void postSuccess(){
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("{\"status\":201}");
        server.enqueue(response);

        BVCurations curations = new BVCurations();
        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), "TEXT", testBitmap).build();
        curations.postContentToCurations(request, new CurationsPostCallback() {
            @Override
            public void onSuccess(CurationsPostResponse response) {
            }

            @Override
            public void onFailure(Throwable throwable) {
                fail("Should not be a failure");
            }
        });
    }

    @Test
    public void postMalformedResponse(){
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("curationsMalformedPostResponse.json");

        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(convertStreamToString(in));

        server.enqueue(response);

        BVCurations curations = new BVCurations();
        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), "TEXT", testBitmap).build();
        curations.postContentToCurations(request, new CurationsPostCallback() {
            @Override
            public void onSuccess(CurationsPostResponse response) {
                fail("Should not be a success");
            }

            @Override
            public void onFailure(Throwable throwable) {}
        });
    }

    @Test
    public void postEmptyBody(){
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8");
        server.enqueue(response);

        BVCurations curations = new BVCurations();
        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), "TEXT", testBitmap).build();
        curations.postContentToCurations(request, new CurationsPostCallback() {
            @Override
            public void onSuccess(CurationsPostResponse response) {
                fail("Should not be successful");
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    @Test
    public void postFailureCurationsError(){
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("{\"status\":\"Could not find group\"}");
        server.enqueue(response);

        BVCurations curations = new BVCurations();
        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), "TEXT", testBitmap).build();
        curations.postContentToCurations(request, new CurationsPostCallback() {
            @Override
            public void onSuccess(CurationsPostResponse response) {
                fail("Should not be successful");
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Test
    public void postFailureServerError(){
        MockResponse response = new MockResponse()
                .setResponseCode(500)
                .addHeader("Content-Type", "application/json; charset=utf-8");
        server.enqueue(response);

        BVCurations curations = new BVCurations();
        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), "TEXT", testBitmap).build();
        curations.postContentToCurations(request, new CurationsPostCallback() {
            @Override
            public void onSuccess(CurationsPostResponse response) {
                fail("Should not be successful");
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}