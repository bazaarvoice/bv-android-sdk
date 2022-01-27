package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.Request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ProgressiveSubmissionRequestsTests {

    private RequestFactory requestFactory;
    private RequestFactoryUtils requestFactoryUtils;

    @Before
    public void setUp() throws Exception {
        this.requestFactory = RequestFactoryUtils.createTestRequestFactory();
        requestFactoryUtils = new RequestFactoryUtils(requestFactory);
    }

    @Test
    public void shouldCreateInitiateSubmitRequest() {
        InitiateSubmitRequest initiateSubmitRequest = new InitiateSubmitRequest.Builder(new ArrayList<>(), "locale")
                .extended(true)
                .agreedToTermsAndConditions(true)
                .authenticationProvider(new SiteAuthenticationProvider("UAS"))
                .build();
        assertNotNull(initiateSubmitRequest);
    }

    @Test
    public void shouldCreateProgressiveSubmitRequest() {
        ProgressiveSubmitRequest progressiveSubmitRequest = new ProgressiveSubmitRequest.Builder(
                "productId",
                "submissionSessionToken ",
                "locale")
                .submissionFields(new HashMap<>())
                .build();
        assertNotNull(progressiveSubmitRequest);
    }

    @Test
    public void shouldCreateValidOkHttpRequestFromInitiateSubmitRequestWithExtended() throws Exception {
        String validUrl = "https://api.bazaarvoice.com/data/initiateSubmit.json?apiversion=5.4&passkey=progressiveSubApiKey&_appId=3.2.1&_appVersion=21&_buildNumber=1&_bvAndroidSdkVersion=bvsdk7&extended=true";
        InitiateSubmitRequest initiateSubmitRequest = new InitiateSubmitRequest.Builder(new ArrayList<>(), "locale")
                .extended(true)
                .agreedToTermsAndConditions(true)
                .authenticationProvider(new SiteAuthenticationProvider("UAS"))
                .build();
        Request request = this.requestFactory.create(initiateSubmitRequest);
        assertEquals(validUrl, request.url().toString());
        assertEquals(request.url().queryParameter("passkey"), "progressiveSubApiKey");
        assertEquals(request.url().queryParameter("extended"), "true");
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "userToken", "UAS", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "locale", "locale", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "agreedToTermsAndConditions", "true", true);
    }

    @Test
    public void shouldCreateValidOkHttpRequestFromInitiateSubmitRequest() throws Exception {
        String validUrl = "https://api.bazaarvoice.com/data/initiateSubmit.json?apiversion=5.4&passkey=progressiveSubApiKey&_appId=3.2.1&_appVersion=21&_buildNumber=1&_bvAndroidSdkVersion=bvsdk7";
        InitiateSubmitRequest initiateSubmitRequest = new InitiateSubmitRequest.Builder(new ArrayList<>(), "locale")
                .agreedToTermsAndConditions(true)
                .authenticationProvider(new SiteAuthenticationProvider("UAS"))
                .build();
        Request request = this.requestFactory.create(initiateSubmitRequest);
        assertEquals(validUrl, request.url().toString());
        assertEquals(request.url().queryParameter("passkey"), "progressiveSubApiKey");
        assertNull(request.url().queryParameter("extended"));
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "userToken", "UAS", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "locale", "locale", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "agreedToTermsAndConditions", "true", true);
    }

    @Test
    public void shouldCreateValidOkHttpRequestFromProgressiveSubmissionRequest() throws Exception {
        String validUrl = "https://api.bazaarvoice.com/data/progressiveSubmit.json?apiversion=5.4&passkey=progressiveSubApiKey&_appId=3.2.1&_appVersion=21&_buildNumber=1&_bvAndroidSdkVersion=bvsdk7";
        HashMap<String, Object> submissionFields = new HashMap<>();
        submissionFields.put("ReviewText", "Review Text");
        ProgressiveSubmitRequest progressiveSubmitRequest = new ProgressiveSubmitRequest.Builder("product1", "submissionSessionToken", "locale")
                .submissionFields(new HashMap<>())
                .agreedToTermsAndConditions(true)
                .authenticationProvider(new SiteAuthenticationProvider("UAS"))
                .submissionFields(submissionFields)
                .build();
        Request request = this.requestFactory.create(progressiveSubmitRequest);
        assertEquals(validUrl, request.url().toString());
        assertEquals(request.url().queryParameter("passkey"), "progressiveSubApiKey");
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "userToken", "UAS", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "locale", "locale", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionFields", "{\"agreedtotermsandconditions\":\"true\",\"ReviewText\":\"Review Text\"}", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionSessionToken", "submissionSessionToken", true);
    }

    @Test
    public void shouldCreateValidOkHttpRequestFromProgressiveSubmissionRequestHostedAuth() throws Exception {
        String validUrl = "https://api.bazaarvoice.com/data/progressiveSubmit.json?hostedauth=true&apiversion=5.4&passkey=progressiveSubApiKey&_appId=3.2.1&_appVersion=21&_buildNumber=1&_bvAndroidSdkVersion=bvsdk7";
        HashMap<String, Object> submissionFields = new HashMap<>();
        submissionFields.put("ReviewText", "Review Text");
        ProgressiveSubmitRequest progressiveSubmitRequest = new ProgressiveSubmitRequest.Builder("product1", "123456abcd", "en_US")
                .agreedToTermsAndConditions(true)
                .submissionFields(submissionFields)
                .hostedAuth(true)
                .build();
        Request request = this.requestFactory.create(progressiveSubmitRequest);
        final HttpUrl url = request.url();
        assertEquals(validUrl, request.url().toString());
        assertEquals(request.url().queryParameter("passkey"), "progressiveSubApiKey");
        assertTrue(url.queryParameterValues("hostedauth").contains("true"));
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "locale", "en_US", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionFields", "{\"agreedtotermsandconditions\":\"true\",\"ReviewText\":\"Review Text\"}", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionSessionToken", "123456abcd", true);

    }

    @Test
    public void shouldCreateValidRequestWithoutTermsAndConditions() throws Exception {
        String validUrl = "https://api.bazaarvoice.com/data/progressiveSubmit.json?apiversion=5.4&passkey=progressiveSubApiKey&_appId=3.2.1&_appVersion=21&_buildNumber=1&_bvAndroidSdkVersion=bvsdk7";
        HashMap<String, Object> submissionFields = new HashMap<>();
        submissionFields.put("ReviewText", "Review Text");
        ProgressiveSubmitRequest progressiveSubmitRequest = new ProgressiveSubmitRequest.Builder("product1", "submissionSessionToken", "locale")
                .submissionFields(new HashMap<>())
                .authenticationProvider(new SiteAuthenticationProvider("UAS"))
                .submissionFields(submissionFields)
                .build();
        Request request = this.requestFactory.create(progressiveSubmitRequest);
        assertEquals(validUrl, request.url().toString());
        assertEquals(request.url().queryParameter("passkey"), "progressiveSubApiKey");
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "userToken", "UAS", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "locale", "locale", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionFields", "{\"ReviewText\":\"Review Text\"}", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionSessionToken", "submissionSessionToken", true);
    }

    @Test
    public void shouldCreateValidRequestWithUserEmail() throws Exception {
        String validUrl = "https://api.bazaarvoice.com/data/progressiveSubmit.json?apiversion=5.4&passkey=progressiveSubApiKey&_appId=3.2.1&_appVersion=21&_buildNumber=1&_bvAndroidSdkVersion=bvsdk7";
        HashMap<String, Object> submissionFields = new HashMap<>();
        submissionFields.put("ReviewText", "Review Text");
        ProgressiveSubmitRequest progressiveSubmitRequest = new ProgressiveSubmitRequest.Builder("product1", "submissionSessionToken", "locale")
                .submissionFields(new HashMap<>())
                .submissionFields(submissionFields)
                .userEmail("test@testuser.com")
                .userId("UserId")
                .build();
        Request request = this.requestFactory.create(progressiveSubmitRequest);
        assertEquals(validUrl, request.url().toString());
        assertEquals(request.url().queryParameter("passkey"), "progressiveSubApiKey");
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "locale", "locale", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionFields", "{\"ReviewText\":\"Review Text\"}", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionSessionToken", "submissionSessionToken", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "userEmail", "test@testuser.com",true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "userId", "UserId",true);
    }

    @Test
    public void shouldCreateValidRequestWithHostedAuth() throws Exception {
        String validUrl = "https://api.bazaarvoice.com/data/progressiveSubmit.json?apiversion=5.4&passkey=progressiveSubApiKey&_appId=3.2.1&_appVersion=21&_buildNumber=1&_bvAndroidSdkVersion=bvsdk7";
        HashMap<String, Object> submissionFields = new HashMap<>();
        BVHostedAuthenticationProvider authenticationProvider = new BVHostedAuthenticationProvider("hostedUAS");
        submissionFields.put("ReviewText", "Review Text");
        ProgressiveSubmitRequest progressiveSubmitRequest = new ProgressiveSubmitRequest.Builder("product1", "submissionSessionToken", "locale")
                .submissionFields(new HashMap<>())
                .submissionFields(submissionFields)
                .authenticationProvider(authenticationProvider)
                .userEmail("test@testuser.com")
                .userId("UserId")
                .build();
        Request request = this.requestFactory.create(progressiveSubmitRequest);
        assertEquals(validUrl, request.url().toString());
        assertEquals(request.url().queryParameter("passkey"), "progressiveSubApiKey");
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "locale", "locale", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionFields", "{\"ReviewText\":\"Review Text\"}", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "submissionSessionToken", "submissionSessionToken", true);
        requestFactoryUtils.assertJsonBodyContainsKeyValEncoded(request, "userToken", "hostedUAS", true);

    }

}
