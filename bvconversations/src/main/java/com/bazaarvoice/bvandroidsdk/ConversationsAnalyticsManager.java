/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.types.FeedbackType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class that wraps {@code AnalyticsManger}, builds and enqueues
 * Conversations specific Analytic events.
 */
class ConversationsAnalyticsManager {
  // region Properties

  private static ConversationsAnalyticsManager instance;

  private final BVPixel bvPixel;

  // endregion

  // region Constructor

  ConversationsAnalyticsManager(BVPixel bvPixel) {
    this.bvPixel = bvPixel;
  }

  // endregion

  // region API

  public static ConversationsAnalyticsManager getInstance(BVSDK bvsdk) {
    if (instance == null) {
      instance = new ConversationsAnalyticsManager(bvsdk.getBvPixel());
    }
    return instance;
  }

  /**
   * Route all Display API Responses here to dispatch events
   *
   * @param conversationResponse
   */
  void sendSuccessfulConversationsDisplayResponse(ConversationsResponse conversationResponse) {
    if (conversationResponse instanceof ReviewResponse) {
      ReviewResponse reviewResponse = (ReviewResponse) conversationResponse;
      sendUgcImpressionEventReviews(reviewResponse.getResults());
      sendReviewsProductPageView(reviewResponse.getResults());
    }
    else if (conversationResponse instanceof StoreReviewResponse) {
      StoreReviewResponse storeReviewResponse = (StoreReviewResponse) conversationResponse;
      sendUgcImpressionEventStoreReviews(storeReviewResponse.getResults());
    }
    else if (conversationResponse instanceof BulkStoreResponse) {
      BulkStoreResponse bulkStoresResponse = (BulkStoreResponse) conversationResponse;
      sendUgcImpressionEventStores(bulkStoresResponse.getResults());
    }
    else if (conversationResponse instanceof QuestionAndAnswerResponse) {
      QuestionAndAnswerResponse questionAndAnswerResponse = (QuestionAndAnswerResponse) conversationResponse;
      sendUgcImpressionEventQAndA(questionAndAnswerResponse.getResults());
      sendQAndAProductPageView(questionAndAnswerResponse.getResults());
    }
    else if (conversationResponse instanceof ProductDisplayPageResponse) {
      ProductDisplayPageResponse productDisplayPageResponse = (ProductDisplayPageResponse) conversationResponse;
      sendPdpProductPageView(productDisplayPageResponse.getResults());
    } else if (conversationResponse instanceof AuthorsResponse) {
      AuthorsResponse authorsResponse = (AuthorsResponse) conversationResponse;
      sendUsedFeatureDisplayAuthors(authorsResponse);
    }
  }

  /**
   * Route all Submit API Responses here to dispatch events
   *
   * @param request
   */
  void sendSuccessfulConversationsSubmitResponse(ConversationsSubmissionRequest request) {
    boolean hasFingerPrint = request.getFingerprint() != null && !request.getFingerprint().isEmpty();

    if (request instanceof QuestionSubmissionRequest) {
      QuestionSubmissionRequest questionSubmissionRequest = (QuestionSubmissionRequest) request;
      String productId = questionSubmissionRequest.getProductId();
      sendUsedFeatureUgcContentSubmission(
          BVEventValues.BVProductType.CONVERSATIONS_QANDA,
          BVEventValues.BVFeatureUsedEventType.ASK_QUESTION,
          productId,
          hasFingerPrint);
    }
    if (request instanceof AnswerSubmissionRequest) {
      AnswerSubmissionRequest answerSubmissionRequest = (AnswerSubmissionRequest) request;
      String questionId = answerSubmissionRequest.getProductId();
      sendUsedFeatureUgcContentSubmission(
          BVEventValues.BVProductType.CONVERSATIONS_QANDA,
          BVEventValues.BVFeatureUsedEventType.ANSWER_QUESTION,
          // TODO: might be able to get this if we were parsing response instead of request
          "none",
          hasFingerPrint,
          // TODO: might make more sense to use answerId here which we could also get if we were parsing response instead of request
          questionId,
          BVEventValues.BVImpressionContentType.QUESTION.toString());
    }
    if (request instanceof ReviewSubmissionRequest) {
      ReviewSubmissionRequest reviewSubmissionRequest = (ReviewSubmissionRequest) request;
      String productId = reviewSubmissionRequest.getProductId();
      sendUsedFeatureUgcContentSubmission(
          BVEventValues.BVProductType.CONVERSATIONS_REVIEWS,
          BVEventValues.BVFeatureUsedEventType.WRITE_REVIEW,
          productId,
          hasFingerPrint);
    }
    if (request instanceof FeedbackSubmissionRequest) {
      FeedbackSubmissionRequest feedbackSubmissionRequest = (FeedbackSubmissionRequest) request;
      String contentId = feedbackSubmissionRequest.getContentId();
      String contentType = feedbackSubmissionRequest.getContentType();
      String feedbackType = feedbackSubmissionRequest.getFeedbackType();
      sendUsedFeatureUgcFeedbackSubmission(
          // TODO: might be able to get this if we were parsing response instead of request
          "none",
          contentId,
          contentType,
          feedbackType);
    }
  }

  /**
   * Route all Photo Upload Responses here to dispatch events
   *
   * @param request
   */
  void sendSuccessfulConversationsPhotoUpload(ConversationsSubmissionRequest request) {
    if (request instanceof ReviewSubmissionRequest) {
      ReviewSubmissionRequest reviewSubmissionRequest = (ReviewSubmissionRequest) request;
      ReviewSubmissionRequest.Builder reviewSubmissionBuilder = (ReviewSubmissionRequest.Builder) reviewSubmissionRequest.getBuilder();
      String productId = reviewSubmissionBuilder.productId;

      BVFeatureUsedEvent event = new BVFeatureUsedEvent(
          productId,
          BVEventValues.BVProductType.CONVERSATIONS_REVIEWS,
          BVEventValues.BVFeatureUsedEventType.PHOTO,
          null);
      bvPixel.track(event);
    }
  }

  /**
   * Route non-response dependent Used-Feature events dispatching here
   *
   * @param productId
   * @param containerId
   * @param bvProductType
   */
  public void sendUsedFeatureInViewEvent(String productId, String containerId, BVEventValues.BVProductType bvProductType) {
    // TODO: Add Integration Test for all the Views calling this
    if (productId == null || containerId == null) {
      return;
    }
    BVInViewEvent event = new BVInViewEvent(productId, containerId, bvProductType, null);
    bvPixel.track(event);
  }

  /**
   * Route non-response dependent Used-Feature events dispatching here
   *
   * @param productId
   * @param bvProductType
   */
  public void sendUsedFeatureScrolledEvent(String productId, BVEventValues.BVProductType bvProductType) {
    productId = productId == null ? "" : productId;
    BVFeatureUsedEvent event = new BVFeatureUsedEvent(
        productId,
        bvProductType,
        BVEventValues.BVFeatureUsedEventType.SCROLLED,
        null);
    bvPixel.track(event);
  }

  // endregion

  // region Helper Methods

  private void sendUgcImpressionEvent(String productId, String contentId, BVEventValues.BVProductType bvProductType, BVEventValues.BVImpressionContentType bvImpressionContentType, String categoryId, String brand) {
    BVImpressionEvent event = new BVImpressionEvent(
        productId, contentId, bvProductType, bvImpressionContentType, categoryId, brand);
    bvPixel.track(event);
  }

  private void sendUgcImpressionEventReviews(List<Review> reviews) {
    for (Review review : reviews) {
      String productId = "", contentId = "";
      if (review != null) {
        productId = review.getProductId();
        contentId = review.getId();
      }
      sendUgcImpressionEvent(productId, contentId, BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, BVEventValues.BVImpressionContentType.REVIEW, null, null);
    }
  }

  private void sendUgcImpressionEventStoreReviews(List<StoreReview> reviews) {
    for (StoreReview review : reviews) {
      String productId = "", contentId = "";
      if (review != null) {
        productId = review.getProductId();
        contentId = review.getId();
      }
      sendUgcImpressionEvent(productId, contentId, BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, BVEventValues.BVImpressionContentType.STORE_REVIEW, null, null);
    }
  }

  private void sendUgcImpressionEventStores(List<Store> stores) {
    for (Store store : stores) {
      String productId = "";
      if (store != null) {
        productId = store.getId();
      }
      sendUgcImpressionEvent(productId, "", BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, BVEventValues.BVImpressionContentType.STORE, null, null);
    }
  }

  private void sendUgcImpressionEventQAndA(List<Question> questions) {
    for (Question question : questions) {
      String productId = "", questionId = "";
      if (question != null) {
        productId = question.getProductId();
        questionId = question.getId();

        List<Answer> answers = question.getAnswers();
        if (answers != null) {
          for (Answer answer : answers) {
            String answerId = "";
            if (answer != null) {
              answerId = answer.getId();
            }
            sendUgcImpressionEvent(productId, answerId, BVEventValues.BVProductType.CONVERSATIONS_QANDA, BVEventValues.BVImpressionContentType.ANSWER, null, null);
          }
        }
      }
      sendUgcImpressionEvent(productId, questionId, BVEventValues.BVProductType.CONVERSATIONS_QANDA, BVEventValues.BVImpressionContentType.QUESTION, null, null);
    }
  }

  private void sendReviewsProductPageView(List<Review> reviews) {
    if (reviews == null || reviews.isEmpty()) {
      return;
    }
    Review review = reviews.get(0);
    if (review != null) {
      Product product = review.getProduct();
      String productId = review.getProductId();
      sendProductPageView(BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, productId, product);
    }
  }

  private void sendQAndAProductPageView(List<Question> questions) {
    if (questions == null || questions.isEmpty()) {
      return;
    }
    Question question = questions.get(0);
    if (question != null) {
      Product product = question.getProduct();
      String productId = question.getProductId();
      sendProductPageView(BVEventValues.BVProductType.CONVERSATIONS_QANDA, productId, product);
    }
  }

  private void sendPdpProductPageView(List<Product> products) {
    if (products == null || products.isEmpty()) {
      return;
    }
    Product product = products.get(0);
    String productId = product.getId();
    sendProductPageView(BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, productId, product);
  }

  private void sendProductPageView(@NonNull BVEventValues.BVProductType bvProductType, @NonNull String productId, @Nullable Product product) {
    Map<String, Object> extraParams = new HashMap<>();
    String categoryId = null;
    if (product != null) {
      categoryId = product.getCategoryId() == null ? "" : product.getCategoryId();

      if (product.getQaStatistics() != null) {
        int numQuestions = product.getQaStatistics().getTotalQuestionCount();
        extraParams.put("numQuestions", numQuestions);
      }
      if (product.getQaStatistics() != null) {
        int numAnswers = product.getQaStatistics().getTotalAnswerCount();
        extraParams.put("numAnswers", numAnswers);
      }
      if (product.getReviewStatistics() != null) {
        int numReviews = product.getReviewStatistics().getTotalReviewCount();
        extraParams.put("numReviews", numReviews);
      }
      if (product.getBrand() != null && product.getBrand().get("name") != null) {
        extraParams.put("brand", product.getBrandExternalId());
      }
    }
    BVPageViewEvent event = new BVPageViewEvent(productId, bvProductType, categoryId);
    event.setAdditionalParams(extraParams);
    bvPixel.track(event);
  }

  private void sendUsedFeatureUgcContentSubmission(
      @NonNull BVEventValues.BVProductType bvProductType,
      @NonNull BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType,
      @Nullable String productId, boolean hasFingerprint) {
    sendUsedFeatureUgcContentSubmission(bvProductType, bvFeatureUsedEventType, productId, hasFingerprint, null, null);
  }

  private void sendUsedFeatureUgcContentSubmission(
      @NonNull BVEventValues.BVProductType bvProductType,
      @NonNull BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType,
      @Nullable String productId, boolean hasFingerprint,
      @Nullable String contentId,
      @Nullable String contentType) {
    productId = productId == null ? "none" : productId;

    BVFeatureUsedEvent event = new BVFeatureUsedEvent(productId, bvProductType, bvFeatureUsedEventType, null);

    Map<String, Object> extraParams = new HashMap<>();
    extraParams.put(BVEventKeys.FeatureUsedEvent.HAS_FINGERPRINT, hasFingerprint);

    if (contentId != null) {
      extraParams.put(BVEventKeys.FeatureUsedEvent.CONTENT_ID, contentId);
    }
    if (contentType != null) {
      extraParams.put(BVEventKeys.FeatureUsedEvent.BV_CONTENT_TYPE, contentType);
    }

    event.setAdditionalParams(extraParams);

    bvPixel.track(event);
  }

  private void sendUsedFeatureUgcFeedbackSubmission(
      @NonNull String productId,
      @Nullable String contentId,
      @Nullable String contentType,
      @Nullable String feedbackType) {
    // TODO: These should be required nonnull params for building these feedback submission events
    Map<String, Object> extraParams = new HashMap<>();
    if (contentId != null) {
      extraParams.put(BVEventKeys.FeatureUsedEvent.CONTENT_ID, contentId);
    }
    if (contentType != null) {
      extraParams.put(BVEventKeys.FeatureUsedEvent.BV_CONTENT_TYPE, contentType);
    }
    if (feedbackType != null) {
      extraParams.put(BVEventKeys.FeatureUsedEvent.DETAIL_1, feedbackType);
    }

    BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType = feedbackTypeToFeatureUsedEventType(feedbackType);

    BVFeatureUsedEvent event = new BVFeatureUsedEvent(productId, BVEventValues.BVProductType.CONVERSATIONS_REVIEWS, bvFeatureUsedEventType, null);
    event.setAdditionalParams(extraParams);
    bvPixel.track(event);
  }

  private BVEventValues.BVFeatureUsedEventType feedbackTypeToFeatureUsedEventType(String feedbackType) {
    BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType = BVEventValues.BVFeatureUsedEventType.FEEDBACK;

    if (feedbackType == null || feedbackType.isEmpty()) {
      return bvFeatureUsedEventType;
    }

    if (feedbackType.equals(FeedbackType.HELPFULNESS.getTypeString())) {
      bvFeatureUsedEventType = BVEventValues.BVFeatureUsedEventType.FEEDBACK;
    } else if (feedbackType.equals(FeedbackType.INAPPROPRIATE.getTypeString())) {
      bvFeatureUsedEventType = BVEventValues.BVFeatureUsedEventType.INAPPROPRIATE;
    }

    return bvFeatureUsedEventType;
  }

  private void sendUsedFeatureDisplayAuthors(AuthorsResponse authorsResponse) {
    if (!authorsResponse.getHasErrors()) {
      List<Author> authors = authorsResponse.getResults();
      for (Author author : authors) {
        sendUsedFeatureDisplayAuthor(author.getId());
      }
    }
  }

  private void sendUsedFeatureDisplayAuthor(String profileId) {
    String productId = "none"; // TODO GOATZ This matches iOS but seems wrong
    BVEventValues.BVProductType bvProductType = BVEventValues.BVProductType.CONVERSATIONS_PROFILE;
    BVEventValues.BVFeatureUsedEventType bvFeatureUsedEventType = BVEventValues.BVFeatureUsedEventType.PROFILE;
    BVFeatureUsedEvent event = new BVFeatureUsedEvent(productId, bvProductType, bvFeatureUsedEventType, null);

    Map<String, Object> extraParams = new HashMap<>();
    extraParams.put("interaction", false);
    extraParams.put("page", profileId);
    event.setAdditionalParams(extraParams);

    bvPixel.track(event);
  }

  // endregion
}
