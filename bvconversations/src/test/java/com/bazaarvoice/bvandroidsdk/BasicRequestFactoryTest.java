package com.bazaarvoice.bvandroidsdk;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BasicRequestFactoryTest {
  private RequestFactory requestFactory;
  private RequestFactoryUtils requestFactoryUtils;

  @Before
  public void setUp() throws Exception {
    this.requestFactory = RequestFactoryUtils.createTestRequestFactory();
    requestFactoryUtils = new RequestFactoryUtils(requestFactory);
  }

  @Test
  public void displayShouldHaveExtraQueryParams() throws Exception  {
    final ReviewsRequest request = new ReviewsRequest.Builder("some1", 10, 10)
        .addCustomDisplayParameter("duck duck", "goose")
        .build();
    final Request okRequest = requestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("duck duck").contains("goose"));
  }

  @Test
  public void displayShouldAllowOneToManyExtraQueryParams() throws Exception {
    final ReviewsRequest request = new ReviewsRequest.Builder("some1", 10, 10)
        .addCustomDisplayParameter("foo", "1")
        .addCustomDisplayParameter("foo", "2")
        .build();
    final Request okRequest = requestFactory.create(request);
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("foo").contains("1"));
    assertTrue(url.queryParameterValues("foo").contains("2"));
  }

  @Test
  public void reviewDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewDisplayRequest();
    requestFactoryUtils.assertContainsGenericProperties(okRequest);
  }

  @Test
  public void reviewDisplayShouldHavePagingQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewDisplayRequest();
    requestFactoryUtils.assertContainsPagingProperties(okRequest, 10, 2);
  }

  @Test
  public void reviewDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewDisplayRequest();
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:prod1"));
    assertEquals("products", url.queryParameter("Include"));
    assertEquals("SubmissionTime:desc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("AuthorId:eq:me"));
  }

  @Test
  public void reviewDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewDisplayRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "reviews.json");
  }

  @Test
  public void reviewDisplayWithIncludeProductShouldHaveStatsReviews() {
    // "Stats=Reviews must be used in conjunction with Include=Products"
    // - https://developer.bazaarvoice.com/conversations-api/reference/v5.4/reviews/review-display#requesting-all-reviews-for-a-particular-product-with-review-statistics-(inc.-average-rating)
    final Request okRequest = requestFactoryUtils.createFullReviewDisplayRequest();
    final HttpUrl httpUrl = okRequest.url();
    assertTrue(httpUrl.queryParameterNames().contains("Stats"));
    assertEquals("Reviews,", httpUrl.queryParameterValues("Stats").get(0));
  }

  @Test
  public void reviewDisplayWithMultipleIncludeParametersShouldHaveAllTypes() {
    final Request okRequest = requestFactoryUtils.createFullReviewDisplayWithAllIncludeTypesRequest();
    final HttpUrl httpUrl = okRequest.url();
    assertEquals(httpUrl.queryParameter("Include"), "products,authors,categories,comments");
  }

  @Test
  public void reviewDisplayWithMultipleIncludeParametersShouldSupportMultipleTypes() {
    List<ReviewIncludeType> types = new ArrayList<>();
    types.add(ReviewIncludeType.PRODUCTS);
    types.add(ReviewIncludeType.AUTHORS);
    types.add(ReviewIncludeType.CATEGORIES);
    types.add(ReviewIncludeType.COMMENTS);
    final ReviewsRequest request = new ReviewsRequest.Builder("", 0 ,0)
            .addIncludeContent(
                    ReviewIncludeType.PRODUCTS,
                    ReviewIncludeType.AUTHORS,
                    ReviewIncludeType.CATEGORIES,
                    ReviewIncludeType.COMMENTS
            )
            .build();
    assertEquals(request.getReviewIncludeTypes(), types);
  }

  @Test
  public void reviewDisplayRequestWithAdditionalStatsTypes() {
    List<PDPContentType> statTypes = new ArrayList<>();
    statTypes.add(PDPContentType.Questions);
    statTypes.add(PDPContentType.Answers);
    statTypes.add(PDPContentType.Reviews);
    final ReviewsRequest request = new ReviewsRequest.Builder("", 0, 0)
            .addPDPContentType(
                    PDPContentType.Questions,
                    PDPContentType.Answers,
                    PDPContentType.Reviews)
            .build();
    assertEquals(statTypes, request.getStatistics());
  }

  @Test
  public void reviewDisplayRequestParamsWithAdditionalTypes() {
    final Request okRequest = requestFactoryUtils.createFullReviewDisplayWithAllStatTypesRequest();
    assertEquals(okRequest.url().queryParameter("Stats"), "Questions,Answers,Reviews");

  }

  @Test
  public void reviewDisplayRequestCreateRequestWithFilter() {
    final Request okRequest = requestFactory.create(new ReviewsRequest.Builder(ReviewOptions.PrimaryFilter.AuthorId,  "1234",10, 0)
            .build());
    assertEquals("AuthorId:eq:1234",okRequest.url().queryParameter("Filter"));
}

  @Test
  public void reviewDisplayRequestCreateRequestWithFilterAndProductFilter() {
    final ReviewsRequest.Builder reviewsRequest = new ReviewsRequest.Builder(ReviewOptions.PrimaryFilter.AuthorId, "1234", 10, 0);
    reviewsRequest.addFilter(ReviewOptions.Filter.ProductId, EqualityOperator.EQ, "testproduct1");
    final Request okRequest = requestFactory.create(reviewsRequest.build());
    final List<String> filterParams = okRequest.url().queryParameterValues("Filter");
    assertEquals("AuthorId:eq:1234", filterParams.get(0));
    assertEquals("ProductId:eq:testproduct1", filterParams.get(1));
  }

  @Test
  public void qnaDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQnaDisplayRequest();
    requestFactoryUtils.assertContainsGenericProperties(okRequest);
  }

  @Test
  public void qnaDisplayShouldHavePagingQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQnaDisplayRequest();
    requestFactoryUtils.assertContainsPagingProperties(okRequest, 10, 0);
  }

  @Test
  public void qnaDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQnaDisplayRequest();
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:product1"));
    assertEquals("ProductId:desc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("HasVideos:eq:true"));
  }

  @Test
  public void qnaDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQnaDisplayRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "questions.json");
  }

  @Test
  public void commentDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentsRequest();
    requestFactoryUtils.assertContainsGenericProperties(okRequest);
  }

  @Test
  public void commentDisplayShouldHavePagingQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentsRequest();
    requestFactoryUtils.assertContainsPagingProperties(okRequest, 20, 0);
  }

  @Test
  public void commentDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentsRequest();
    final HttpUrl url = okRequest.url();
    assertTrue(url.queryParameterValues("Filter").contains("ReviewId:eq:review1"));
    assertEquals("SubmissionTime:asc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("IsFeatured:eq:false"));
  }

  @Test
  public void commentsDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentsRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "reviewcomments.json");
  }

  @Test
  public void authorDisplayShouldHaveBVSDKQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAuthorsRequest();
    requestFactoryUtils.assertContainsGenericProperties(okRequest);
  }

  @Test
  public void authorDisplayShouldHaveOwnQueryParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAuthorsRequest();
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
  public void authorDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAuthorsRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "authors.json");
  }

  @Test
  public void createBulkStoreRequest() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullBulkStoreRequest();
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    requestFactoryUtils.assertContainsGenericStoreProperties(okRequest);
    assertTrue(url.queryParameterValues("Filter").contains("Id:eq:store1,store2"));
    assertTrue(url.queryParameterValues("Filter").contains("ContentLocale:eq:esp"));
  }

  @Test
  public void bulkStoreDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullBulkStoreRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "products.json");
  }

  @Test
  public void createBulkRatingsRequest() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullBulkRatingsRequest();
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    requestFactoryUtils.assertContainsGenericProperties(okRequest);
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:product1,product2"));
    assertTrue(url.queryParameterValues("Filter").contains("ContentLocale:eq:den"));
    assertTrue(url.queryParameterValues("incentivizedstats").contains("true"));
    assertEquals("NativeReviews", url.queryParameter("Stats"));
  }

  @Test
  public void bulkRatingsDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullBulkRatingsRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "statistics.json");
  }

  @Test
  public void createStoreReviewsRequest() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewRequest();
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    requestFactoryUtils.assertContainsGenericStoreProperties(okRequest);
    requestFactoryUtils.assertContainsPagingProperties(url, 8, 2);
    assertTrue(url.queryParameterValues("Filter").contains("ProductId:eq:store1"));
    assertEquals("products", url.queryParameter("Include"));
    assertEquals("SubmissionTime:asc", url.queryParameter("Sort"));
    assertTrue(url.queryParameterValues("Filter").contains("AuthorId:eq:me"));
  }

  @Test
  public void storeReviewsDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "reviews.json");
  }

  @Test
  public void storeReviewDisplayWithIncludeProductShouldHaveStatsReviews() {
    // "Stats=Reviews must be used in conjunction with Include=Products"
    // - https://developer.bazaarvoice.com/conversations-api/reference/v5.4/reviews/review-display#requesting-all-reviews-for-a-particular-product-with-review-statistics-(inc.-average-rating)
    final Request okRequest = requestFactoryUtils.createFullStoreReviewRequest();
    final HttpUrl httpUrl = okRequest.url();
    assertTrue(httpUrl.queryParameterNames().contains("Stats"));
    assertEquals("Reviews", httpUrl.queryParameterValues("Stats").get(0));
  }

  @Test
  public void createBulkProductRequest() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullBulkProductRequest();
    assertNotNull(okRequest);
    System.out.println(okRequest.toString());

    final HttpUrl url = okRequest.url();

    requestFactoryUtils.assertContainsGenericProperties(okRequest);
    assertTrue(url.queryParameterValues("Filter").contains("AverageOverallRating:gte:3"));
    assertTrue(url.queryParameterValues("Filter").contains("IsActive:eq:true"));
    assertEquals("Reviews,Questions", url.queryParameter("Include"));
    assertEquals("5", url.queryParameter("Limit_Reviews"));
    assertEquals("3", url.queryParameter("Limit_Questions"));
    assertTrue(url.queryParameterValues("Sort_Reviews").contains("HasPhotos:desc"));
    assertEquals("Reviews", url.queryParameter("Stats"));
  }

  @Test
  public void bulkProductDisplayFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullBulkProductRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "products.json");
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

    requestFactoryUtils.assertContainsGenericProperties(okRequest);
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
  public void submissionBvHostedAuthStage1_shouldChooseHostedAuthProviderOnly() throws Exception {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Submit, "prod1")
        .authenticationProvider(new BVHostedAuthenticationProvider("red@email.com", "https://red.com/auth"))
        .hostedAuthenticationEmail("blue@email.com")
        .hostedAuthenticationCallback("https://blue.com/auth")
        .user("uasbleh")
        .build();
    final Request okRequest = requestFactory.create(request);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_authenticationemail", "red%40email.com");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_callbackurl", "https%3A%2F%2Fred.com%2Fauth");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "User", "uasbleh", false);
  }

  @Test
  public void submissionBvHostedAuthStage2_shouldChooseHostedAuthProviderOnly() throws Exception {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Submit, "prod1")
        .authenticationProvider(new BVHostedAuthenticationProvider("bvhosteduas"))
        .hostedAuthenticationEmail("blue@email.com")
        .hostedAuthenticationCallback("https://blue.com/auth")
        .user("uasbleh")
        .build();
    final Request okRequest = requestFactory.create(request);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_authenticationemail", "blue%40email.com", false);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_callbackurl", "https%3A%2F%2Fblue.com%2Fauth", false);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "User", "bvhosteduas");
  }

  @Test
  public void submissionSiteAuth_shouldChooseSiteAuthOnly() throws Exception {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Submit, "prod1")
        .authenticationProvider(new SiteAuthenticationProvider("siteauthuas"))
        .hostedAuthenticationEmail("blue@email.com")
        .hostedAuthenticationCallback("https://blue.com/auth")
        .user("uasbleh")
        .build();
    final Request okRequest = requestFactory.create(request);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_authenticationemail", "blue%40email.com", false);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_callbackurl", "https%3A%2F%2Fblue.com%2Fauth", false);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "User", "siteauthuas");
  }

  @Test
  public void submissionAuthPieceMeal_shouldExist() throws Exception {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Submit, "prod1")
        .hostedAuthenticationEmail("blue@email.com")
        .hostedAuthenticationCallback("https://blue.com/auth")
        .user("uasbleh")
        .build();
    final Request okRequest = requestFactory.create(request);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_authenticationemail", "blue%40email.com");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "hostedauthentication_callbackurl", "https%3A%2F%2Fblue.com%2Fauth");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "User", "uasbleh");
  }

  @Test
  public void feedbackSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullFeedbackSubmissionRequest();
    System.out.println(okRequest.toString());
    assertNotNull(okRequest);
    requestFactoryUtils.assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void feedbackSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullFeedbackSubmissionRequest();
    requestFactoryUtils.assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void feedbackSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullFeedbackSubmissionRequest();
    requestFactoryUtils.assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void feedbackSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullFeedbackSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ContentId", "review1");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ContentType", "review");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "FeedbackType", "helpfulness");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "Vote", "positive");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ReasonText", "Fantastic%20review.%20Would%20recommend%20100%25%21");
  }

  @Test
  public void feedbackSubmissionMustAlwaysBePreviewForAction() throws Exception {
    final FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder("content1").build();
    final Request okRequest = requestFactory.create(request);
    System.out.println(okRequest);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "action", "Preview");
  }

  @Test
  public void feedbackSubmissionFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullFeedbackSubmissionRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "submitfeedback.json");
  }

  @Test
  public void reviewSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest();
    requestFactoryUtils.assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void reviewSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest();
    requestFactoryUtils.assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void reviewSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest();
    requestFactoryUtils.assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void reviewSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest();

    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ProductId", "prod1");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "IsRecommended", "true");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "SendEmailAlertWhenCommented", "true");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "Rating", "4");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterScore", "3");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "Title", "This%20is%20great%21");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ReviewText", "This%20is%20something%20I%20would%20recommend%20100%25%20%3AD");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterComment", "Something%20about%20promotion");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "tag_foo_0", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "tag_baz_1", "quux");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "tagid_foo%2Fbar", "baz");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "additionalfield_foo", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "contextdatavalue_foo", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "rating_foo", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "rating_baz", "2");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "VideoUrl_1", "https%3A%2F%2Fwww.website.com%2Fvideo.mp4");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "VideoCaption_1", "Totes%20cool%20100%25%21");
  }

  @Test
  public void reviewSubmissionShouldHaveFingerprintProvided() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "fp", "test_fp");
  }

  @Test
  public void reviewSubmissionFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "submitreview.json");
  }

  @Test
  public void reviewSubmissionWithActionForm_Should_notContainAnActionParam() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest(Action.Form);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "action", "Form", false);
  }

  @Test
  public void reviewSubmissionWithActionPreview_Should_containActionPreview() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest(Action.Preview);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "action", "Preview");
  }

  @Test
  public void reviewSubmissionWithActionSubmit_Should_containActionSubmit() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullReviewSubmissionRequest(Action.Submit);
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "action", "Submit");
  }

  @Test
  public void submissionShouldNotAllowNullFormParams() throws Exception {
    final ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Preview, "prod1")
        .isRecommended(null)
        .build();
    final Request okRequest = requestFactory.create(request);
    requestFactoryUtils.assertFormBodyContainsKeyEncoded(okRequest, "IsRecommended", false);
  }

  @Test
  public void storeReviewSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewSubmissionRequest();
    requestFactoryUtils.assertContainsBVSDKFormParams(okRequest, "storeApiKeyBar");
  }

  @Test
  public void storeReviewSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewSubmissionRequest();
    requestFactoryUtils.assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void storeReviewSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewSubmissionRequest();
    requestFactoryUtils.assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void storeReviewSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewSubmissionRequest();

    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ProductId", "store1");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "IsRecommended", "true");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "SendEmailAlertWhenCommented", "true");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "Rating", "4");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterScore", "3");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "Title", "This%20is%20great%21");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ReviewText", "This%20is%20something%20I%20would%20recommend%20100%25%20%3AD");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "NetPromoterComment", "Something%20about%20promotion");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "tag_foo_0", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "tag_baz_1", "quux");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "tagid_foo%2Fbar", "baz");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "additionalfield_foo", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "contextdatavalue_foo", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "rating_foo", "bar");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "rating_baz", "2");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "VideoUrl_1", "https%3A%2F%2Fwww.website.com%2Fvideo.mp4");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "VideoCaption_1", "Totes%20cool%20100%25%21");
  }

  @Test
  public void storeReviewSubmissionShouldHaveFingerprintProvided() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "fp", "test_fp");
  }

  @Test
  public void storeReviewSubmissionFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullStoreReviewSubmissionRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "submitreview.json");
  }

  @Test
  public void questionSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQuestionSubmissionRequest();
    requestFactoryUtils.assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void questionSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQuestionSubmissionRequest();
    requestFactoryUtils.assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void questionSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQuestionSubmissionRequest();

    /**
     * queryParams.put(kPRODUCT_ID, productId);
     queryParams.put(kQUESTION_SUMMARY, questionSummary);
     queryParams.put(kQUESTION_DETAILS, questionDetails);
     queryParams.put(kIS_ANONUSER, isUserAnonymous);
     queryParams.put(kSEND_EMAIL_ANSWERED, sendEmailAlertWhenAnswered);
     */
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ProductId", "prod1");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "QuestionSummary", "Does%20it%20have%20over%209000%25%20UV%20protection%3F");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "QuestionDetails", "Percentage%20being%209001%25%20or%20higher");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "IsUserAnonymous", "true");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "SendEmailAlertWhenAnswered", "true");
  }

  @Test
  public void questionSubmissionShouldHaveFingerprintProvided() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQuestionSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "fp", "test_fp");
  }

  @Test
  public void questionSubmissionFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullQuestionSubmissionRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "submitquestion.json");
  }

  @Test
  public void answerSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAnswerSubmissionRequest();
    requestFactoryUtils.assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void answerSubmissionShouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAnswerSubmissionRequest();
    requestFactoryUtils.assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void answerSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAnswerSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "QuestionId", "question1");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "AnswerText", "Some%20great%20answer%20%24%25%20here");
  }

  @Test
  public void answerSubmissionShouldHaveFingerprintProvided() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAnswerSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "fp", "test_fp");
  }

  @Test
  public void answerSubmissionFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullAnswerSubmissionRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "submitanswer.json");
  }

  @Test
  public void commentSubmissionShouldHaveBVSDKFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentSubmissionRequest();
    requestFactoryUtils.assertContainsBVSDKFormParams(okRequest);
  }

  @Test
  public void commentSubmissionshouldHaveBVMobileInfoFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentSubmissionRequest();
    requestFactoryUtils.assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void commentSubmissionShouldHaveSubmissionRequestFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentSubmissionRequest();
    requestFactoryUtils.assertContainsConversationsSubmissionRequestFormParams(okRequest);
  }

  @Test
  public void commentSubmissionShouldHaveOwnFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "ReviewId", "review1");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "CommentText", "My%20%23commenthere");
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "Title", "Something%20**great**%21");
  }

  @Test
  public void commentSubmissionShouldHaveFingerprintProvided() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentSubmissionRequest()  ;
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "fp", "test_fp");
  }

  @Test
  public void commentSubmissionFinalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullCommentSubmissionRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "submitreviewcomment.json");
  }

  @Test
  public void userAuthTokenSubmission_shouldHave_BVMobileInfoFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullUserAuthTokenSubmissionRequest();
    requestFactoryUtils.assertContainsBVMobileInfoFormParams(okRequest);
  }

  @Test
  public void userAuthTokenSubmission_shouldHave_ownFormParams() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullUserAuthTokenSubmissionRequest();
    requestFactoryUtils.assertFormBodyContainsKeyValEncoded(okRequest, "authtoken", "testAuthToken");
  }

  @Test
  public void userAuthTokenSubmission_finalPath() throws Exception {
    final Request okRequest = requestFactoryUtils.createFullUserAuthTokenSubmissionRequest();
    requestFactoryUtils.assertFinalPathIs(okRequest.url(), "authenticateuser.json");
  }

    @Test
    public void shouldCreatePhotoUploadRequest() {
        PhotoUpload photoUpload = new PhotoUpload(new File("filename"), "PhotoTesting", PhotoUpload.ContentType.REVIEW);
        PhotoUploadRequest request = new PhotoUploadRequest.Builder(photoUpload).build();
        assertNotNull(request);
    }


}