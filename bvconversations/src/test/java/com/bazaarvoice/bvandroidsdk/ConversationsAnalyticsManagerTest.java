package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ConversationsAnalyticsManagerTest extends BVBaseTest {

  private BvStubData stubData;
  private Gson gson;

  @Before
  public void setUp() throws Exception {
    stubData = new BvStubData();
    gson = new GsonBuilder().setPrettyPrinting().create();
  }

  @Test
  public void displayReviewsResponseShouldSendEvents() throws Exception {
    ConversationsAnalyticsManager subject = new ConversationsAnalyticsManager(BVSDK.getInstance().getBvPixel());
    String reviewsResponseJsonStr = jsonFileAsString("reviews_all_reviews.json");
    ReviewResponse reviewResponse = gson.fromJson(reviewsResponseJsonStr, ReviewResponse.class);

    subject.sendSuccessfulConversationsDisplayResponse(reviewResponse);

    ArgumentCaptor<BVMobileAnalyticsEvent> eventArgumentCaptor = ArgumentCaptor.forClass(BVMobileAnalyticsEvent.class);
    verify(bvPixel, atLeastOnce()).track(eventArgumentCaptor.capture());
    List<BVMobileAnalyticsEvent> events = eventArgumentCaptor.getAllValues();

    int impressionEventCount = 0;
    int pageViewEventCount = 0;

    for (BVMobileAnalyticsEvent event : events) {
      event.setBvMobileParams(stubData.getBvMobileParams());
      if (event instanceof BVImpressionEvent) {
        impressionEventCount++;
      } else if (event instanceof BVPageViewEvent) {
        pageViewEventCount++;
      }
    }

    assertEquals(10, impressionEventCount);
    assertEquals(1, pageViewEventCount);
  }

  @Test
  public void submitReviewRequestShouldSendEvents() throws Exception {
    ReviewSubmissionRequest request = new ReviewSubmissionRequest.Builder(Action.Submit, "prod123").build();
    submitRequestShouldSendEvent(request);
  }

  @Test
  public void submitQuestionRequestShouldSendEvents() throws Exception {
    QuestionSubmissionRequest request = new QuestionSubmissionRequest.Builder(Action.Submit, "prod123").build();
    submitRequestShouldSendEvent(request);
  }

  @Test
  public void submitAnswerRequestShouldSendEvents() throws Exception {
    AnswerSubmissionRequest request = new AnswerSubmissionRequest.Builder(Action.Submit, "question123", "answer text goes here").build();
    submitRequestShouldSendEvent(request);
  }

  @Test
  public void submitFeedbackRequestShouldSendEvents() throws Exception {
    FeedbackSubmissionRequest request = new FeedbackSubmissionRequest.Builder("reviewId")
        .feedbackType(FeedbackType.HELPFULNESS)
        .feedbackContentType(FeedbackContentType.REVIEW)
        .build();
    submitRequestShouldSendEvent(request);
  }

  private void submitRequestShouldSendEvent(ConversationsSubmissionRequest request) throws Exception {
    ConversationsAnalyticsManager subject = new ConversationsAnalyticsManager(BVSDK.getInstance().getBvPixel());

    subject.sendSuccessfulConversationsSubmitResponse(request);

    ArgumentCaptor<BVMobileAnalyticsEvent> eventArgumentCaptor = ArgumentCaptor.forClass(BVMobileAnalyticsEvent.class);
    verify(bvPixel, atLeastOnce()).track(eventArgumentCaptor.capture());
    List<BVMobileAnalyticsEvent> events = eventArgumentCaptor.getAllValues();

    int featureUsedEventCount = 0;

    for (BVMobileAnalyticsEvent event : events) {
      event.setBvMobileParams(stubData.getBvMobileParams());
      System.out.println(event.toRaw());
      if (event instanceof BVFeatureUsedEvent) {
        featureUsedEventCount++;
      }
    }

    assertEquals(1, featureUsedEventCount);
  }
}
