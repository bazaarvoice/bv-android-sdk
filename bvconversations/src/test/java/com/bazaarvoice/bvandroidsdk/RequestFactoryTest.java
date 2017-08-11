package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;

import org.junit.Before;
import org.junit.Test;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestFactoryTest {
  private RequestFactory requestFactory;
  private GenericFormBodyParams genericFormBodyParams;

  @Before
  public void setUp() throws Exception {
    requestFactory = createTestRequestFactory();
    genericFormBodyParams = createGenericFormBodyParams();
  }

  @Test
  public void displayShouldHaveExtraQueryParams() throws Exception  {
    final ReviewsRequest request = new ReviewsRequest.Builder("some1", 10, 10)
        .addAdditionalField("duck duck", "goose")
        .build();
    final Request okRequest = requestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("duck duck").contains("goose"));
  }

  @Test
  public void displayShouldAllowOneToManyExtraQueryParams() throws Exception {
    final ReviewsRequest request = new ReviewsRequest.Builder("some1", 10, 10)
        .addAdditionalField("foo", "1")
        .addAdditionalField("foo", "2")
        .build();
    final Request okRequest = requestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("foo").contains("1"));
    assertTrue(url.queryParameterValues("foo").contains("2"));
  }

  @Test
  public void reviewDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = createFullReviewDisplayRequest();
    assertContainsGenericProperties(okRequest);
  }

  @Test
  public void reviewDisplayShouldHavePagingQueryParams() throws Exception {
    final Request okRequest = createFullReviewDisplayRequest();
    assertContainsPagingProperties(okRequest, 10, 2);
  }

  @Test
  public void reviewDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = createFullReviewDisplayRequest();
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:prod1"));
    assertEquals("products", url.queryParameter("Include"));
    assertEquals("SubmissionTime:desc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("AuthorId:eq:me"));
  }

  @Test
  public void reviewDisplayWithIncludeProductShouldHaveStatsReviews() {
    // "Stats=Reviews must be used in conjunction with Include=Products"
    // - https://developer.bazaarvoice.com/conversations-api/reference/v5.4/reviews/review-display#requesting-all-reviews-for-a-particular-product-with-review-statistics-(inc.-average-rating)
    final Request okRequest = createFullReviewDisplayRequest();
    final HttpUrl httpUrl = okRequest.url();
    assertTrue(httpUrl.queryParameterNames().contains("Stats"));
    assertTrue(httpUrl.queryParameterValues("Stats").get(0).equals("Reviews"));
  }

  @Test
  public void qnaDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = createFullQnaDisplayRequest();
    assertContainsGenericProperties(okRequest);
  }

  @Test
  public void qnaDisplayShouldHavePagingQueryParams() throws Exception {
    final Request okRequest = createFullQnaDisplayRequest();
    assertContainsPagingProperties(okRequest, 10, 0);
  }

  @Test
  public void qnaDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = createFullQnaDisplayRequest();
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:product1"));
    assertEquals("ProductId:desc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("HasVideos:eq:true"));
  }

  @Test
  public void commentDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = createFullCommentsRequest();
    assertContainsGenericProperties(okRequest);
  }

  @Test
  public void commentDisplayShouldHavePagingQueryParams() throws Exception {
    final Request okRequest = createFullCommentsRequest();
    assertContainsPagingProperties(okRequest, 20, 0);
  }

  @Test
  public void commentDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = createFullCommentsRequest();
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("Filter").contains("ReviewId:eq:review1"));
    assertEquals("SubmissionTime:asc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("IsFeatured:eq:false"));
  }

  @Test
  public void authorDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = createFullAuthorsRequest();
    assertContainsGenericProperties(okRequest);
  }

  @Test
  public void authorDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = createFullAuthorsRequest();
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("Filter").contains("Id:eq:author1"));
    assertEquals("IsFeatured:desc", url.queryParameter("Sort_Reviews"));
    assertEquals("LastModificationTime:desc", url.queryParameter("Sort_Questions"));
    assertEquals("IsBestAnswer:asc", url.queryParameter("Sort_Answers"));
    assertEquals("Reviews", url.queryParameter("Include"));
    assertEquals("11", url.queryParameter("Limit_Reviews"));
    assertEquals("Questions", url.queryParameter("Stats"));
  }

  @Test
  public void createBulkStoreRequest() throws Exception {
    final List<String> storeIds = new ArrayList<String>();
    storeIds.add("store1");
    storeIds.add("store2");
    final BulkStoreRequest request = new BulkStoreRequest.Builder(storeIds)
        .addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, "esp")
        .build();
    final Request okRequest = requestFactory.create(request);
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    assertContainsGenericStoreProperties(okRequest);
    assertTrue(url.queryParameterValues("Filter").contains("Id:eq:store1,store2"));
    assertTrue(url.queryParameterValues("Filter").contains("ContentLocale:eq:esp"));
  }

  @Test
  public void createBulkRatingsRequest() throws Exception {
    final List<String> productIds = new ArrayList<String>();
    productIds.add("product1");
    productIds.add("product2");
    final BulkRatingsRequest request = new BulkRatingsRequest.Builder(productIds, BulkRatingOptions.StatsType.NativeReviews)
        .addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, "den")
        .build();
    final Request okRequest = requestFactory.create(request);
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    assertContainsGenericProperties(okRequest);
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:product1,product2"));
    assertTrue(url.queryParameterValues("Filter").contains("ContentLocale:eq:den"));
    assertEquals("NativeReviews", url.queryParameter("Stats"));
  }

  @Test
  public void createStoreReviewsRequest() throws Exception {
    final Request okRequest = createFullStoreReviewRequest();
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    assertContainsGenericStoreProperties(okRequest);
    assertContainsPagingProperties(url, 8, 2);
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:store1"));
    assertEquals("products", url.queryParameter("Include"));
    assertEquals("SubmissionTime:asc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("AuthorId:eq:me"));
  }

  @Test
  public void storeReviewDisplayWithIncludeProductShouldHaveStatsReviews() {
    // "Stats=Reviews must be used in conjunction with Include=Products"
    // - https://developer.bazaarvoice.com/conversations-api/reference/v5.4/reviews/review-display#requesting-all-reviews-for-a-particular-product-with-review-statistics-(inc.-average-rating)
    final Request okRequest = createFullStoreReviewRequest();
    final HttpUrl httpUrl = okRequest.url();
    assertTrue(httpUrl.queryParameterNames().contains("Stats"));
    assertTrue(httpUrl.queryParameterValues("Stats").get(0).equals("Reviews"));
  }

  @Test
  public void createBulkProductRequest() throws Exception {
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
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    assertContainsGenericProperties(okRequest);
    assertTrue(url.queryParameterValues("Filter").contains("AverageOverallRating:gte:3"));
    assertTrue(url.queryParameterValues("Filter").contains("IsActive:eq:true"));
    assertEquals("Reviews,Questions", url.queryParameter("Include"));
    assertEquals("5", url.queryParameter("Limit_Reviews"));
    assertEquals("3", url.queryParameter("Limit_Questions"));
    assertTrue(url.queryParameterValues("Sort_Reviews").contains("HasPhotos:desc"));
    assertEquals("Reviews", url.queryParameter("Stats"));
  }

  @Test
  public void createProductDisplayPageRequest() throws Exception {
    final ProductDisplayPageRequest request = new ProductDisplayPageRequest.Builder("prod1")
        .addFilter(ProductOptions.Filter.AverageOverallRating, EqualityOperator.GTE, "3")
        .addFilter(ProductOptions.Filter.IsActive, EqualityOperator.EQ, "true")
        .addIncludeContent(PDPContentType.Reviews, 5)
        .addIncludeContent(PDPContentType.Questions, 3)
        .addReviewSort(ReviewOptions.Sort.HasPhotos, SortOrder.DESC)
        .addIncludeStatistics(PDPContentType.Reviews)
        .build();
    final Request okRequest = requestFactory.create(request);
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    assertContainsGenericProperties(okRequest);
    assertTrue(url.queryParameterValues("Filter").contains("Id:eq:prod1"));
    assertTrue(url.queryParameterValues("Filter").contains("AverageOverallRating:gte:3"));
    assertTrue(url.queryParameterValues("Filter").contains("IsActive:eq:true"));
    assertEquals("Reviews,Questions", url.queryParameter("Include"));
    assertEquals("5", url.queryParameter("Limit_Reviews"));
    assertEquals("3", url.queryParameter("Limit_Questions"));
    assertTrue(url.queryParameterValues("Sort_Reviews").contains("HasPhotos:desc"));
    assertEquals("Reviews", url.queryParameter("Stats"));
  }

  @Test
  public void feedbackSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = createFullFeedbackSubmissionRequest();
    System.out.println(okRequest.toString());
    assertNotNull(okRequest);
    assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void feedbackSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = createFullFeedbackSubmissionRequest();
    assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void feedbackSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = createFullFeedbackSubmissionRequest();
    assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void feedbackSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = createFullFeedbackSubmissionRequest();
    assertFormBodyContainsKeyValEncoded(okRequest, "ContentId", "review1");
    assertFormBodyContainsKeyValEncoded(okRequest, "ContentType", "review");
    assertFormBodyContainsKeyValEncoded(okRequest, "FeedbackType", "helpfulness");
    assertFormBodyContainsKeyValEncoded(okRequest, "Vote", "positive");
    assertFormBodyContainsKeyValEncoded(okRequest, "ReasonText", "Fantastic%20review.%20Would%20recommend%20100%25%21");
  }

  @Test
  public void feedbackSubmissionMustAlwaysBePreviewForAction() throws Exception {
    final FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder("content1").build();
    final Request okRequest = requestFactory.create(request);
    System.out.println(okRequest);
    assertFormBodyContainsKeyValEncoded(okRequest, "action", "Preview");
  }

  @Test
  public void reviewSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = createFullReviewSubmissionRequest();
    assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void reviewSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = createFullReviewSubmissionRequest();
    assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void reviewSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = createFullReviewSubmissionRequest();
    assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void reviewSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = createFullReviewSubmissionRequest();

    assertFormBodyContainsKeyValEncoded(okRequest, "ProductId", "prod1");
    assertFormBodyContainsKeyValEncoded(okRequest, "IsRecommended", "true");
    assertFormBodyContainsKeyValEncoded(okRequest, "SendEmailAlertWhenCommented", "true");
    assertFormBodyContainsKeyValEncoded(okRequest, "Rating", "4");
    assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterScore", "3");
    assertFormBodyContainsKeyValEncoded(okRequest, "Title", "This%20is%20great%21");
    assertFormBodyContainsKeyValEncoded(okRequest, "ReviewText", "This%20is%20something%20I%20would%20recommend%20100%25%20%3AD");
    assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterComment", "Something%20about%20promotion");
    assertFormBodyContainsKeyValEncoded(okRequest, "tag_foo_0", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "tag_baz_1", "quux");
    assertFormBodyContainsKeyValEncoded(okRequest, "tagid_foo%2Fbar", "baz");
    assertFormBodyContainsKeyValEncoded(okRequest, "additionalfield_foo", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "contextdatavalue_foo", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "rating_foo", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "rating_baz", "2");
    assertFormBodyContainsKeyValEncoded(okRequest, "VideoUrl_1", "https%3A%2F%2Fwww.website.com%2Fvideo.mp4");
    assertFormBodyContainsKeyValEncoded(okRequest, "VideoCaption_1", "Totes%20cool%20100%25%21");
  }

  @Test
  public void submissionShouldNotAllowNullFormParams() throws Exception {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Preview, "prod1")
        .isRecommended(null)
        .build();
    final Request okRequest = requestFactory.create(request);
    assertFormBodyContainsKeyEncoded(okRequest, "IsRecommended", false);
  }

  @Test
  public void storeReviewSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = createFullStoreReviewSubmissionRequest();
    assertContainsBVSDKFormParams(okRequest, "storeApiKeyBar");
  }

  @Test
  public void storeReviewSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = createFullStoreReviewSubmissionRequest();
    assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void storeReviewSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = createFullStoreReviewSubmissionRequest();
    assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void storeReviewSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = createFullStoreReviewSubmissionRequest();

    assertFormBodyContainsKeyValEncoded(okRequest, "ProductId", "store1");
    assertFormBodyContainsKeyValEncoded(okRequest, "IsRecommended", "true");
    assertFormBodyContainsKeyValEncoded(okRequest, "SendEmailAlertWhenCommented", "true");
    assertFormBodyContainsKeyValEncoded(okRequest, "Rating", "4");
    assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterScore", "3");
    assertFormBodyContainsKeyValEncoded(okRequest, "Title", "This%20is%20great%21");
    assertFormBodyContainsKeyValEncoded(okRequest, "ReviewText", "This%20is%20something%20I%20would%20recommend%20100%25%20%3AD");
    assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterComment", "Something%20about%20promotion");
    assertFormBodyContainsKeyValEncoded(okRequest, "tag_foo_0", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "tag_baz_1", "quux");
    assertFormBodyContainsKeyValEncoded(okRequest, "tagid_foo%2Fbar", "baz");
    assertFormBodyContainsKeyValEncoded(okRequest, "additionalfield_foo", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "contextdatavalue_foo", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "rating_foo", "bar");
    assertFormBodyContainsKeyValEncoded(okRequest, "rating_baz", "2");
    assertFormBodyContainsKeyValEncoded(okRequest, "VideoUrl_1", "https%3A%2F%2Fwww.website.com%2Fvideo.mp4");
    assertFormBodyContainsKeyValEncoded(okRequest, "VideoCaption_1", "Totes%20cool%20100%25%21");
  }

  @Test
  public void questionSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = createFullQuestionSubmissionRequest();
    assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void questionSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = createFullQuestionSubmissionRequest();
    assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void questionSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = createFullQuestionSubmissionRequest();

    /**
     * queryParams.put(kPRODUCT_ID, productId);
     queryParams.put(kQUESTION_SUMMARY, questionSummary);
     queryParams.put(kQUESTION_DETAILS, questionDetails);
     queryParams.put(kIS_ANONUSER, isUserAnonymous);
     queryParams.put(kSEND_EMAIL_ANSWERED, sendEmailAlertWhenAnswered);
     */
    assertFormBodyContainsKeyValEncoded(okRequest, "ProductId", "prod1");
    assertFormBodyContainsKeyValEncoded(okRequest, "QuestionSummary", "Does%20it%20have%20over%209000%25%20UV%20protection%3F");
    assertFormBodyContainsKeyValEncoded(okRequest, "QuestionDetails", "Percentage%20being%209001%25%20or%20higher");
    assertFormBodyContainsKeyValEncoded(okRequest, "IsUserAnonymous", "true");
    assertFormBodyContainsKeyValEncoded(okRequest, "SendEmailAlertWhenAnswered", "true");
  }

  @Test
  public void answerSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = createFullAnswerSubmissionRequest();
    assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void answerSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = createFullAnswerSubmissionRequest();
    assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void answerSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = createFullAnswerSubmissionRequest();
    assertFormBodyContainsKeyValEncoded(okRequest, "QuestionId", "question1");
    assertFormBodyContainsKeyValEncoded(okRequest, "AnswerText", "Some%20great%20answer%20%24%25%20here");
  }

  @Test
  public void commentSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = createFullCommentSubmissionRequest();
    assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void commentSubmissionshouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = createFullCommentSubmissionRequest();
    assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void commentSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = createFullCommentSubmissionRequest();
    assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void commentSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = createFullCommentSubmissionRequest();
    assertFormBodyContainsKeyValEncoded(okRequest, "ReviewId", "review1");
    assertFormBodyContainsKeyValEncoded(okRequest, "CommentText", "My%20%23commenthere");
    assertFormBodyContainsKeyValEncoded(okRequest, "Title", "Something%20**great**%21");
  }

  // region Helper Assertions

  private void assertFormBodyContainsKeyValEncoded(Request okRequest, String encodedKey, String encodedVal) throws Exception {
    assertFormBodyContainsKeyValEncoded(okRequest, encodedKey, encodedVal, true);
  }

  private void assertFormBodyContainsKeyValEncoded(Request okRequest, String encodedKey, String encodedVal, boolean contains) throws Exception {
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

  private void assertFormBodyContainsKeyEncoded(Request okRequest, String encodedKey, boolean contains) throws Exception {
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

  private void assertContainsPagingProperties(HttpUrl url, int limit, int offset) throws Exception {
    assertEquals(String.valueOf(limit), url.queryParameter("Limit"));
    assertEquals(String.valueOf(offset), url.queryParameter("Offset"));
  }

  private void assertContainsPagingProperties(Request okRequest, int limit, int offset) throws Exception {
    final HttpUrl url = okRequest.url();
    assertEquals(String.valueOf(limit), url.queryParameter("Limit"));
    assertEquals(String.valueOf(offset), url.queryParameter("Offset"));
  }

  private void assertContainsGenericProperties(Request okRequest) throws Exception {
    assertContainsGenericQueryParams(okRequest.url());
    assertEquals("Android-TestDk", okRequest.header("User-Agent"));
    assertEquals("convApiKeyFoo", okRequest.url().queryParameter("passkey"));
  }

  private void assertContainsGenericStoreProperties(Request okRequest) throws Exception {
    assertContainsGenericQueryParams(okRequest.url());
    assertEquals("Android-TestDk", okRequest.header("User-Agent"));
    assertEquals("storeApiKeyBar", okRequest.url().queryParameter("passkey"));
  }

  private void assertContainsGenericQueryParams(HttpUrl url) throws Exception {
    assertEquals(API_VERSION, url.queryParameter("apiversion"));
    assertEquals("3.2.1", url.queryParameter("_appId"));
    assertEquals("21", url.queryParameter("_appVersion"));
    assertEquals("1", url.queryParameter("_buildNumber"));
    assertEquals("bvsdk7", url.queryParameter("_bvAndroidSdkVersion"));
  }

  private void assertContainsBVSDKFormParams(Request okRequest) throws Exception {
    assertContainsBVSDKFormParams(okRequest, "convApiKeyFoo");
  }

  private void assertContainsBVSDKFormParams(Request okRequest, String passkey) throws Exception {
    assertEquals("Android-TestDk", okRequest.header("User-Agent"));
    assertFormBodyContainsKeyValEncoded(okRequest, "apiversion", API_VERSION);
    assertFormBodyContainsKeyValEncoded(okRequest, "passkey", passkey);
  }

  private void assertContainsBVMobileInfoFormParams(Request okRequest) throws Exception {
    assertFormBodyContainsKeyValEncoded(okRequest, "_appId", "3.2.1");
    assertFormBodyContainsKeyValEncoded(okRequest, "_appVersion", "21");
    assertFormBodyContainsKeyValEncoded(okRequest, "_buildNumber", "1");
    assertFormBodyContainsKeyValEncoded(okRequest, "_bvAndroidSdkVersion", "bvsdk7");
  }

  private void assertContainsConversationsSubmissionRequestFormParams(Request okRequest) throws Exception {
    assertFormBodyContainsKeyValEncoded(okRequest, "UserId", genericFormBodyParams.getUserId());
    assertFormBodyContainsKeyValEncoded(okRequest, "UserNickname", genericFormBodyParams.getUserNickname());
    assertFormBodyContainsKeyValEncoded(okRequest, "fp", genericFormBodyParams.getFingerprint());
    assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_authenticationemail", "email%40email.com");
    assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_callbackurl", "https%3A%2F%2Fwww.callback.url");
    assertFormBodyContainsKeyValEncoded(okRequest, "campaignid", genericFormBodyParams.getCampaignId());
    assertFormBodyContainsKeyValEncoded(okRequest, "locale", genericFormBodyParams.getLocale());
    assertFormBodyContainsKeyValEncoded(okRequest, "User", genericFormBodyParams.getUser());
    assertFormBodyContainsKeyValEncoded(okRequest, "UserEmail", "user%40email.com");
    assertFormBodyContainsKeyValEncoded(okRequest, "UserLocation", "Austin%2C%20TX");
    assertFormBodyContainsKeyValEncoded(okRequest, "sendemailalertwhenpublished", genericFormBodyParams.getSendEmailAlert());
    assertFormBodyContainsKeyValEncoded(okRequest, "agreedToTermsAndConditions", genericFormBodyParams.getAgreedToTAndC());
  }
  // endregion

  // region Stub Data
  private RequestFactory createTestRequestFactory() {
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
    return new RequestFactory(bvMobileInfo, bvRootApiUrls, bvConfig, bvSdkUserAgent);
  }

  private List<PhotoUpload> createPhotoList(PhotoUpload.ContentType contentType) {
    List<PhotoUpload> photos = new ArrayList<>();
    PhotoUpload photoUpload1 = new PhotoUpload(new File("file://path/to/photo1.jpg"), "Summer sunset, on the beach", contentType);
    photos.add(photoUpload1);
    PhotoUpload photoUpload2 = new PhotoUpload(new File("file://other/path/to/photo2.jpg"), ">:D 100%", contentType);
    photos.add(photoUpload2);
    return photos;
  }

  private Request createFullReviewDisplayRequest() {
    final ReviewsRequest request = new ReviewsRequest.Builder("prod1", 10, 2)
        .addIncludeContent(ReviewIncludeType.PRODUCTS)
        .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC)
        .addFilter(ReviewOptions.Filter.AuthorId, EqualityOperator.EQ, "me")
        .build();
    return requestFactory.create(request);
  }

  private Request createFullQnaDisplayRequest() {
    final QuestionAndAnswerRequest request = new QuestionAndAnswerRequest.Builder("product1", 10, 0)
        .addQuestionSort(QuestionOptions.Sort.ProductId, SortOrder.DESC)
        .addFilter(QuestionOptions.Filter.HasVideos, EqualityOperator.EQ, "true")
        .build();
    return requestFactory.create(request);
  }

  private Request createFullCommentsRequest() {
    final CommentsRequest request = new CommentsRequest.Builder("review1", 20, 0)
        .addSort(CommentOptions.Sort.SUBMISSION_TIME, SortOrder.ASC)
        .addIncludeContent(CommentIncludeType.REVIEWS, 10)
        .addFilter(CommentOptions.Filter.IS_FEATURED, EqualityOperator.EQ, "false")
        .build();
    return requestFactory.create(request);
  }

  private Request createFullAuthorsRequest() {
    final AuthorsRequest request = new AuthorsRequest.Builder("author1")
        .addIncludeContent(AuthorIncludeType.REVIEWS, 11)
        .addIncludeStatistics(AuthorIncludeType.QUESTIONS)
        .addQuestionSort(QuestionOptions.Sort.LastModificationTime, SortOrder.DESC)
        .addAnswerSort(AnswerOptions.Sort.IsBestAnswer, SortOrder.ASC)
        .addReviewSort(ReviewOptions.Sort.IsFeatured, SortOrder.DESC)
        .build();
    return requestFactory.create(request);
  }

  private Request createFullStoreReviewRequest() {
    final StoreReviewsRequest request = new StoreReviewsRequest.Builder("store1", 8, 2)
        .addIncludeContent(ReviewIncludeType.PRODUCTS)
        .addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.ASC)
        .addFilter(ReviewOptions.Filter.AuthorId, EqualityOperator.EQ, "me")
        .build();
    return requestFactory.create(request);
  }

  private Request createFullFeedbackSubmissionRequest() {
    final FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder("review1")
        .feedbackContentType(FeedbackContentType.REVIEW)
        .feedbackType(FeedbackType.HELPFULNESS)
        .feedbackVote(FeedbackVoteType.POSITIVE)
        .reasonFlaggedText("Fantastic review. Would recommend 100%!")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .user(genericFormBodyParams.getUser())
        .fingerPrint(genericFormBodyParams.getFingerprint())
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

  private Request createFullReviewSubmissionRequest() {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Preview, "prod1")
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
        .user(genericFormBodyParams.getUser())
        .fingerPrint(genericFormBodyParams.getFingerprint())
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

  private Request createFullStoreReviewSubmissionRequest() {
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
        .user(genericFormBodyParams.getUser())
        .fingerPrint(genericFormBodyParams.getFingerprint())
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

  private Request createFullQuestionSubmissionRequest() {
    final QuestionSubmissionRequest request = new QuestionSubmissionRequest.Builder(Action.Preview, "prod1")
        .questionSummary("Does it have over 9000% UV protection?")
        .questionDetails("Percentage being 9001% or higher")
        .isUserAnonymous(true)
        .sendEmailAlertWhenAnswered(true)
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .user(genericFormBodyParams.getUser())
        .fingerPrint(genericFormBodyParams.getFingerprint())
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

  private Request createFullAnswerSubmissionRequest() {
    final AnswerSubmissionRequest request = new AnswerSubmissionRequest.Builder(Action.Preview, "question1", "Some great answer $% here")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .user(genericFormBodyParams.getUser())
        .fingerPrint(genericFormBodyParams.getFingerprint())
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

  private Request createFullCommentSubmissionRequest() {
    final CommentSubmissionRequest request = new CommentSubmissionRequest.Builder(Action.Preview, "review1", "My #commenthere")
        .title("Something **great**!")
        .userId(genericFormBodyParams.getUserId())
        .userNickname(genericFormBodyParams.getUserNickname())
        .user(genericFormBodyParams.getUser())
        .fingerPrint(genericFormBodyParams.getFingerprint())
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

  private GenericFormBodyParams createGenericFormBodyParams() {
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