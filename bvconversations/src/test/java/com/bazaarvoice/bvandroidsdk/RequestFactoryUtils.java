package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okio.Buffer;

import static com.bazaarvoice.bvandroidsdk.ConversationsRequest.API_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestFactoryUtils {
  private final RequestFactory requestFactory;
  private final GenericFormBodyParams genericFormBodyParams;

  public RequestFactoryUtils(RequestFactory requestFactory) {
    this.requestFactory = requestFactory;
    this.genericFormBodyParams = createGenericFormBodyParams();
  }

  // region Helper Assertions

  public void assertFormBodyContainsKeyValEncoded(Request okRequest, String encodedKey, String encodedVal) throws Exception {
    assertFormBodyContainsKeyValEncoded(okRequest, encodedKey, encodedVal, true);
  }

  public void assertFormBodyContainsKeyValEncoded(Request okRequest, String encodedKey, String encodedVal, boolean contains) throws Exception {
    assertTrue("Body must be of type FormBody", okRequest.body() instanceof FormBody);
    final Buffer buffer = new Buffer();
    okRequest.body().writeTo(buffer);
    final String requestBody = buffer.readUtf8();
    final FormBody formBody = (FormBody) okRequest.body();
    for (int i=0; i<formBody.size(); i++) {
      String currKey = formBody.encodedName(i);
      String currValue = formBody.encodedValue(i);
      if (currKey.equals(encodedKey) && currValue.equals(encodedVal)) {
        if (!contains) {
          fail("Found unexpected key-value pair in FormBody, "   + encodedKey + "=" + encodedVal + "\n\nActual FormBody:\n" + requestBody);
        } else {
          return;
        }
      }
    }
    if (contains) {
      fail("Could not find expected key-value pair in FormBody, " + encodedKey + "=" + encodedVal + "\n\nActual FormBody:\n" + requestBody);
    }
  }

  public void assertFormBodyContainsKeyEncoded(Request okRequest, String encodedKey, boolean contains) throws Exception {
    assertTrue("Body must be of type FormBody", okRequest.body() instanceof FormBody);
    final Buffer buffer = new Buffer();
    okRequest.body().writeTo(buffer);
    final String requestBody = buffer.readUtf8();
    final FormBody formBody = (FormBody) okRequest.body();
    for (int i=0; i<formBody.size(); i++) {
      String currKey = formBody.encodedName(i);
      String currValue = formBody.encodedValue(i);
      if (currKey.equals(encodedKey)) {
        if (!contains) {
          fail("Found unexpected key-value pair in FormBody, "   + encodedKey + "=" + currValue + "\n\nActual FormBody:\n" + requestBody);
        } else {
          return;
        }
      }
    }
    if (contains) {
      fail("Could not find expected key in FormBody, " + encodedKey + "\n\nActual FormBody:\n" + requestBody);
    }
  }

  public void assertContainsPagingProperties(HttpUrl url, int limit, int offset) throws Exception {
    assertEquals(String.valueOf(limit), url.queryParameter("Limit"));
    assertEquals(String.valueOf(offset), url.queryParameter("Offset"));
  }

  public void assertContainsPagingProperties(Request okRequest, int limit, int offset) throws Exception {
    final HttpUrl url = okRequest.url();
    assertEquals(String.valueOf(limit), url.queryParameter("Limit"));
    assertEquals(String.valueOf(offset), url.queryParameter("Offset"));
  }

  public void assertContainsGenericProperties(Request okRequest) throws Exception {
    assertContainsGenericQueryParams(okRequest.url());
    assertEquals("Android-TestDk", okRequest.header("User-Agent"));
    assertEquals("convApiKeyFoo", okRequest.url().queryParameter("passkey"));
  }

  public void assertContainsGenericStoreProperties(Request okRequest) throws Exception {
    assertContainsGenericQueryParams(okRequest.url());
    assertEquals("Android-TestDk", okRequest.header("User-Agent"));
    assertEquals("storeApiKeyBar", okRequest.url().queryParameter("passkey"));
  }

  public void assertContainsGenericQueryParams(HttpUrl url) throws Exception {
    assertEquals(API_VERSION, url.queryParameter("apiversion"));
    assertEquals("3.2.1", url.queryParameter("_appId"));
    assertEquals("21", url.queryParameter("_appVersion"));
    assertEquals("1", url.queryParameter("_buildNumber"));
    assertEquals("bvsdk7", url.queryParameter("_bvAndroidSdkVersion"));
  }

  public void assertContainsBVSDKFormParams(Request okRequest) throws Exception {
    assertContainsBVSDKFormParams(okRequest, "convApiKeyFoo");
  }

  public void assertContainsBVSDKFormParams(Request okRequest, String passkey) throws Exception {
    assertEquals("Android-TestDk", okRequest.header("User-Agent"));
    assertFormBodyContainsKeyValEncoded(okRequest, "apiversion", API_VERSION);
    assertFormBodyContainsKeyValEncoded(okRequest, "passkey", passkey);
  }

  public void assertContainsBVMobileInfoFormParams(Request okRequest) throws Exception {
    assertFormBodyContainsKeyValEncoded(okRequest, "_appId", "3.2.1");
    assertFormBodyContainsKeyValEncoded(okRequest, "_appVersion", "21");
    assertFormBodyContainsKeyValEncoded(okRequest, "_buildNumber", "1");
    assertFormBodyContainsKeyValEncoded(okRequest, "_bvAndroidSdkVersion", "bvsdk7");
  }

  public void assertContainsConversationsSubmissionRequestFormParams(Request okRequest) throws Exception {
    assertFormBodyContainsKeyValEncoded(okRequest, "UserId", genericFormBodyParams.getUserId());
    assertFormBodyContainsKeyValEncoded(okRequest, "UserNickname", genericFormBodyParams.getUserNickname());
    assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_authenticationemail", "email%40email.com");
    assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_callbackurl", "https%3A%2F%2Fwww.callback.url");
    assertFormBodyContainsKeyValEncoded(okRequest, "campaignid", genericFormBodyParams.getCampaignId());
    assertFormBodyContainsKeyValEncoded(okRequest, "locale", genericFormBodyParams.getLocale());
    assertFormBodyContainsKeyValEncoded(okRequest, "UserEmail", "user%40email.com");
    assertFormBodyContainsKeyValEncoded(okRequest, "UserLocation", "Austin%2C%20TX");
    assertFormBodyContainsKeyValEncoded(okRequest, "sendemailalertwhenpublished", genericFormBodyParams.getSendEmailAlert());
    assertFormBodyContainsKeyValEncoded(okRequest, "agreedToTermsAndConditions", genericFormBodyParams.getAgreedToTAndC());
  }

  public void assertFinalPathIs(HttpUrl url, String finalPath) {
    assertEquals(finalPath, url.pathSegments().get(url.pathSegments().size() - 1));
  }
  // endregion

  // region Stub Data
  public static BasicRequestFactory createTestRequestFactory() {
    final BVMobileInfo bvMobileInfo = mock(BVMobileInfo.class);
    when(bvMobileInfo.getBvSdkVersion()).thenReturn("bvsdk7");
    when(bvMobileInfo.getMobileAppCode()).thenReturn("1");
    when(bvMobileInfo.getMobileAppIdentifier()).thenReturn("3.2.1");
    when(bvMobileInfo.getMobileAppVersion()).thenReturn("21");
    when(bvMobileInfo.getMobileDeviceName()).thenReturn("test_device");
    when(bvMobileInfo.getMobileOs()).thenReturn("Android");
    when(bvMobileInfo.getMobileOsVersion()).thenReturn("0.1.2");
    final String rootApiUrl = "https://api.bazaarvoice.com/";
    final BVRootApiUrls bvRootApiUrls = mock(BVRootApiUrls.class);
    when(bvRootApiUrls.getBazaarvoiceApiRootUrl()).thenReturn(rootApiUrl);
    final String convApiKey = "convApiKeyFoo";
    final String storeApiKey = "storeApiKeyBar";
    final String bvSdkUserAgent = "Android-TestDk";
    final BVSDK bvsdk = mock(BVSDK.class);
    final BVConfig bvConfig = new BVConfig.Builder()
        .apiKeyConversations(convApiKey)
        .apiKeyConversationsStores(storeApiKey)
        .clientId("yoyomakers")
        .build();
    final FingerprintProvider fingerprintProvider = new FingerprintProvider() {
      @Override
      public String getFingerprint() {
        return "test_fp";
      }
    };
    return new BasicRequestFactory(bvMobileInfo, bvRootApiUrls, bvConfig, bvSdkUserAgent, fingerprintProvider);
  }

  public List<PhotoUpload> createPhotoList(PhotoUpload.ContentType contentType) {
    List<PhotoUpload> photos = new ArrayList<>();
    PhotoUpload photoUpload1 = new PhotoUpload(new File("file://path/to/photo1.jpg"), "Summer sunset, on the beach", contentType);
    photos.add(photoUpload1);
    PhotoUpload photoUpload2 = new PhotoUpload(new File("file://other/path/to/photo2.jpg"), ">:D 100%", contentType);
    photos.add(photoUpload2);
    return photos;
  }

  public Request createFullReviewDisplayRequest() {
    final ReviewsRequest request = new ReviewsRequest.Builder("prod1", 10, 2)
        .addIncludeContent(ReviewIncludeType.PRODUCTS)
        .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
        .addFilter(ReviewOptions.Filter.AuthorId, EqualityOperator.EQ, "me")
        .build();
    return requestFactory.create(request);
  }

  public Request createFullReviewDisplayWithAllIncludeTypesRequest() {
    final ReviewsRequest request = new ReviewsRequest.Builder("prod1", 10, 2)
        .addIncludeContent(   ReviewIncludeType.PRODUCTS,
                ReviewIncludeType.AUTHORS,
                ReviewIncludeType.CATEGORIES,
                ReviewIncludeType.COMMENTS)
        .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
        .addFilter(ReviewOptions.Filter.AuthorId, EqualityOperator.EQ, "me")
        .build();
    return requestFactory.create(request);
  }

  public Request createFullReviewDisplayWithAllStatTypesRequest() {
    final ReviewsRequest request = new ReviewsRequest.Builder("prod1", 10, 2)
            .addPDPContentType(
                    PDPContentType.Questions,
                    PDPContentType.Answers,
                    PDPContentType.Reviews)
            .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
            .addFilter(ReviewOptions.Filter.AuthorId, EqualityOperator.EQ, "me")
            .build();
    return requestFactory.create(request);
  }

  public Request createFullQnaDisplayRequest() {
    final QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder("product1", 10, 0)
        .addQuestionSort(QuestionOptions.Sort.ProductId, SortOrder.DESC)
        .addFilter(QuestionOptions.Filter.HasVideos, EqualityOperator.EQ, "true")
        .build();
    return requestFactory.create(request);
  }

  public Request createFullCommentsRequest() {
    final CommentsRequest request = new CommentsRequest.Builder("review1", 20, 0)
        .addSort(CommentOptions.Sort.SUBMISSION_TIME, SortOrder.ASC)
        .addIncludeContent(CommentIncludeType.REVIEWS, 10)
        .addFilter(CommentOptions.Filter.IS_FEATURED, EqualityOperator.EQ, "false")
        .build();
    return requestFactory.create(request);
  }

  public Request createFullAuthorsRequest() {
    final AuthorsRequest request = new AuthorsRequest.Builder("author1")
        .addIncludeContent(AuthorIncludeType.REVIEWS, 11)
        .addIncludeStatistics(AuthorIncludeType.QUESTIONS)
        .addQuestionSort(QuestionOptions.Sort.LastModificationTime, SortOrder.DESC)
        .addAnswerSort(AnswerOptions.Sort.IsBestAnswer, SortOrder.ASC)
        .addReviewSort(ReviewOptions.Sort.IsFeatured, SortOrder.DESC)
        .build();
    return requestFactory.create(request);
  }

  public Request createFullStoreReviewRequest() {
    final StoreReviewsRequest request = new StoreReviewsRequest.Builder("store1", 8, 2)
        .addIncludeContent(ReviewIncludeType.PRODUCTS)
        .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.ASC)
        .addFilter(ReviewOptions.Filter.AuthorId, EqualityOperator.EQ, "me")
        .build();
    return requestFactory.create(request);
  }

  public Request createFullBulkStoreRequest() {
    final List<String> storeIds = new ArrayList<String>();
    storeIds.add("store1");
    storeIds.add("store2");
    final BulkStoreRequest request = new BulkStoreRequest.Builder(storeIds)
        .addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, "esp")
        .build();
    final Request okRequest = requestFactory.create(request);
    return okRequest;
  }

  public Request createFullBulkRatingsRequest() {
    final List<String> productIds = new ArrayList<String>();
    productIds.add("product1");
    productIds.add("product2");
    final BulkRatingsRequest request = new BulkRatingsRequest.Builder(productIds, BulkRatingOptions.StatsType.NativeReviews)
        .addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, "den")
        .build();
    final Request okRequest = requestFactory.create(request);
    return okRequest;
  }

  public Request createFullBulkProductRequest() {
    final List<String> productIds = new ArrayList<String>();
    productIds.add("product1");
    productIds.add("product2");
    // TODO: Seems broken, would expect to be able to pass it product ids? We also don't have and example for this on our public doc website
    final BulkProductRequest request = new BulkProductRequest.Builder()
        .addFilter(ProductOptions.Filter.AverageOverallRating, EqualityOperator.GTE, "3")
        .addFilter(ProductOptions.Filter.IsActive, EqualityOperator.EQ, "true")
        .addIncludeContent(PDPContentType.Reviews, 5)
        .addIncludeContent(PDPContentType.Questions, 3)
        .addReviewSort(ReviewOptions.Sort.HasPhotos, SortOrder.DESC)
        .addIncludeStatistics(PDPContentType.Reviews)
        .build();
    final Request okRequest = requestFactory.create(request);
    return okRequest;
  }

  public Request createFullFeedbackSubmissionRequest() {
    final FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder("review1")
        .feedbackContentType(FeedbackContentType.REVIEW)
        .feedbackType(FeedbackType.HELPFULNESS)
        .feedbackVote(FeedbackVoteType.POSITIVE)
        .reasonFlaggedText("Fantastic review. Would recommend 100%!")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .fingerPrint(genericFormBodyParams.getFingerprint())
        .authenticationProvider(genericFormBodyParams.getAuthProvider())
        .user(genericFormBodyParams.getUser())
        .hostedAuthenticationEmail(genericFormBodyParams.getHostedAuthEmail())
        .hostedAuthenticationCallback(genericFormBodyParams.getHostedAuthCbUrl())
        .campaignId(genericFormBodyParams.getCampaignId())
        .locale(genericFormBodyParams.getLocale())
        .userEmail(genericFormBodyParams.getUserEmail())
        .userLocation(genericFormBodyParams.getUserLocation())
        .sendEmailAlertWhenPublished(Boolean.parseBoolean(genericFormBodyParams.getSendEmailAlert()))
        .agreedToTermsAndConditions(Boolean.parseBoolean(genericFormBodyParams.getAgreedToTAndC()))
        .build();
    return requestFactory.create(request);
  }

  public Request createFullReviewSubmissionRequest(Action action) {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(action, "prod1")
        .isRecommended(true)
        .sendEmailAlertWhenCommented(true)
        .rating(4)
        .netPromoterScore(3)
        .title("This is great!")
        .reviewText("This is something I would recommend 100% :D")
        .netPromoterComment("Something about promotion")
        .addFreeFormTag("foo", "bar")
        .addFreeFormTag("baz", "quux")
        .addPredefinedTag("foo", "bar", "baz")
        .addAdditionalField("foo", "bar")
        .addContextDataValueString("foo", "bar")
        .addRatingSlider("foo", "bar")
        .addRatingQuestion("baz", 2)
        .addVideoUrl("https://www.website.com/video.mp4", "Totes cool 100%!")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .fingerPrint(genericFormBodyParams.getFingerprint())
        .authenticationProvider(genericFormBodyParams.getAuthProvider())
        .user(genericFormBodyParams.getUser())
        .hostedAuthenticationEmail(genericFormBodyParams.getHostedAuthEmail())
        .hostedAuthenticationCallback(genericFormBodyParams.getHostedAuthCbUrl())
        .campaignId(genericFormBodyParams.getCampaignId())
        .locale(genericFormBodyParams.getLocale())
        .userEmail(genericFormBodyParams.getUserEmail())
        .userLocation(genericFormBodyParams.getUserLocation())
        .sendEmailAlertWhenPublished(Boolean.parseBoolean(genericFormBodyParams.getSendEmailAlert()))
        .agreedToTermsAndConditions(Boolean.parseBoolean(genericFormBodyParams.getAgreedToTAndC()))
        .build();
    return requestFactory.create(request);
  }

  public Request createFullReviewSubmissionRequest() {
    return createFullReviewSubmissionRequest(Action.Preview);
  }

  public Request createFullStoreReviewSubmissionRequest() {
    final StoreReviewSubmissionRequest request = new StoreReviewSubmissionRequest.Builder(Action.Preview, "store1")
        .isRecommended(true)
        .sendEmailAlertWhenCommented(true)
        .rating(4)
        .netPromoterScore(3)
        .title("This is great!")
        .reviewText("This is something I would recommend 100% :D")
        .netPromoterComment("Something about promotion")
        .addFreeFormTag("foo", "bar")
        .addFreeFormTag("baz", "quux")
        .addPredefinedTag("foo", "bar", "baz")
        .addAdditionalField("foo", "bar")
        .addContextDataValueString("foo", "bar")
        .addRatingSlider("foo", "bar")
        .addRatingQuestion("baz", 2)
        .addVideoUrl("https://www.website.com/video.mp4", "Totes cool 100%!")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .fingerPrint(genericFormBodyParams.getFingerprint())
        .user(genericFormBodyParams.getUser())
        .authenticationProvider(genericFormBodyParams.getAuthProvider())
        .hostedAuthenticationEmail(genericFormBodyParams.getHostedAuthEmail())
        .hostedAuthenticationCallback(genericFormBodyParams.getHostedAuthCbUrl())
        .campaignId(genericFormBodyParams.getCampaignId())
        .locale(genericFormBodyParams.getLocale())
        .userEmail(genericFormBodyParams.getUserEmail())
        .userLocation(genericFormBodyParams.getUserLocation())
        .sendEmailAlertWhenPublished(Boolean.parseBoolean(genericFormBodyParams.getSendEmailAlert()))
        .agreedToTermsAndConditions(Boolean.parseBoolean(genericFormBodyParams.getAgreedToTAndC()))
        .build();
    return requestFactory.create(request);
  }

  public Request createFullQuestionSubmissionRequest() {
    final QuestionSubmissionRequest request = new QuestionSubmissionRequest.Builder(Action.Preview, "prod1")
        .questionSummary("Does it have over 9000% UV protection?")
        .questionDetails("Percentage being 9001% or higher")
        .isUserAnonymous(true)
        .sendEmailAlertWhenAnswered(true)
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .fingerPrint(genericFormBodyParams.getFingerprint())
        .authenticationProvider(genericFormBodyParams.getAuthProvider())
        .user(genericFormBodyParams.getUser())
        .hostedAuthenticationEmail(genericFormBodyParams.getHostedAuthEmail())
        .hostedAuthenticationCallback(genericFormBodyParams.getHostedAuthCbUrl())
        .campaignId(genericFormBodyParams.getCampaignId())
        .locale(genericFormBodyParams.getLocale())
        .userEmail(genericFormBodyParams.getUserEmail())
        .userLocation(genericFormBodyParams.getUserLocation())
        .sendEmailAlertWhenPublished(Boolean.parseBoolean(genericFormBodyParams.getSendEmailAlert()))
        .agreedToTermsAndConditions(Boolean.parseBoolean(genericFormBodyParams.getAgreedToTAndC()))
        .build();
    return requestFactory.create(request);
  }

  public Request createFullAnswerSubmissionRequest() {
    final AnswerSubmissionRequest request = new AnswerSubmissionRequest.Builder(Action.Preview, "question1", "Some great answer $% here")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .fingerPrint(genericFormBodyParams.getFingerprint())
        .authenticationProvider(genericFormBodyParams.getAuthProvider())
        .user(genericFormBodyParams.getUser())
        .hostedAuthenticationEmail(genericFormBodyParams.getHostedAuthEmail())
        .hostedAuthenticationCallback(genericFormBodyParams.getHostedAuthCbUrl())
        .campaignId(genericFormBodyParams.getCampaignId())
        .locale(genericFormBodyParams.getLocale())
        .userEmail(genericFormBodyParams.getUserEmail())
        .userLocation(genericFormBodyParams.getUserLocation())
        .sendEmailAlertWhenPublished(Boolean.parseBoolean(genericFormBodyParams.getSendEmailAlert()))
        .agreedToTermsAndConditions(Boolean.parseBoolean(genericFormBodyParams.getAgreedToTAndC()))
        .build();
    return requestFactory.create(request);
  }

  public Request createFullCommentSubmissionRequest() {
    final CommentSubmissionRequest request = new CommentSubmissionRequest.Builder(Action.Preview, "review1", "My #commenthere")
        .title("Something **great**!")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .fingerPrint(genericFormBodyParams.getFingerprint())
        .authenticationProvider(genericFormBodyParams.getAuthProvider())
        .user(genericFormBodyParams.getUser())
        .hostedAuthenticationEmail(genericFormBodyParams.getHostedAuthEmail())
        .hostedAuthenticationCallback(genericFormBodyParams.getHostedAuthCbUrl())
        .campaignId(genericFormBodyParams.getCampaignId())
        .locale(genericFormBodyParams.getLocale())
        .userEmail(genericFormBodyParams.getUserEmail())
        .userLocation(genericFormBodyParams.getUserLocation())
        .sendEmailAlertWhenPublished(Boolean.parseBoolean(genericFormBodyParams.getSendEmailAlert()))
        .agreedToTermsAndConditions(Boolean.parseBoolean(genericFormBodyParams.getAgreedToTAndC()))
        .build();
    return requestFactory.create(request);
  }

  public Request createFullUserAuthTokenSubmissionRequest() {
    final UserAuthenticationStringRequest request = new UserAuthenticationStringRequest("testAuthToken");
    return requestFactory.create(request);
  }

  public GenericFormBodyParams createGenericFormBodyParams() {
    return new GenericFormBodyParams(
        "fooser",
        "nickelname",
        "foongerprint",
        "email@email.com",
        "https://www.callback.url",
        "campaignNumeroUno",
        "en-us",
        "youser",
        "user@email.com",
        "Austin, TX",
        "true",
        "false");
  }

  private static final class GenericFormBodyParams {
    private final String userId, userNickname, fingerprint,
        hostedAuthEmail, hostedAuthCbUrl, campaignId,
        locale, user, userEmail,
        userLocation, sendEmailAlert, agreedToTAndC;

    public GenericFormBodyParams(String userId, String userNickname, String fingerprint, String hostedAuthEmail, String hostedAuthCbUrl, String campaignId, String locale, String user, String userEmail, String userLocation, String sendEmailAlert, String agreedToTAndC) {
      this.userId = userId;
      this.userNickname = userNickname;
      this.fingerprint = fingerprint;
      this.hostedAuthEmail = hostedAuthEmail;
      this.hostedAuthCbUrl = hostedAuthCbUrl;
      this.campaignId = campaignId;
      this.locale = locale;
      this.user = user;
      this.userEmail = userEmail;
      this.userLocation = userLocation;
      this.sendEmailAlert = sendEmailAlert;
      this.agreedToTAndC = agreedToTAndC;
    }

    public String getUserId() {
      return userId;
    }

    public String getUserNickname() {
      return userNickname;
    }

    public String getFingerprint() {
      return fingerprint;
    }

    public AuthenticationProvider getAuthProvider() {
      return new BVHostedAuthenticationProvider(hostedAuthEmail, hostedAuthCbUrl);
    }

    public String getHostedAuthEmail() {
      return hostedAuthEmail;
    }

    public String getHostedAuthCbUrl() {
      return hostedAuthCbUrl;
    }

    public String getCampaignId() {
      return campaignId;
    }

    public String getLocale() {
      return locale;
    }

    public String getUser() {
      return user;
    }

    public String getUserEmail() {
      return userEmail;
    }

    public String getUserLocation() {
      return userLocation;
    }

    public String getSendEmailAlert() {
      return sendEmailAlert;
    }

    public String getAgreedToTAndC() {
      return agreedToTAndC;
    }

    public static String encode(String input) throws Exception {
      return URLEncoder.encode(input, "UTF-8")
          .replaceAll("\\+", "%20");
    }
  }
  // endregion
}
