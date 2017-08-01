package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {BaseShadows.ShadowNetwork.class, BvSdkShadows.BvShadowAsyncTask.class, BaseShadows.ShadowAdIdClientNoLimit.class})
public class CurationsUnitTest extends BVBaseTest {

    BVCurations curations;
    CurationsFeedRequest request = new CurationsFeedRequest.Builder(new ArrayList<String>(){{add("__all_");}}).build();
    String curationsDisplayFullUrl;
    String curationsPostFullUrl;
    Bitmap testBitmap;
    private CurationsFeedRequest.Builder genericFeedBuilder;
    private String genericFeedRequestStr;

    @Override
    protected void modifyPropertiesToInitSDK() {
        bazaarvoiceApiBaseUrl = server.url("").toString();
        curationsDisplayFullUrl = bazaarvoiceApiBaseUrl + "curations/content/get?client=" + bvUserProvidedData.getBvConfig().getClientId() +"&passkey=" + bvUserProvidedData.getBvConfig().getApiKeyCurations();

        genericFeedBuilder = new CurationsFeedRequest.Builder(Arrays.asList("__all__"));
        genericFeedRequestStr = curationsDisplayFullUrl + "&groups=__all__";

        curationsPostFullUrl  = bazaarvoiceApiBaseUrl + "curations/content/add/?client=" + bvUserProvidedData.getBvConfig().getClientId() + "&passkey=" + bvUserProvidedData.getBvConfig().getApiKeyCurations();
    }

    @Override
    protected void afterInitSdk(BVSDK bvsdk) {
        super.afterInitSdk(bvsdk);
        curations = new BVCurations();
    }

    @Test
    public void fetchCurationsFeed() throws Exception {
        String json = jsonFileAsString("curationsFeedTest1.json");
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(json);

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
        String json = jsonFileAsString("curationsMalformedFeedTest1.json");
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(json);

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
        String json = jsonFileAsString("curations500Error.json");
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(json);

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

        String expected = curationsDisplayFullUrl + "&groups=__all__";

        List<String> groups = new ArrayList<>();
        groups.add("__all__");

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).build();

        String actual = request.toUrlQueryString();
        assertEquals(expected, actual);
    }

    @Test
    public void requestSerialization2(){

        String expected = curationsDisplayFullUrl + "&groups=a&groups=b";

        List<String> groups = new ArrayList<>();
        groups.add("a");
        groups.add("b");

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).build();

        String actual = request.toUrlQueryString();
        assertEquals(expected, actual);
    }

    @Test
    public void requestSerialization3(){

        String expected = curationsDisplayFullUrl + "&groups=__all__&media={%27video%27:{%27width%27:480,%27height%27:360}}";

        List<String> groups = new ArrayList<>();
        groups.add("__all__");

        CurationsMedia media = new CurationsMedia("video", 480, 360);
        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).media(media).build();


        String actual = request.toUrlQueryString();
        assertEquals(expected, actual);
    }

    @Test
    public void requestSerialization4(){

        String expected = curationsDisplayFullUrl + "&groups=__all__&tags=testTag&tags=other";

        List<String> groups = new ArrayList<>();
        groups.add("__all__");

        List<String> tags = new ArrayList<>();
        tags.add("testTag");
        tags.add("other");

        CurationsFeedRequest request = new CurationsFeedRequest.Builder(groups).tags(tags).build();

        String actual = request.toUrlQueryString();
        assertEquals(expected, actual);
    }

    CurationsAuthor getAuthor(){
        return new CurationsAuthor.Builder("Alias", "Token").build();
    }

    List<String> getGroups(){
        return Arrays.asList("TestGroup1", "TestGroup2");
    }

    @Test
    public void postFullUrlGeneratedFromRequestBuilderCorrectly(){
        CurationsPostRequest request = new CurationsPostRequest.Builder(getAuthor(), getGroups(), "TEXT", testBitmap)
                .build();

        assertEquals(curationsPostFullUrl, request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedGroups() {
        CurationsFeedRequest request = new CurationsFeedRequest.Builder(Arrays.asList("pocket", "jelly"))
                .build();

        assertEquals(curationsDisplayFullUrl + "&groups=pocket&groups=jelly", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedAuthorToken() {
        CurationsFeedRequest request = genericFeedBuilder
                .authorTokenOrAlias("authorArthur")
                .build();

        assertEquals(genericFeedRequestStr + "&author=authorArthur", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedExternalId() {
        CurationsFeedRequest request = genericFeedBuilder
                .externalId("id123")
                .build();

        assertEquals(genericFeedRequestStr + "&externalId=id123", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedAfter() {
        CurationsFeedRequest request = genericFeedBuilder
                .after(12345l)
                .build();

        assertEquals(genericFeedRequestStr + "&after=12345", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedBefore() {
        CurationsFeedRequest request = genericFeedBuilder
                .before(12345l)
                .build();

        assertEquals(genericFeedRequestStr + "&before=12345", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedFeatured() {
        CurationsFeedRequest request = genericFeedBuilder
                .featured(11)
                .build();

        assertEquals(genericFeedRequestStr + "&featured=11", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedLimit() {
        CurationsFeedRequest request = genericFeedBuilder
                .limit(23)
                .build();

        assertEquals(genericFeedRequestStr + "&limit=23", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedPerGroupLimit() {
        CurationsFeedRequest request = genericFeedBuilder
                .perGroupLimit(20)
                .build();

        assertEquals(genericFeedRequestStr + "&per_group_limit=20", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedHasGeoTag() {
        CurationsFeedRequest request = genericFeedBuilder
                .hasGeoTag(true).build();

        assertEquals(genericFeedRequestStr + "&has_geotag=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedHasLink() {
        CurationsFeedRequest request = genericFeedBuilder
                .hasLink(true).build();

        assertEquals(genericFeedRequestStr + "&has_link=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedWithProductData() {
        CurationsFeedRequest request = genericFeedBuilder
                .withProductData(true).build();

        assertEquals(genericFeedRequestStr + "&withProductData=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedHasPhoto() {
        CurationsFeedRequest request = genericFeedBuilder
                .hasPhoto(true).build();

        assertEquals(genericFeedRequestStr + "&has_photo=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedHasVideo() {
        CurationsFeedRequest request = genericFeedBuilder
                .hasVideo(true).build();

        assertEquals(genericFeedRequestStr + "&has_video=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedHasPhotoOrVideo() {
        CurationsFeedRequest request = genericFeedBuilder
            .hasPhotoOrVideo(true).build();

        assertEquals(genericFeedRequestStr + "&has_photo_or_video=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedIncludeComments() {
        CurationsFeedRequest request = genericFeedBuilder
                .includeComments(true).build();

        assertEquals(genericFeedRequestStr + "&include_comments=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedExplicitPermission() {
        CurationsFeedRequest request = genericFeedBuilder
                .explicitPermission(true).build();

        assertEquals(genericFeedRequestStr + "&explicit_permission=true", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedMedia() {
        CurationsMedia media = new CurationsMedia("potato", 9001, 9002);
        CurationsFeedRequest request = genericFeedBuilder
                .media(media).build();
        assertEquals(genericFeedRequestStr + "&media={%27potato%27:{%27width%27:9001,%27height%27:9002}}", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedTags() {
        CurationsFeedRequest request = genericFeedBuilder
                .tags(Arrays.asList("tag1", "tag2", "tag3")).build();

        assertEquals(genericFeedRequestStr + "&tags=tag1&tags=tag2&tags=tag3", request.toUrlQueryString());
    }

    @Test
    public void displayFullUrlGeneratedLatLong() {
        CurationsFeedRequest request = genericFeedBuilder
                .location(40.2, -31.4).build();
        assertEquals(genericFeedRequestStr + "&geolocation=40.2,-31.4", request.toUrlQueryString());
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
                .geoCoordinates(37.123, -97.456)
                .timestampInSeconds(Long.parseLong(timeStamp)).build();

        String [] expectedComponents = new String[11];
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
        expectedComponents[10] = "\"coordinates\":{\"latitude\":37.123,\"longitude\":-97.456}";

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
    public void postMalformedResponse() throws Exception {
        String json = jsonFileAsString("curationsMalformedPostResponse.json");
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(json);

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